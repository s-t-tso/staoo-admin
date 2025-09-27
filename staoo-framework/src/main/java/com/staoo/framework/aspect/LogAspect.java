package com.staoo.framework.aspect;

import com.alibaba.fastjson2.JSON;
import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.OperationLogBase;
import com.staoo.common.domain.TableResult;
import com.staoo.common.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 操作日志切面
 * 用于拦截带有@LogOperation注解的方法，记录操作日志
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.staoo.common.annotation.LogOperation)")
    public void logPointCut() {
    }

    /**
     * 环绕通知
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;

        try {
            // 执行目标方法
            result = point.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            // 异步记录操作日志
            final Exception finalException = exception;
            final ProceedingJoinPoint finalPoint = point;
            final long finalStartTime = startTime;
            final Object finalResult = result;
            CompletableFuture.runAsync(() -> {
                try {
                    if (finalException == null || !getLogOperation(finalPoint).ignoreException()) {
                        saveOperationLog(finalPoint, finalResult, finalException, finalStartTime);
                    }
                } catch (Exception e) {
                    logger.error("记录操作日志失败", e);
                }
            });
        }
    }

    /**
     * 保存操作日志
     */
    private void saveOperationLog(ProceedingJoinPoint point, Object result, Exception exception, long startTime) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取LogOperation注解
        LogOperation logOperation = getLogOperation(point);

        // 构建操作日志
        OperationLogBase operationLogBase = new OperationLogBase();

        // 设置操作基本信息
        operationLogBase.setModule(logOperation.module());
        operationLogBase.setOperationType(logOperation.operationType());
        operationLogBase.setContent(logOperation.content());

        // 设置时间信息
        operationLogBase.setCreateTime(LocalDateTime.now());
        operationLogBase.setUpdateTime(LocalDateTime.now());
        operationLogBase.setOperationTime(LocalDateTime.now());
        operationLogBase.setExecutionTime(System.currentTimeMillis() - startTime);

        // 设置请求信息
        operationLogBase.setRequestUrl(request.getRequestURI());
        operationLogBase.setRequestMethod(request.getMethod());
        operationLogBase.setIp(getIp(request));

        // 设置用户信息
        // 这里应该从上下文中获取当前登录用户信息
        // 例如: operationLogBase.setUserId(getCurrentUserId());
        // 暂时设置为1
        operationLogBase.setUserId(1L);

        // 设置请求参数
        if (logOperation.recordRequestParams()) {
            String params = getParams(point);
            operationLogBase.setRequestParams(params);
        }

        // 设置请求体
        if (logOperation.recordRequestBody()) {
            // 这里需要根据具体情况获取请求体
            // 例如通过自定义的RequestWrapper获取
        }

        // 设置响应结果
        if (logOperation.recordResponseResult() && result != null) {
            String responseResult = getResponseResult(result);
            operationLogBase.setResponseResult(responseResult);
        }

        // 设置状态和错误信息
        if (exception != null) {
            operationLogBase.setStatus(0);
            operationLogBase.setErrorMessage(exception.getMessage());
        } else {
            operationLogBase.setStatus(1);
        }

        // 设置浏览器和操作系统信息
        String userAgent = request.getHeader("User-Agent");
        operationLogBase.setBrowser(getBrowser(userAgent));
        operationLogBase.setOs(getOs(userAgent));

        // 保存操作日志
        operationLogService.save(operationLogBase);
    }

    /**
     * 获取LogOperation注解
     */
    private LogOperation getLogOperation(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(LogOperation.class);
    }

    /**
     * 获取请求参数
     */
    private String getParams(ProceedingJoinPoint point) {
        try {
            Object[] args = point.getArgs();
            if (args == null || args.length == 0) {
                return "";
            }

            // 过滤敏感参数和文件类型参数
            Object[] filteredArgs = Arrays.stream(args)
                    .filter(arg -> !(arg instanceof MultipartFile || arg instanceof MultipartFile[]))
                    .toArray();

            // 将参数转换为JSON字符串
            return JSON.toJSONString(filteredArgs);
        } catch (Exception e) {
            logger.error("获取请求参数失败", e);
            return "";
        }
    }

    /**
     * 获取响应结果
     */
    private String getResponseResult(Object result) {
        try {
            // 如果是TableResult类型，只保留部分信息
            if (result instanceof TableResult) {
                TableResult tableResult = (TableResult) result;
                Map<String, Object> pageInfo = new HashMap<>();
                pageInfo.put("total", tableResult.getTotal());
                pageInfo.put("page", tableResult.getPage());
                pageInfo.put("pagesize", tableResult.getPagesize());
                pageInfo.put("listSize", tableResult.getRow() != null ? tableResult.getRow().size() : 0);
                return JSON.toJSONString(pageInfo);
            }
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error("获取响应结果失败", e);
            return "";
        }
    }

    /**
     * 获取IP地址
     */
    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理的情况，第一个IP为真实IP
        int index = ip.indexOf(",");
        if (index > 0) {
            ip = ip.substring(0, index);
        }
        return ip;
    }

    /**
     * 获取浏览器信息
     */
    private String getBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("IE")) {
            return "IE";
        }
        return "Unknown";
    }

    /**
     * 获取操作系统信息
     */
    private String getOs(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("iOS")) {
            return "iOS";
        } else if (userAgent.contains("Android")) {
            return "Android";
        }
        return "Unknown";
    }
}

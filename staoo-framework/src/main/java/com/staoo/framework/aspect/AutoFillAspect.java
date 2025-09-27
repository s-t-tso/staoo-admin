package com.staoo.framework.aspect;

import com.staoo.common.annotation.AutoFill;
import com.staoo.common.util.AutoFillUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 自动填充切面
 * 用于通过AOP方式自动填充创建人、创建时间、更新人和更新时间字段
 */
@Aspect
@Component
public class AutoFillAspect {

    private static final Logger logger = LoggerFactory.getLogger(AutoFillAspect.class);

    /**
     * 定义切点：拦截带有@AutoFill注解的方法
     */
    @Pointcut("@annotation(com.staoo.common.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 方法执行前的通知，用于填充创建信息
     * @param joinPoint 连接点
     */
    @Before("autoFillPointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return;
            }

            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Parameter[] parameters = method.getParameters();

            // 遍历参数，找到需要自动填充的参数
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null && AutoFillUtils.needAutoFill(arg)) {
                    // 填充创建信息
                    AutoFillUtils.fillCreateInfo(arg);
                }
            }
        } catch (Exception e) {
            logger.error("自动填充创建信息失败", e);
        }
    }

    /**
     * 方法执行后的通知，用于填充更新信息
     * @param joinPoint 连接点
     * @param result 方法返回结果
     */
    @AfterReturning(pointcut = "autoFillPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 如果方法返回值需要自动填充，则填充更新信息
            if (result != null && AutoFillUtils.needAutoFill(result)) {
                AutoFillUtils.fillUpdateInfo(result);
            }
        } catch (Exception e) {
            logger.error("自动填充更新信息失败", e);
        }
    }
}
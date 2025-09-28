package com.staoo.system.auth.service.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.common.util.UserUtils;
import com.staoo.common.auth.dto.LoginRequest;
import com.staoo.common.auth.dto.LoginResponse;
import com.staoo.system.auth.service.LoginService;
import com.staoo.system.auth.strategy.LoginStrategyFactory;
import com.staoo.system.domain.LoginLog;
import com.staoo.system.service.LoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录服务实现类
 * 实现登录相关的业务逻辑
 */
@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 参数验证
        if (request == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "登录请求不能为空");
        }

        String loginType = request.getLoginType();
        String ip = request.getIp();
        String userAgent = request.getUserAgent();
        String username = request.getUsername();

        try {
            // 根据登录类型获取对应的登录策略
            if (!loginStrategyFactory.supports(loginType)) {
                throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, "不支持的登录方式: " + loginType);
            }

            // 执行登录
            LoginResponse response = loginStrategyFactory.getStrategy(loginType).login(request);

            // 记录登录日志
            recordLoginLog(username, true, "登录成功", ip, userAgent);
            
            return response;
        } catch (BusinessException e) {
            logger.error("登录业务异常: {}", e.getMessage());
            recordLoginLog(username, false, e.getMessage(), ip, userAgent);
            throw e;
        } catch (Exception e) {
            logger.error("登录异常: {}", e.getMessage(), e);
            recordLoginLog(username, false, "登录异常: " + e.getMessage(), ip, userAgent);
            throw new BusinessException(StatusCodeEnum.INTERNAL_SERVER_ERROR, "登录失败，请稍后重试");
        }
    }

    @Override
    public void handleLoginFailure(String username) {
        // 这个方法现在由各个具体的策略实现来处理
        // 保留此方法以保持接口兼容性
        logger.warn("用户 [{}] 登录失败", username);
    }

    @Override
    public boolean isUserLocked(String username) {
        // 这个方法现在由各个具体的策略实现来处理
        // 保留此方法以保持接口兼容性
        return false;
    }

    @Override
    public void recordLoginLog(String username, boolean status, String message, String ip, String userAgent) {
        try {
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setStatus(status ? 1 : 0);
            loginLog.setErrorMsg(message);
            loginLog.setIp(ip);

            // 获取浏览器和操作系统信息
            String browser = getBrowser(userAgent);
            String os = getOs(userAgent);
            loginLog.setBrowser(browser);
            loginLog.setOs(os);

            // 保存登录日志
            loginLogService.addLoginLog(loginLog);
        } catch (Exception e) {
            logger.error("记录登录日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取浏览器信息
     */
    private String getBrowser(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        if (userAgent.contains("MSIE")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else {
            return "Other";
        }
    }

    /**
     * 获取操作系统信息
     */
    private String getOs(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown";
        }

        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Macintosh")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iOS")) {
            return "iOS";
        } else {
            return "Other";
        }
    }
}

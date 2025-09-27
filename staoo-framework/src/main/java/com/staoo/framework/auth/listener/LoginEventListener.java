package com.staoo.framework.auth.listener;

import com.staoo.framework.auth.event.LoginEvent;
import com.staoo.system.auth.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 登录事件监听器
 * 处理登录相关的事件，如记录登录日志等
 */
@Component
public class LoginEventListener {
    private static final Logger logger = LoggerFactory.getLogger(LoginEventListener.class);

    @Autowired
    private LoginService loginService;

    /**
     * 处理登录事件
     * @param event 登录事件
     */
    @EventListener
    public void handleLoginEvent(LoginEvent event) {
        if (event.isSuccess()) {
            handleLoginSuccess(event);
        } else {
            handleLoginFailure(event);
        }
    }

    /**
     * 处理登录成功事件
     * @param event 登录事件
     */
    private void handleLoginSuccess(LoginEvent event) {
        // 记录登录成功日志
        logger.info("用户登录成功: {}", event);

        // 记录登录日志
        loginService.recordLoginLog(
            event.getUsername(),
            true,
            "登录成功",
            event.getIpAddress(),
            event.getUserAgent()
        );
    }

    /**
     * 处理登录失败事件
     * @param event 登录事件
     */
    private void handleLoginFailure(LoginEvent event) {
        // 记录登录失败日志
        logger.warn("用户登录失败: {}", event);

        // 处理登录失败，可能导致账号锁定
        loginService.handleLoginFailure(event.getUsername());
        // 记录登录日志
        loginService.recordLoginLog(
            event.getUsername(),
            false,
            event.getMessage(),
            event.getIpAddress(),
            event.getUserAgent()
        );
    }
}

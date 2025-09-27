package com.staoo.framework.auth.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 认证事件发布者
 * 用于发布认证相关的事件
 */
@Component
public class AuthEventPublisher {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 发布登录成功事件
     */
    public void publishLoginSuccessEvent(LoginEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布登录失败事件
     */
    public void publishLoginFailureEvent(LoginEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布登出事件
     */
    public void publishLogoutEvent(LogoutEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布会话过期事件
     */
    public void publishSessionExpiredEvent(SessionEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布账号锁定事件
     */
    public void publishAccountLockedEvent(AccountEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布账号解锁事件
     */
    public void publishAccountUnlockedEvent(AccountEvent event) {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发布密码修改事件
     */
    public void publishPasswordChangedEvent(AccountEvent event) {
        eventPublisher.publishEvent(event);
    }
}
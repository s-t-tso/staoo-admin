package com.staoo.framework.auth.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 账号事件
 * 封装账号相关的事件信息，如账号锁定、解锁、密码修改等
 */
public class AccountEvent extends ApplicationEvent {
    private final String username;
    private final EventType eventType;
    private final String message;
    private final LocalDateTime timestamp;
    private final String operator;

    public AccountEvent(Object source, String username, EventType eventType, String message, String operator) {
        super(source);
        this.username = username;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.operator = operator;
    }

    // 账号事件类型枚举
    public enum EventType {
        ACCOUNT_LOCKED,     // 账号锁定
        ACCOUNT_UNLOCKED,   // 账号解锁
        PASSWORD_CHANGED,   // 密码修改
        PROFILE_UPDATED,    // 资料更新
        ACCOUNT_CREATED,    // 账号创建
        ACCOUNT_DELETED,    // 账号删除
        ACCOUNT_ENABLED,    // 账号启用
        ACCOUNT_DISABLED    // 账号禁用
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getAccountTimestamp() {
        return timestamp;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "AccountEvent{" +
                "username='" + username + '\'' +
                ", eventType=" + eventType +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", operator='" + operator + '\'' +
                '}';
    }
}
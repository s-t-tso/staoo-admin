package com.staoo.framework.auth.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * 会话事件
 * 封装会话相关的事件信息，如会话过期等
 */
public class SessionEvent extends ApplicationEvent {
    private final String username;
    private final String sessionId;
    private final EventType eventType;
    private final LocalDateTime timestamp;
    private final String deviceId;
    private final String deviceType;

    public SessionEvent(Object source, String username, String sessionId, EventType eventType,
                      String deviceId, String deviceType) {
        super(source);
        this.username = username;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    // 会话事件类型枚举
    public enum EventType {
        // 会话创建
        SESSION_CREATED,
        // 会话过期
        SESSION_EXPIRED,
        // 会话无效化
        SESSION_INVALIDATED,
        // 会话续期
        SESSION_RENEWED
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public LocalDateTime getSessionTimestamp() {
        return timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    @Override
    public String toString() {
        return "SessionEvent{" +
                "username='" + username + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", eventType=" + eventType +
                ", sessionTimestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
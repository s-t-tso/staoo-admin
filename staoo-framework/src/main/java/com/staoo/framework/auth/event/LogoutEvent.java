package com.staoo.framework.auth.event;

import org.springframework.context.ApplicationEvent;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 登出事件
 * 封装登出相关的事件信息
 */
public class LogoutEvent extends ApplicationEvent {
    private final String username;
    private final String ipAddress;
    private final String userAgent;
    private final LocalDateTime timestamp;
    private final String deviceId;
    private final String deviceType;

    public LogoutEvent(Object source, String username, String ipAddress, String userAgent,
                      String deviceId, String deviceType) {
        super(source);
        this.username = username;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.timestamp = LocalDateTime.now();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public LogoutEvent(Object source, String username, HttpServletRequest request,
                      String deviceId, String deviceType) {
        super(source);
        this.username = username;
        this.ipAddress = getClientIp(request);
        this.userAgent = request.getHeader("User-Agent");
        this.timestamp = LocalDateTime.now();
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    // 获取客户端真实IP
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个代理的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LocalDateTime getLogoutTimestamp() {
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
        return "LogoutEvent{" +
                "username='" + username + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
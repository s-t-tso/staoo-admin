package com.staoo.common.auth.dto;

import java.io.Serializable;

/**
 * 登录请求DTO
 */
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 登录类型
     */
    private String loginType;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 自定义参数
     */
    private Object customParams;
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getLoginType() {
        return loginType;
    }
    
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public Object getCustomParams() {
        return customParams;
    }
    
    public void setCustomParams(Object customParams) {
        this.customParams = customParams;
    }
}
package com.staoo.system.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 第三方登录请求参数
 * 封装不同第三方登录方式的通用请求参数
 */
public class ThirdPartyLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 登录类型（如"IAM", "OAUTH2"）
     */
    private String loginType;
    
    /**
     * 第三方应用ID
     */
    private String appId;
    
    /**
     * 授权码或token等认证信息
     */
    private String authCode;
    
    /**
     * 回调URL
     */
    private String redirectUri;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 租户编码
     */
    private String tenantCode;
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 设备类型
     */
    private String deviceType;
    
    /**
     * 客户端IP
     */
    private String clientIp;
    
    /**
     * 用户代理信息
     */
    private String userAgent;
    
    /**
     * 其他自定义参数
     */
    private Map<String, Object> customParams;
    
    // Getters and Setters
    public String getLoginType() {
        return loginType;
    }
    
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getAuthCode() {
        return authCode;
    }
    
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getTenantCode() {
        return tenantCode;
    }
    
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public Map<String, Object> getCustomParams() {
        return customParams;
    }
    
    public void setCustomParams(Map<String, Object> customParams) {
        this.customParams = customParams;
    }
}
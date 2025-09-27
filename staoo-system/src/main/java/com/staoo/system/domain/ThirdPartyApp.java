package com.staoo.system.domain;

import java.util.Date;
import java.util.List;

/**
 * 第三方应用实体类
 */
public class ThirdPartyApp {
    private Long id;                  // 应用ID
    private String appName;           // 应用名称
    private String appKey;            // 应用标识
    private String appSecret;         // 应用密钥（加密存储）
    private String appIcon;           // 应用图标
    private String appDomain;         // 应用域名
    private String status;            // 应用状态（0：启用，1：禁用）
    private String remark;            // 备注
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
    private List<String> callbackUrls;// 回调地址列表
    private List<String> permissions; // 权限列表
    private List<Long> tenantIds;     // 可访问的租户ID列表

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getCallbackUrls() {
        return callbackUrls;
    }

    public void setCallbackUrls(List<String> callbackUrls) {
        this.callbackUrls = callbackUrls;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<Long> getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(List<Long> tenantIds) {
        this.tenantIds = tenantIds;
    }
}
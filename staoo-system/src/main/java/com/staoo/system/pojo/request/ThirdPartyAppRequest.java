package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 第三方应用请求对象
 */
public class ThirdPartyAppRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // 应用ID
    private Long id;

    // 应用名称
    @NotBlank(message = "应用名称不能为空")
    @Size(max = 100, message = "应用名称不能超过100个字符")
    private String appName;

    // 应用标识
    private String appKey;
    // 应用密钥（加密存储）
    private String appSecret;

    // 应用图标
    @Size(max = 255, message = "应用图标不能超过255个字符")
    private String appIcon;

    // 应用域名
    @Size(max = 255, message = "应用域名不能超过255个字符")
    private String appDomain;

    // 应用状态（0：启用，1：禁用）
    private String status;

    // 备注
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;

    // 回调地址列表
    private List<String> callbackUrls;
    // 权限列表
    private List<String> permissions;
    // 可访问的租户ID列表
    private List<Long> tenantIds;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirdPartyAppRequest that = (ThirdPartyAppRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(appName, that.appName) && Objects.equals(appKey, that.appKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appName, appKey);
    }

    @Override
    public String toString() {
        return "ThirdPartyAppRequest{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 第三方应用分页查询请求对象
 */
public class ThirdPartyAppQueryRequest extends PageQuery {
    
    private static final long serialVersionUID = 1L;

    private Long id;                  // 应用ID
    
    private String appName;           // 应用名称
    
    private String appKey;            // 应用标识
    
    @Size(max = 255, message = "应用图标不能超过255个字符")
    private String appIcon;           // 应用图标
    
    @Size(max = 255, message = "应用域名不能超过255个字符")
    private String appDomain;         // 应用域名
    
    private String status;            // 应用状态（0：启用，1：禁用）
    
    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;            // 备注
    
    private List<String> callbackUrls;// 回调地址列表
    private List<String> permissions; // 权限列表
    private List<Long> tenantIds;     // 可访问的租户ID列表
    
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
}
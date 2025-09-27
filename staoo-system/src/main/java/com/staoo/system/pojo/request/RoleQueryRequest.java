package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;

/**
 * 角色查询请求类
 * 用于角色管理的分页查询
 */
public class RoleQueryRequest extends PageQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 角色名称
     */
    @Size(max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色状态
     * 0-禁用，1-启用
     */
    private Integer status;

    /**
     * 角色标识
     */
    @Size(max = 100, message = "角色标识长度不能超过100个字符")
    private String roleKey;

    /**
     * 数据权限类型
     * 1-所有数据，2-自定义数据
     */
    private Integer dataScope;
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getRoleKey() {
        return roleKey;
    }
    
    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }
    
    public Integer getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }
}
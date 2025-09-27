package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户-租户关联请求类
 * 用于用户-租户关联相关接口的参数验证和接收
 */
public class UserTenantRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 角色类型
     * 1-创建者 2-管理者 3-普通用户
     */
    private Integer roleType;

    /**
     * 状态
     * 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTenantRequest that = (UserTenantRequest) o;
        return Objects.equals(userId, that.userId) && 
               Objects.equals(tenantId, that.tenantId) && 
               Objects.equals(roleType, that.roleType) && 
               Objects.equals(status, that.status) && 
               Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tenantId, roleType, status, remark);
    }

    @Override
    public String toString() {
        return "UserTenantRequest{" +
               "userId=" + userId +
               ", tenantId=" + tenantId +
               ", roleType=" + roleType +
               ", status=" + status +
               ", remark='" + remark + '\'' +
               '}';
    }
}
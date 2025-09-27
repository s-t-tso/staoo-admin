package com.staoo.system.pojo.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户-租户关联响应类
 * 用于封装用户-租户关联的数据，在API响应中返回给前端
 */
public class UserTenantResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户ID
     */
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
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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
        UserTenantResponse that = (UserTenantResponse) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(tenantId, that.tenantId) &&
               Objects.equals(roleType, that.roleType) &&
               Objects.equals(status, that.status) &&
               Objects.equals(createBy, that.createBy) &&
               Objects.equals(createTime, that.createTime) &&
               Objects.equals(updateBy, that.updateBy) &&
               Objects.equals(updateTime, that.updateTime) &&
               Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, tenantId, roleType, status, createBy, createTime, updateBy, updateTime, remark);
    }

    @Override
    public String toString() {
        return "UserTenantResponse{" +
               "id=" + id +
               ", userId=" + userId +
               ", tenantId=" + tenantId +
               ", roleType=" + roleType +
               ", status=" + status +
               ", createBy='" + createBy + '\'' +
               ", createTime=" + createTime +
               ", updateBy='" + updateBy + '\'' +
               ", updateTime=" + updateTime +
               ", remark='" + remark + '\'' +
               '}';
    }
}
package com.staoo.system.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 * 对应数据库中的sys_role表
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 角色状态
     * 0-禁用，1-启用
     */
    private Integer status;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;

    /**
     * 数据权限类型
     * 1-所有数据，2-自定义数据
     */
    private Integer dataScope;

    /**
     * 数据范围组织ID列表
     */
    private List<Long> dataScopeOrgIds;

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public List<Long> getDataScopeOrgIds() {
        return dataScopeOrgIds;
    }

    public void setDataScopeOrgIds(List<Long> dataScopeOrgIds) {
        this.dataScopeOrgIds = dataScopeOrgIds;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", roleName='" + roleName + '\'' +
                ", status=" + status +
                ", sort=" + sort +
                '}';
    }
}
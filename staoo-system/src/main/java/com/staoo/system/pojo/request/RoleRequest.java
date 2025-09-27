package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色请求类
 * 用于角色新增和更新操作的请求参数
 */
public class RoleRequest {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 30, message = "角色名称长度必须在2-30个字符之间")
    private String roleName;

    /**
     * 角色状态
     * 0-禁用，1-启用
     */
    @NotNull(message = "角色状态不能为空")
    private Integer status;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注信息不能超过200个字符")
    private String remark;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/**
 * 菜单请求类
 * 用于菜单新增和更新操作的请求参数
 */
public class MenuRequest {
    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 2, max = 50, message = "菜单名称长度必须在2-50个字符之间")
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单类型
     * 0-目录，1-菜单，2-按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer menuType;

    /**
     * 菜单图标
     */
    @Size(max = 50, message = "菜单图标不能超过50个字符")
    private String icon;

    /**
     * 菜单路径
     */
    @Size(max = 255, message = "菜单路径不能超过255个字符")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 255, message = "组件路径不能超过255个字符")
    private String component;

    /**
     * 权限标识
     */
    @Size(max = 100, message = "权限标识不能超过100个字符")
    private String perms;

    /**
     * 菜单排序
     */
    private Integer orderNum;

    /**
     * 菜单状态
     * 0-禁用，1-启用
     */
    @NotNull(message = "菜单状态不能为空")
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注信息不能超过200个字符")
    private String remark;

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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
}
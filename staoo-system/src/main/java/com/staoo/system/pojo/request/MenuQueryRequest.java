package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;

/**
 * 菜单查询请求类
 * 用于接收菜单分页查询的参数
 */
public class MenuQueryRequest extends PageQuery {
    private static final long serialVersionUID = 1L;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 菜单名称
     */
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 菜单类型
     * 0-目录，1-菜单，2-按钮
     */
    private Integer menuType;
    
    /**
     * 菜单状态
     * 0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 菜单可见性
     * 0-不可见，1-可见
     */
    private Integer visible;
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getVisible() {
        return visible;
    }
    
    public void setVisible(Integer visible) {
        this.visible = visible;
    }
    
    /**
     * 权限标识
     */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;
}
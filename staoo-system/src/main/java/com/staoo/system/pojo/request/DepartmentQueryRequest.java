package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;

/**
 * 部门查询请求类
 * 用于部门管理的分页查询
 */
public class DepartmentQueryRequest extends PageQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 部门名称
     */
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门状态
     * 0-禁用，1-启用
     */
    private Integer status;

    /**
     * 负责人名称
     */
    @Size(max = 20, message = "负责人名称长度不能超过20个字符")
    private String leaderName;
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getLeaderName() {
        return leaderName;
    }
    
    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
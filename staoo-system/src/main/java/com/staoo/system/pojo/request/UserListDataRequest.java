package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户列表数据请求类
 * 用于获取用户列表数据接口的参数验证和接收
 */
public class UserListDataRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向
     */
    private String sortOrder;

    // getter and setter methods
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
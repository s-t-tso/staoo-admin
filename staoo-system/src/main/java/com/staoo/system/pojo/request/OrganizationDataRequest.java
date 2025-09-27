package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组织架构数据请求类
 * 用于获取组织架构数据接口的参数验证和接收
 */
public class OrganizationDataRequest implements Serializable {
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
     * 搜索关键词
     */
    private String keyword;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
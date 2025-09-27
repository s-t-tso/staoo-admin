package com.staoo.common.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * 通用分页查询参数实体类
 */
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式
     * asc-升序，desc-降序
     */
    private String sortOrder;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 扩展参数
     */
    private Map<String, Object> params;

    // getter and setter methods
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

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 获取分页查询的起始位置
     * @return 起始位置
     */
    public Integer getStartIndex() {
        return (pageNum - 1) * pageSize;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
package com.staoo.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 分页表格数据响应实体类
 * 用于封装分页数据，作为BaseResponse的data字段类型
 */
public class PageDataResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 数据列表
     */
    private List<T> row;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pagesize;

    // 无参构造方法
    public PageDataResponse() {
    }

    // 带参构造方法
    public PageDataResponse(Long total, List<T> row, Integer page, Integer pagesize) {
        this.total = total;
        this.row = row;
        this.page = page;
        this.pagesize = pagesize;
    }

    // getter and setter methods
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRow() {
        return row;
    }

    public void setRow(List<T> row) {
        this.row = row;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    /**
     * 创建分页数据响应
     * @param total 总记录数
     * @param row 数据列表
     * @param page 当前页码
     * @param pagesize 每页条数
     * @param <T> 数据类型
     * @return 分页数据响应对象
     */
    public static <T> PageDataResponse<T> of(Long total, List<T> row, Integer page, Integer pagesize) {
        return new PageDataResponse<>(total, row, page, pagesize);
    }

    @Override
    public String toString() {
        return "PageDataResponse{" +
                "total=" + total +
                ", row=" + row +
                ", page=" + page +
                ", pagesize=" + pagesize +
                '}';
    }
}
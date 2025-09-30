package com.staoo.common.domain;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 表格数据响应实体类
 * 用于封装分页表格数据
 */
public class TableResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pagesize;

    // 无参构造方法
    public TableResult() {
    }

    // 带参构造方法
    public TableResult(Long total, List<T> row) {
        this.total = total;
        this.list = row;
    }

    // 带参构造方法
    public TableResult(Long total, Integer page, Integer pagesize, List<T> row) {
        this.total = total;
        this.page = page;
        this.pagesize = pagesize;
        this.list = row;
    }

    /**
     * 空表格结果
     *
     * @param <T> 数据类型
     * @return 空表格结果
     */
    public static <T> TableResult<T> empty() {
        return new TableResult<>(0L, Collections.emptyList());
    }

    /**
     * 构建表格结果
     *
     * @param total 总记录数
     * @param row   数据列表
     * @param <T>   数据类型
     * @return 表格结果
     */
    public static <T> TableResult<T> build(Long total, List<T> row) {
        return new TableResult<>(total, row);
    }

    /**
     * 构建表格结果
     *
     * @param row 数据列表
     * @param <T> 数据类型
     * @return 表格结果
     */
    public static <T> TableResult<T> build(Page<T> row) {
        return new TableResult<>(row.getTotal(), row.getPageNum(), row.getPageSize(), row.getResult());
    }

    /**
     * 构建表格结果
     *
     * @param total    总记录数
     * @param page     当前页码
     * @param pagesize 每页条数
     * @param row      数据列表
     * @param <T>      数据类型
     * @return 表格结果
     */
    public static <T> TableResult<T> build(Long total, Integer page, Integer pagesize, List<T> row) {
        return new TableResult<>(total, page, pagesize, row);
    }

    // getter and setter methods
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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

    @Override
    public String toString() {
        return "TableResult{" +
                "total=" + total +
                ", list.size()=" + (list != null ? list.size() : 0) +
                ", page=" + page +
                ", pagesize=" + pagesize +
                '}';
    }
}

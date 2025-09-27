package com.staoo.system.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据字典实体类
 * 对应数据库中的sys_dict表
 */
public class Dict implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典描述
     */
    private String remark;

    /**
     * 状态
     * 0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 字典项列表
     */
    private List<DictItem> dictItems;

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

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public List<DictItem> getDictItems() {
        return dictItems;
    }

    public void setDictItems(List<DictItem> dictItems) {
        this.dictItems = dictItems;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", dictType='" + dictType + '\'' +
                ", dictName='" + dictName + '\'' +
                ", status=" + status +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                '}';
    }

    /**
     * 字典项内部类
     */
    public static class DictItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 字典项ID
         */
        private Long id;

        /**
         * 字典ID
         */
        private Long dictId;

        /**
         * 租户ID
         */
        private Long tenantId;

        /**
         * 字典项标签
         */
        private String itemLabel;

        /**
         * 字典项值
         */
        private String itemValue;

        /**
         * 排序
         */
        private Integer sort;

        /**
         * 状态
         * 0-禁用，1-启用
         */
        private Integer status;

        /**
         * 备注
         */
        private String remark;

        /**
         * 创建者
         */
        private Long createBy;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;

        /**
         * 更新者
         */
        private Long updateBy;

        /**
         * 更新时间
         */
        private LocalDateTime updateTime;

        // getter and setter methods
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getDictId() {
            return dictId;
        }

        public void setDictId(Long dictId) {
            this.dictId = dictId;
        }

        public Long getTenantId() {
            return tenantId;
        }

        public void setTenantId(Long tenantId) {
            this.tenantId = tenantId;
        }

        public String getItemLabel() {
            return itemLabel;
        }

        public void setItemLabel(String itemLabel) {
            this.itemLabel = itemLabel;
        }

        public String getItemValue() {
            return itemValue;
        }

        public void setItemValue(String itemValue) {
            this.itemValue = itemValue;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
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

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public Long getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Long createBy) {
            this.createBy = createBy;
        }

        public LocalDateTime getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }

        public Long getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Long updateBy) {
            this.updateBy = updateBy;
        }

        @Override
        public String toString() {
            return "DictItem{" +
                    "id=" + id +
                    ", dictId=" + dictId +
                    ", tenantId=" + tenantId +
                    ", itemLabel='" + itemLabel + '\'' +
                    ", itemValue='" + itemValue + '\'' +
                    ", sort=" + sort +
                    ", status=" + status +
                    ", createBy=" + createBy +
                    ", updateBy=" + updateBy +
                    '}';
        }
    }
}
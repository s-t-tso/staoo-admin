package com.staoo.flow.domain;

import java.util.Date;

/**
 * 表单模板实体类
 * 用于存储表单模板的基本信息和配置
 */
public class FormTemplate {
    // 模板ID
    private Long id;
    // 表单唯一标识
    private String formKey;
    // 表单名称
    private String formName;
    // 描述
    private String description;
    // 表单配置JSON
    private String formConfig;
    // 状态（草稿、已发布）
    private String status;
    // 版本号
    private Integer version;
    // 租户ID
    private Long tenantId;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 创建人ID
    private Long createBy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(String formConfig) {
        this.formConfig = formConfig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
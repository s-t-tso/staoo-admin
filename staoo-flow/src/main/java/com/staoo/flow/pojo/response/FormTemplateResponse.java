package com.staoo.flow.pojo.response;

import java.util.Date;

/**
 * 表单模板响应类
 * 用于返回表单模板相关的响应数据
 */
public class FormTemplateResponse {
    private Long id;                 // 模板ID
    private String formKey;          // 表单唯一标识
    private String formName;         // 表单名称
    private String description;      // 描述
    private String formConfig;       // 表单配置JSON
    private String status;           // 状态（草稿、已发布）
    private String statusDesc;       // 状态描述
    private Integer version;         // 版本号
    private Long tenantId;           // 租户ID
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
    private Long createBy;           // 创建人ID
    private String createByName;     // 创建人姓名
    
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
    
    public String getStatusDesc() {
        return statusDesc;
    }
    
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
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
    
    public String getCreateByName() {
        return createByName;
    }
    
    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
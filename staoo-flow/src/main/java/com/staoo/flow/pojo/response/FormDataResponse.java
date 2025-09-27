package com.staoo.flow.pojo.response;

import java.util.Date;

/**
 * 表单数据响应类
 * 用于返回表单数据相关的响应数据
 */
public class FormDataResponse {
    // 数据ID
    private Long id;
    // 表单标识
    private String formKey;
    // 表单模板ID
    private Long templateId;
    // 表单数据JSON
    private String formData;
    // 数据状态
    private String status;
    // 状态描述
    private String statusDesc;
    // 租户ID
    private Long tenantId;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 创建人ID
    private Long createBy;
    // 创建人名称
    private String creatorName;
    // 关联的流程实例ID
    private String processInstanceId;
    // 业务键
    private String businessKey;
    
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
    
    public Long getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    
    public String getFormData() {
        return formData;
    }
    
    public void setFormData(String formData) {
        this.formData = formData;
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
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getBusinessKey() {
        return businessKey;
    }
    
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
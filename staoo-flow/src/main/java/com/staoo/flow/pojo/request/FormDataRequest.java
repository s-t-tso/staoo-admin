package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 表单数据请求类
 * 用于接收表单数据相关的请求参数
 */
public class FormDataRequest {
    // 数据ID
    private Long id;
    
    @NotBlank(message = "表单标识不能为空")
    @Size(max = 64, message = "表单标识长度不能超过64个字符")
    // 表单标识
    private String formKey;
    
    @NotNull(message = "表单模板ID不能为空")
    // 表单模板ID
    private Long templateId;
    
    @NotBlank(message = "表单数据不能为空")
    // 表单数据JSON
    private String formData;
    
    @Size(max = 10, message = "数据状态长度不能超过10个字符")
    // 数据状态
    private String status;
    
    @NotNull(message = "租户ID不能为空")
    // 租户ID
    private Long tenantId;
    
    // 创建人ID
    private Long createBy;
    // 创建人名称
    private String creatorName;
    // 关联的流程实例ID
    private String processInstanceId;
    
    @Size(max = 64, message = "业务键长度不能超过64个字符")
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
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
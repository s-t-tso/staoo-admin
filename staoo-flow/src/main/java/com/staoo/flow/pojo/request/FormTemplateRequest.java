package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 表单模板请求类
 * 用于接收表单模板相关的请求参数
 */
public class FormTemplateRequest {
    private Long id;                 // 模板ID
    
    @NotBlank(message = "表单标识不能为空")
    @Size(max = 64, message = "表单标识长度不能超过64个字符")
    private String formKey;          // 表单唯一标识
    
    @NotBlank(message = "表单名称不能为空")
    @Size(max = 128, message = "表单名称长度不能超过128个字符")
    private String formName;         // 表单名称
    
    @Size(max = 512, message = "描述长度不能超过512个字符")
    private String description;      // 描述
    
    @NotBlank(message = "表单配置不能为空")
    private String formConfig;       // 表单配置JSON
    
    @Size(max = 10, message = "状态长度不能超过10个字符")
    private String status;           // 状态（草稿、已发布）
    
    private Integer version;         // 版本号
    
    @NotBlank(message = "租户ID不能为空")
    private Long tenantId;           // 租户ID
    
    private Long createBy;           // 创建人ID
    
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
    
    public Long getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
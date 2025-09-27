package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 流程模板请求类
 * 用于接收流程模板相关的请求参数
 */
public class ProcessTemplateRequest {
    // 模板ID
    private Long id;

    @NotBlank(message = "流程标识不能为空")
    @Size(max = 64, message = "流程标识长度不能超过64个字符")
    // 流程唯一标识
    private String processKey;

    @NotBlank(message = "流程名称不能为空")
    @Size(max = 128, message = "流程名称长度不能超过128个字符")
    // 流程名称
    private String processName;

    @Size(max = 512, message = "描述长度不能超过512个字符")
    // 描述
    private String description;

    @NotBlank(message = "BPMN XML定义不能为空")
    // BPMN XML定义
    private String bpmnXml;

    @Size(max = 10, message = "状态长度不能超过10个字符")
    // 状态（草稿、已发布）
    private String status;

    // 版本号
    private Integer version;

    @NotNull(message = "租户ID不能为空")
    // 租户ID
    private Long tenantId;

    @Size(max = 64, message = "流程分类长度不能超过64个字符")
    // 流程分类
    private String category;

    @Size(max = 64, message = "关联的表单标识长度不能超过64个字符")
    // 关联的表单标识
    private String formKey;

    // 创建者
    private Long createBy;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBpmnXml() {
        return bpmnXml;
    }

    public void setBpmnXml(String bpmnXml) {
        this.bpmnXml = bpmnXml;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }
}

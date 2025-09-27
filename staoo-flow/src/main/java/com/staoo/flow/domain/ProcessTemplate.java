package com.staoo.flow.domain;

import java.util.Date;
import com.staoo.common.annotation.AutoFill;

/**
 * 流程模板实体类
 * 用于存储流程模板的基本信息和配置
 */
@AutoFill
public class ProcessTemplate {
    // 模板ID
    private Long id;
    // 流程唯一标识
    private String processKey;
    // 流程名称
    private String processName;
    // 描述
    private String description;
    // BPMN XML定义
    private String bpmnXml;
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
    // 流程分类
    private String category;
    // 关联的表单标识
    private String formKey;

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

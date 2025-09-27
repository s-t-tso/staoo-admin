package com.staoo.flow.pojo.response;

import java.io.Serializable;

/**
 * 启动流程实例响应类
 * 用于封装启动流程实例后的返回数据
 */
public class StartProcessResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 流程实例ID
    private String processInstanceId;
    // 流程定义ID
    private String processDefinitionId;
    // 流程定义Key
    private String processDefinitionKey;
    // 租户ID
    private Long tenantId;
    
    // Getters and Setters
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }
    
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
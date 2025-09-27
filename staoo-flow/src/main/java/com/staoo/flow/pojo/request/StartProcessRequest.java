package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 启动流程实例请求类
 * 用于接收启动流程实例的请求参数
 */
public class StartProcessRequest {
    
    @NotBlank(message = "流程定义Key不能为空")
    @Size(max = 64, message = "流程定义Key长度不能超过64个字符")
    private String processDefinitionKey;  // 流程定义Key
    
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;  // 租户ID
    
    private Map<String, Object> variables;  // 流程变量
    
    // Getters and Setters
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
    
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 流程启动请求类
 * 用于接收流程启动相关的请求参数
 */
public class FlowStartRequest {

    @NotBlank(message = "流程定义Key不能为空")
    @Size(max = 128, message = "流程定义Key长度不能超过128个字符")
    // 流程定义Key
    private String processDefinitionKey;
    
    @NotNull(message = "租户ID不能为空")
    // 租户ID
    private Long tenantId;
    
    // 流程变量
    private Map<String, Object> variables;
    
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
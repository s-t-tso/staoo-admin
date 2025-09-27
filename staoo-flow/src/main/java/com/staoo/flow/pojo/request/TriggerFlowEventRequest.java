package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 触发流程事件请求类
 * 用于接收触发流程事件的请求参数
 */
public class TriggerFlowEventRequest {
    
    @NotBlank(message = "流程实例ID不能为空")
    @Size(max = 64, message = "流程实例ID长度不能超过64个字符")
    private String processInstanceId;  // 流程实例ID
    
    @NotBlank(message = "事件类型不能为空")
    @Size(max = 64, message = "事件类型长度不能超过64个字符")
    private String eventType;  // 事件类型
    
    private Map<String, Object> eventData;  // 事件数据
    
    // Getters and Setters
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public Map<String, Object> getEventData() {
        return eventData;
    }
    
    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }
}
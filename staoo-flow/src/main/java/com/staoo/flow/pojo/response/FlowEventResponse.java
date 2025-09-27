package com.staoo.flow.pojo.response;

import java.io.Serializable;

/**
 * 流程事件响应类
 * 用于返回流程事件触发相关的响应数据
 */
public class FlowEventResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String processInstanceId;  // 流程实例ID
    private String eventType;  // 事件类型
    private String status;  // 处理状态
    private String message;  // 处理消息
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
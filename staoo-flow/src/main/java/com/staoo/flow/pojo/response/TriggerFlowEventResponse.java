package com.staoo.flow.pojo.response;

import java.io.Serializable;

/**
 * 触发流程事件响应类
 * 用于封装触发流程事件后的返回数据
 */
public class TriggerFlowEventResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 流程实例ID
    private String processInstanceId;
    // 事件类型
    private String eventType;
    // 处理状态
    private String status;
    // 处理消息
    private String message;
    
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
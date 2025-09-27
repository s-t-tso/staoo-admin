package com.staoo.flow.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 流程任务记录请求类
 * 用于接收流程任务记录相关的请求参数
 */
public class FlowTaskRecordRequest {
    // 主键ID
    private Long id;
    
    @NotBlank(message = "流程实例ID不能为空")
    @Size(max = 64, message = "流程实例ID长度不能超过64个字符")
    // 流程实例ID
    private String processInstanceId;
    
    @NotBlank(message = "任务ID不能为空")
    @Size(max = 64, message = "任务ID长度不能超过64个字符")
    // 任务ID
    private String taskId;
    
    @Size(max = 128, message = "任务名称长度不能超过128个字符")
    // 任务名称
    private String taskName;
    
    // 处理人ID
    private Long assigneeId;
    
    @Size(max = 128, message = "处理人姓名长度不能超过128个字符")
    // 处理人姓名
    private String assigneeName;
    
    @NotBlank(message = "处理动作不能为空")
    @Size(max = 64, message = "处理动作长度不能超过64个字符")
    // 处理动作：APPROVE-审批通过，REJECT-拒绝，CLAIM-认领，ASSIGN-指派
    private String action;
    
    @Size(max = 512, message = "处理意见长度不能超过512个字符")
    // 处理意见
    private String comment;
    
    @NotNull(message = "租户ID不能为空")
    // 租户ID
    private Long tenantId;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public Long getAssigneeId() {
        return assigneeId;
    }
    
    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
    
    public String getAssigneeName() {
        return assigneeName;
    }
    
    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
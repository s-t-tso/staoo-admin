package com.staoo.flow.pojo.response;

import java.util.Date;

/**
 * 流程任务记录响应类
 * 用于返回流程任务记录相关的响应数据
 */
public class FlowTaskRecordResponse {
    private Long id;            // 主键ID
    private String processInstanceId;  // 流程实例ID
    private String taskId;      // 任务ID
    private String taskName;    // 任务名称
    private Long assigneeId;    // 处理人ID
    private String assigneeName; // 处理人姓名
    private String action;      // 处理动作：APPROVE-审批通过，REJECT-拒绝，CLAIM-认领，ASSIGN-指派
    private String actionDesc;  // 处理动作描述
    private String comment;     // 处理意见
    private Date createTime;    // 创建时间
    private Date completeTime;  // 完成时间
    private Long tenantId;      // 租户ID
    
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
    
    public String getActionDesc() {
        return actionDesc;
    }
    
    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getCompleteTime() {
        return completeTime;
    }
    
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
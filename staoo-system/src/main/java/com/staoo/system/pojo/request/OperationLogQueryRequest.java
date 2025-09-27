package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 操作日志分页查询请求对象
 * 用于操作日志的分页查询参数
 */
public class OperationLogQueryRequest extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 操作模块
     */
    @Size(max = 100, message = "操作模块不能超过100个字符")
    private String module;

    /**
     * 操作类型
     */
    @Size(max = 50, message = "操作类型不能超过50个字符")
    private String operationType;

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 操作人姓名
     */
    @Size(max = 50, message = "操作人姓名不能超过50个字符")
    private String username;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 操作IP
     */
    @Size(max = 50, message = "IP地址不能超过50个字符")
    private String ip;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
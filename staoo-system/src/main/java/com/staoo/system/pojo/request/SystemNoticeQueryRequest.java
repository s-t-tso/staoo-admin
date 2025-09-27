package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 系统通知分页查询请求对象
 * 用于系统通知的分页查询参数
 */
public class SystemNoticeQueryRequest extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 通知标题
     */
    @Size(max = 200, message = "通知标题不能超过200个字符")
    private String title;

    /**
     * 通知类型（1-系统通知，2-业务通知，3-其他通知）
     */
    private Integer type;

    /**
     * 通知级别（1-普通，2-重要，3-紧急）
     */
    private Integer level;

    /**
     * 通知状态（0-未发布，1-已发布）
     */
    private Integer status;

    /**
     * 是否置顶（0-否，1-是）
     */
    private Integer isTop;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
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
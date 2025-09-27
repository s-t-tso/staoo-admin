package com.staoo.system.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.staoo.common.annotation.AutoFill;

/**
 * 系统通知实体类
 * 对应数据库中的sys_notice表
 * 支持批量删除功能
 */
@AutoFill
public class SystemNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 通知ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型
     * 0-系统通知，1-业务通知
     */
    private Integer type;

    /**
     * 通知级别
     * 0-普通，1-重要，2-紧急
     */
    private Integer level;

    /**
     * 通知状态
     * 0-草稿，1-已发布，2-已撤回
     */
    private Integer status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 是否置顶
     * 0-否，1-是
     */
    private Integer isTop;

    /**
     * 阅读状态
     * 0-未读，1-已读
     */
    private Integer readStatus;

    // getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "SystemNotice{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", title='" + title + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(50, content.length())) : "null") + "...'" +
                ", type=" + type +
                ", level=" + level +
                ", status=" + status +
                ", publishTime=" + publishTime +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", isTop=" + isTop +
                ", readStatus=" + readStatus +
                '}';
    }
}
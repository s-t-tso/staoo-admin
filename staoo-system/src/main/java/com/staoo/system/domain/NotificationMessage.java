package com.staoo.system.domain;

import java.util.Date;

/**
 * 通知消息实体类
 * 用于封装数据变更的通知消息内容
 */
public class NotificationMessage {
    // 消息ID，用于幂等性处理
    private String messageId;
    // 应用标识
    private String appKey;
    // 数据类型
    private String dataType;
    // 租户ID
    private Long tenantId;
    // 变更数据
    private Object data;
    // 变更类型（ADD：新增，UPDATE：更新，DELETE：删除）
    private String changeType;
    // 发送时间
    private Date sendTime;
    // 回调地址
    private String callbackUrl;

    // Getter and Setter methods
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
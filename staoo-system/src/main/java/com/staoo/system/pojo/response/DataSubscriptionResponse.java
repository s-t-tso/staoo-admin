package com.staoo.system.pojo.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 数据订阅响应对象
 */
public class DataSubscriptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;                  // 订阅ID
    private String appKey;            // 应用标识
    private String dataType;          // 数据类型（ORG：组织架构，USER：用户，FLOW：流程）
    private String callbackUrl;       // 回调地址
    private String status;            // 状态（0：启用，1：禁用）
    private Date createTime;          // 创建时间
    private Date updateTime;          // 更新时间
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getCallbackUrl() {
        return callbackUrl;
    }
    
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSubscriptionResponse that = (DataSubscriptionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(appKey, that.appKey) && Objects.equals(dataType, that.dataType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, appKey, dataType);
    }
    
    @Override
    public String toString() {
        return "DataSubscriptionResponse{" +
                "id=" + id +
                ", appKey='" + appKey + '\'' +
                ", dataType='" + dataType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
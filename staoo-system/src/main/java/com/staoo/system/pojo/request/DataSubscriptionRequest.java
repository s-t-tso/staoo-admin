package com.staoo.system.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * 数据订阅请求对象
 */
public class DataSubscriptionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // 订阅ID
    private Long id;

    // 应用标识
    @NotBlank(message = "应用标识不能为空")
    @Size(max = 64, message = "应用标识不能超过64个字符")
    private String appKey;

    // 数据类型（ORG：组织架构，USER：用户，FLOW：流程）
    @NotBlank(message = "数据类型不能为空")
    @Size(max = 64, message = "数据类型不能超过64个字符")
    private String dataType;

    // 回调地址
    @NotBlank(message = "回调地址不能为空")
    @Size(max = 512, message = "回调地址不能超过512个字符")
    private String callbackUrl;

    // 状态（0：启用，1：禁用）
    private String status;
    
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSubscriptionRequest that = (DataSubscriptionRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(appKey, that.appKey) && Objects.equals(dataType, that.dataType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, appKey, dataType);
    }
    
    @Override
    public String toString() {
        return "DataSubscriptionRequest{" +
                "id=" + id +
                ", appKey='" + appKey + '\'' +
                ", dataType='" + dataType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
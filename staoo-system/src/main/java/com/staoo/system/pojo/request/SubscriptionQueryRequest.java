package com.staoo.system.pojo.request;

import com.staoo.common.domain.PageQuery;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 数据订阅分页查询请求对象
 * 用于数据订阅的分页查询参数
 */
public class SubscriptionQueryRequest extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 64, message = "应用标识不能超过64个字符")
    private String appKey;            // 应用标识

    @Size(max = 64, message = "数据类型不能超过64个字符")
    private String dataType;          // 数据类型（ORG：组织架构，USER：用户，FLOW：流程）

    private String status;            // 状态（0：启用，1：禁用）

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
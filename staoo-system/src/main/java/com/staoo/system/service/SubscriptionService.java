package com.staoo.system.service;

import com.staoo.system.domain.DataSubscription;

import java.util.List;

/**
 * 数据订阅服务接口
 * 定义数据订阅的业务操作
 */
public interface SubscriptionService {
    /**
     * 订阅数据变更
     * @param appKey 应用标识
     * @param dataType 数据类型
     * @param callbackUrl 回调地址
     * @return 订阅ID
     */
    Long subscribe(String appKey, String dataType, String callbackUrl);

    /**
     * 取消订阅数据变更
     * @param appKey 应用标识
     * @param dataType 数据类型
     * @return 是否取消成功
     */
    boolean unsubscribe(String appKey, String dataType);

    /**
     * 获取应用的所有订阅
     * @param appKey 应用标识
     * @return 订阅列表
     */
    List<DataSubscription> getAppSubscriptions(String appKey);

    /**
     * 获取指定数据类型的所有订阅
     * @param dataType 数据类型
     * @return 订阅列表
     */
    List<DataSubscription> getSubscriptionsByDataType(String dataType);

    /**
     * 启用/禁用订阅
     * @param subscriptionId 订阅ID
     * @param status 状态（0：启用，1：禁用）
     * @return 是否操作成功
     */
    boolean changeSubscriptionStatus(Long subscriptionId, String status);
}
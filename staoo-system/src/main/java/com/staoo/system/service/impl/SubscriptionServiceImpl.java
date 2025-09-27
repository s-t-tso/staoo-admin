package com.staoo.system.service.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.domain.DataSubscription;
import com.staoo.system.mapper.DataSubscriptionMapper;
import com.staoo.system.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

/**
 * 数据订阅服务实现类
 * 实现数据订阅的业务操作
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    private DataSubscriptionMapper dataSubscriptionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long subscribe(String appKey, String dataType, String callbackUrl) {
        // 参数校验
        validateSubscribeParams(appKey, dataType, callbackUrl);

        // 检查是否已存在相同的订阅
        DataSubscription existingSubscription = dataSubscriptionMapper.selectByAppKeyAndDataType(appKey, dataType);
        if (existingSubscription != null) {
            // 如果已存在，则更新回调地址和状态
            existingSubscription.setCallbackUrl(callbackUrl);
            existingSubscription.setStatus("0"); // 启用状态
            existingSubscription.setUpdateTime(new Date());
            dataSubscriptionMapper.update(existingSubscription);
            return existingSubscription.getId();
        }

        // 创建新的订阅
        DataSubscription subscription = new DataSubscription();
        subscription.setAppKey(appKey);
        subscription.setDataType(dataType);
        subscription.setCallbackUrl(callbackUrl);
        subscription.setStatus("0"); // 默认为启用状态
        subscription.setCreateTime(new Date());
        subscription.setUpdateTime(new Date());

        int count = dataSubscriptionMapper.insert(subscription);
        if (count <= 0) {
            throw new BusinessException(StatusCodeEnum.SUBSCRIPTION_CREATE_FAILED);
        }

        logger.info("Subscribe success: appKey={}, dataType={}", appKey, dataType);
        return subscription.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unsubscribe(String appKey, String dataType) {
        // 参数校验
        if (appKey == null || appKey.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "应用标识不能为空");
        }
        if (dataType == null || dataType.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "数据类型不能为空");
        }

        int count = dataSubscriptionMapper.deleteByAppKeyAndDataType(appKey, dataType);
        boolean success = count > 0;

        if (success) {
            logger.info("Unsubscribe success: appKey={}, dataType={}", appKey, dataType);
        } else {
            logger.warn("Unsubscribe failed: subscription not found for appKey={}, dataType={}", appKey, dataType);
        }

        return success;
    }

    @Override
    public List<DataSubscription> getAppSubscriptions(String appKey) {
        if (appKey == null || appKey.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "应用标识不能为空");
        }

        List<DataSubscription> subscriptions = dataSubscriptionMapper.selectByAppKey(appKey);
        return subscriptions != null ? subscriptions : List.of();
    }

    @Override
    public List<DataSubscription> getSubscriptionsByDataType(String dataType) {
        if (dataType == null || dataType.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "数据类型不能为空");
        }

        List<DataSubscription> subscriptions = dataSubscriptionMapper.selectByDataType(dataType);
        return subscriptions != null ? subscriptions : List.of();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeSubscriptionStatus(Long subscriptionId, String status) {
        if (subscriptionId == null || subscriptionId <= 0) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "订阅ID不能为空");
        }
        if (status == null || (!"0".equals(status) && !"1".equals(status))) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "状态值无效");
        }

        // 检查订阅是否存在
        DataSubscription subscription = dataSubscriptionMapper.selectById(subscriptionId);
        if (subscription == null) {
            throw new BusinessException(StatusCodeEnum.SUBSCRIPTION_NOT_FOUND);
        }

        int count = dataSubscriptionMapper.updateStatus(subscriptionId, status);
        boolean success = count > 0;

        if (success) {
            logger.info("Change subscription status success: id={}, status={}", subscriptionId, status);
        } else {
            logger.warn("Change subscription status failed: id={}", subscriptionId);
        }

        return success;
    }

    /**
     * 验证订阅参数
     */
    private void validateSubscribeParams(String appKey, String dataType, String callbackUrl) {
        if (appKey == null || appKey.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "应用标识不能为空");
        }
        if (dataType == null || dataType.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "数据类型不能为空");
        }
        if (callbackUrl == null || callbackUrl.isEmpty()) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "回调地址不能为空");
        }
        // 简单验证URL格式
        if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "回调地址格式无效");
        }
    }
}
package com.staoo.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.staoo.common.domain.TableResult;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.domain.DataSubscription;
import com.staoo.system.pojo.request.SubscriptionQueryRequest;
import com.staoo.system.mapper.DataSubscriptionMapper;
import com.staoo.system.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        List<DataSubscription> subscriptions = dataSubscriptionMapper.selectByAppKey(appKey);
        return subscriptions != null ? subscriptions : List.of();
    }

    @Override
    public List<DataSubscription> getSubscriptionsByDataType(String dataType) {
        List<DataSubscription> subscriptions = dataSubscriptionMapper.selectByDataType(dataType);
        return subscriptions != null ? subscriptions : List.of();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeSubscriptionStatus(Long subscriptionId, String status) {
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

    
    @Override
    public List<DataSubscription> getList(SubscriptionQueryRequest request) {
        try {
            // 参数校验已移至Request层
            
            // 调用Mapper层方法查询列表
            return dataSubscriptionMapper.selectListByRequest(request);
        } catch (Exception e) {
            logger.error("查询订阅列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }
    
    @Override
    public TableResult<DataSubscription> getPage(SubscriptionQueryRequest request) {
        try {
            // 设置分页参数
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            
            // 调用新的getList方法获取数据
            List<DataSubscription> list = getList(request);
            Page<DataSubscription> pageList = (Page<DataSubscription>) list;
            
            // 构建分页结果
            return TableResult.build(pageList.getTotal(), request.getPageNum(), request.getPageSize(), list);
        } catch (Exception e) {
            logger.error("分页查询订阅列表失败", e);
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR);
        }
    }
    

}
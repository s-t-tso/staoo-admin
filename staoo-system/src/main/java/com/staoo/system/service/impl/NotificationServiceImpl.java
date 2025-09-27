package com.staoo.system.service.impl;

import com.staoo.system.domain.DataSubscription;
import com.staoo.system.domain.NotificationMessage;
import com.staoo.system.service.NotificationService;
import com.staoo.system.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 通知服务实现类
 * 实现数据变更通知的业务操作
 * 注意：由于Maven依赖问题，当前版本临时移除了RabbitMQ相关代码
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    @Autowired
    private SubscriptionService subscriptionService;

    // 交换机名称
    //private static final String DATA_CHANGE_EXCHANGE = "data-change-exchange";
    // 流程变更队列名称
    //private static final String FLOW_CHANGE_QUEUE = "flow-change-queue";

    @Override
    public void sendDataChangeNotification(String dataType, Long tenantId, Object data, String changeType) {
        // 参数校验
        if (dataType == null || dataType.isEmpty()) {
            logger.error("Failed to send notification: dataType is empty");
            return;
        }
        if (tenantId == null) {
            logger.error("Failed to send notification: tenantId is null");
            return;
        }
        if (changeType == null || changeType.isEmpty()) {
            logger.error("Failed to send notification: changeType is empty");
            return;
        }

        try {
            // 构建通知消息
            NotificationMessage message = new NotificationMessage();
            message.setMessageId(UUID.randomUUID().toString());
            message.setDataType(dataType);
            message.setTenantId(tenantId);
            message.setData(data);
            message.setChangeType(changeType);
            message.setSendTime(new Date());

            // 获取订阅该数据类型的应用
            List<DataSubscription> subscriptions = subscriptionService.getSubscriptionsByDataType(dataType);

            if (CollectionUtils.isEmpty(subscriptions)) {
                logger.info("No subscriptions found for dataType: {}", dataType);
                return;
            }

            // 发送通知到消息队列
            for (DataSubscription subscription : subscriptions) {
                if ("0".equals(subscription.getStatus())) { // 只发送给启用的订阅
                    message.setAppKey(subscription.getAppKey());
                    message.setCallbackUrl(subscription.getCallbackUrl());
                    
                    // 发送消息到消息队列
                    // 临时注释掉RabbitMQ相关代码，因为依赖问题
                    // rabbitTemplate.convertAndSend(DATA_CHANGE_EXCHANGE, 
                    //                             subscription.getAppKey(), 
                    //                             message);
                    
                    logger.info("Data change notification would be sent: messageId={}, appKey={}, dataType={}", 
                               message.getMessageId(), subscription.getAppKey(), dataType);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to send data change notification: dataType={}", dataType, e);
        }
    }

    @Override
    public void sendFlowChangeNotification(Long flowInstanceId, String status, Object result) {
        // 参数校验
        if (flowInstanceId == null) {
            logger.error("Failed to send flow notification: flowInstanceId is null");
            return;
        }
        if (status == null || status.isEmpty()) {
            logger.error("Failed to send flow notification: status is empty");
            return;
        }

        try {
            // 构建流程变更通知消息
            NotificationMessage message = new NotificationMessage();
            message.setMessageId(UUID.randomUUID().toString());
            message.setDataType("FLOW");
            message.setData(result);
            message.setChangeType(status);
            message.setSendTime(new Date());
            message.setAppKey("flow-system"); // 流程系统自身

            // 发送消息到流程变更队列
            // 临时注释掉RabbitMQ相关代码，因为依赖问题
            // rabbitTemplate.convertAndSend(FLOW_CHANGE_QUEUE, message);

            logger.info("Flow change notification would be sent: messageId={}, flowInstanceId={}, status={}", 
                       message.getMessageId(), flowInstanceId, status);
        } catch (Exception e) {
            logger.error("Failed to send flow change notification: flowInstanceId={}", flowInstanceId, e);
        }
    }

    @Override
    public void handleNotificationResult(String messageId, boolean success, String errorMessage) {
        if (messageId == null || messageId.isEmpty()) {
            logger.error("Failed to handle notification result: messageId is empty");
            return;
        }

        try {
            if (success) {
                logger.info("Notification handled successfully: messageId={}", messageId);
            } else {
                logger.error("Notification handling failed: messageId={}, error={}", messageId, errorMessage);
                // 这里可以添加失败处理逻辑，如记录到失败日志表等
            }
        } catch (Exception e) {
            logger.error("Failed to handle notification result: messageId={}", messageId, e);
        }
    }
}
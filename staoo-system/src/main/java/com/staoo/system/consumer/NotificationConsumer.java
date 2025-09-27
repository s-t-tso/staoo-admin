package com.staoo.system.consumer;

import com.staoo.system.domain.NotificationMessage;
import com.staoo.system.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通知消息消费者
 * 负责监听消息队列，处理数据变更通知
 * 注意：由于Maven依赖问题，当前版本临时移除了RabbitMQ相关代码
 */
@Component
public class NotificationConsumer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    // 用于幂等处理的消息ID缓存
    private static final Map<String, Boolean> PROCESSED_MESSAGES = new ConcurrentHashMap<>();
    // 用于重试计数的映射
    private static final Map<String, AtomicInteger> RETRY_COUNTS = new ConcurrentHashMap<>();
    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 处理流程变更通知
     * 注意：临时移除了RabbitMQ监听注解
     * @param message 通知消息
     */
    //@RabbitListener(queues = "flow-change-queue")
    public void handleFlowChangeNotification(NotificationMessage message) {
        if (message == null) {
            logger.error("Received null flow change notification message");
            return;
        }

        logger.info("Received flow change notification: messageId={}, flowInstanceId={}, status={}",
                message.getMessageId(), message.getData() != null ? message.getData() : "null", message.getChangeType());

        try {
            // 这里可以添加流程变更的处理逻辑
            // 例如更新流程状态、触发相关业务操作等
            
            // 处理完成后记录结果
            notificationService.handleNotificationResult(message.getMessageId(), true, null);
        } catch (Exception e) {
            logger.error("Failed to handle flow change notification: messageId={}", message.getMessageId(), e);
            notificationService.handleNotificationResult(message.getMessageId(), false, e.getMessage());
        }
    }

    /**
     * 处理数据变更通知
     * 这个方法会被动态创建的队列监听器调用
     * @param message 通知消息
     */
    public void handleDataChangeNotification(NotificationMessage message) {
        if (message == null) {
            logger.error("Received null data change notification message");
            return;
        }

        logger.info("Received data change notification: messageId={}, dataType={}, appKey={}",
                message.getMessageId(), message.getDataType(), message.getAppKey());

        // 幂等处理：检查消息是否已经处理过
        if (isMessageProcessed(message.getMessageId())) {
            logger.info("Message already processed, skipping: messageId={}", message.getMessageId());
            return;
        }

        // 获取重试计数
        AtomicInteger retryCount = getRetryCount(message.getMessageId());

        try {
            // 执行回调通知
            boolean success = executeCallback(message);

            if (success) {
                // 标记消息为已处理
                markMessageAsProcessed(message.getMessageId());
                // 清理重试计数
                clearRetryCount(message.getMessageId());
                // 记录处理结果
                notificationService.handleNotificationResult(message.getMessageId(), true, null);
            } else {
                handleRetry(message, retryCount);
            }
        } catch (Exception e) {
            logger.error("Error processing data change notification: messageId={}", message.getMessageId(), e);
            handleRetry(message, retryCount);
        }
    }

    /**
     * 执行回调通知
     * @param message 通知消息
     * @return 执行结果
     */
    private boolean executeCallback(NotificationMessage message) {
        if (message.getCallbackUrl() == null || message.getCallbackUrl().isEmpty()) {
            logger.error("Callback URL is empty: messageId={}", message.getMessageId());
            return false;
        }

        try {
            logger.info("Executing callback to: {}", message.getCallbackUrl());
            
            // 发送POST请求到回调URL
            Map<String, Object> response = restTemplate.postForObject(
                    message.getCallbackUrl(),
                    message,
                    Map.class
            );

            // 检查响应是否成功
            if (response != null && "success".equals(response.get("code"))) {
                logger.info("Callback executed successfully: messageId={}", message.getMessageId());
                return true;
            } else {
                logger.warn("Callback returned non-success response: messageId={}, response={}", 
                        message.getMessageId(), response);
                return false;
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP client error during callback: messageId={}, statusCode={}", 
                    message.getMessageId(), e.getStatusCode());
            return false;
        } catch (HttpServerErrorException e) {
            logger.error("HTTP server error during callback: messageId={}, statusCode={}", 
                    message.getMessageId(), e.getStatusCode());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during callback: messageId={}", message.getMessageId(), e);
            return false;
        }
    }

    /**
     * 处理重试逻辑
     * @param message 通知消息
     * @param retryCount 当前重试计数
     */
    private void handleRetry(NotificationMessage message, AtomicInteger retryCount) {
        int currentRetry = retryCount.incrementAndGet();
        
        if (currentRetry <= MAX_RETRY_COUNT) {
            logger.warn("Retry notification: messageId={}, attempt={}/{}",
                    message.getMessageId(), currentRetry, MAX_RETRY_COUNT);
            // 这里可以添加延迟重试逻辑
            // 例如使用RabbitMQ的死信队列和TTL实现延迟重试
            // 暂时直接返回false，让消息进入死信队列
        } else {
            logger.error("Max retry attempts reached: messageId={}", message.getMessageId());
            notificationService.handleNotificationResult(message.getMessageId(), false, "Max retry attempts reached");
            // 清理重试计数
            clearRetryCount(message.getMessageId());
        }
    }

    /**
     * 检查消息是否已经处理过
     * @param messageId 消息ID
     * @return 是否已处理
     */
    private boolean isMessageProcessed(String messageId) {
        return PROCESSED_MESSAGES.containsKey(messageId);
    }

    /**
     * 标记消息为已处理
     * @param messageId 消息ID
     */
    private void markMessageAsProcessed(String messageId) {
        PROCESSED_MESSAGES.put(messageId, Boolean.TRUE);
    }

    /**
     * 获取消息的重试计数
     * @param messageId 消息ID
     * @return 重试计数器
     */
    private AtomicInteger getRetryCount(String messageId) {
        return RETRY_COUNTS.computeIfAbsent(messageId, k -> new AtomicInteger(0));
    }

    /**
     * 清理消息的重试计数
     * @param messageId 消息ID
     */
    private void clearRetryCount(String messageId) {
        RETRY_COUNTS.remove(messageId);
    }
}
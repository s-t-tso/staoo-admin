package com.staoo.system.service;

/**
 * 通知服务接口
 * 定义数据变更通知的业务操作
 */
public interface NotificationService {
    /**
     * 发送数据变更通知
     * @param dataType 数据类型
     * @param tenantId 租户ID
     * @param data 变更数据
     * @param changeType 变更类型
     */
    void sendDataChangeNotification(String dataType, Long tenantId, Object data, String changeType);

    /**
     * 发送流程变更通知
     * @param flowInstanceId 流程实例ID
     * @param status 流程状态
     * @param result 流程结果
     */
    void sendFlowChangeNotification(Long flowInstanceId, String status, Object result);

    /**
     * 处理通知结果
     * @param messageId 消息ID
     * @param success 是否成功
     * @param errorMessage 错误信息
     */
    void handleNotificationResult(String messageId, boolean success, String errorMessage);
}
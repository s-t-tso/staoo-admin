package com.staoo.system.listener;

import com.staoo.system.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 数据变更监听器
 * 监听系统中的数据变更事件并触发通知
 */
@Component
public class DataChangeListener implements ApplicationListener<DataChangeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DataChangeListener.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * 处理数据变更事件
     * @param event 数据变更事件
     */
    @Override
    public void onApplicationEvent(DataChangeEvent event) {
        if (event == null) {
            logger.error("Received null data change event");
            return;
        }

        try {
            // 从事件中获取数据变更信息
            String dataType = event.getDataType();
            Long tenantId = event.getTenantId();
            Object data = event.getData();
            String changeType = event.getChangeType();

            // 校验必要参数
            if (dataType == null || dataType.isEmpty()) {
                logger.error("Data type is empty in event");
                return;
            }
            if (tenantId == null) {
                logger.error("Tenant ID is null in event");
                return;
            }
            if (changeType == null || changeType.isEmpty()) {
                logger.error("Change type is empty in event");
                return;
            }

            logger.info("Received data change event: dataType={}, tenantId={}, changeType={}",
                    dataType, tenantId, changeType);

            // 调用通知服务发送数据变更通知
            notificationService.sendDataChangeNotification(dataType, tenantId, data, changeType);
        } catch (Exception e) {
            logger.error("Failed to handle data change event", e);
        }
    }
}

/**
 * 数据变更事件类
 * 用于封装数据变更的相关信息
 */
class DataChangeEvent extends org.springframework.context.ApplicationEvent {
    private String dataType;
    private Long tenantId;
    private Object data;
    private String changeType;

    public DataChangeEvent(Object source, String dataType, Long tenantId, Object data, String changeType) {
        super(source);
        this.dataType = dataType;
        this.tenantId = tenantId;
        this.data = data;
        this.changeType = changeType;
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
}
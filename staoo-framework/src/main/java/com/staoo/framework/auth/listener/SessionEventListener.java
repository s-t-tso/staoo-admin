package com.staoo.framework.auth.listener;

import com.staoo.framework.auth.event.SessionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 会话事件监听器
 * 处理会话相关的事件，如会话过期等
 */
@Component
public class SessionEventListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionEventListener.class);

    /**
     * 处理会话事件
     * @param event 会话事件
     */
    @EventListener
    public void handleSessionEvent(SessionEvent event) {
        switch (event.getEventType()) {
            case SESSION_CREATED:
                handleSessionCreated(event);
                break;
            case SESSION_EXPIRED:
                handleSessionExpired(event);
                break;
            case SESSION_INVALIDATED:
                handleSessionInvalidated(event);
                break;
            case SESSION_RENEWED:
                handleSessionRenewed(event);
                break;
            default:
                logger.warn("未知的会话事件类型: {}", event.getEventType());
        }
    }

    /**
     * 处理会话创建事件
     * @param event 会话事件
     */
    private void handleSessionCreated(SessionEvent event) {
        logger.info("会话创建: {}", event);
        
        // TODO: 可以在这里添加会话创建后的处理逻辑
        // 如：记录会话信息、初始化会话数据等
    }

    /**
     * 处理会话过期事件
     * @param event 会话事件
     */
    private void handleSessionExpired(SessionEvent event) {
        logger.info("会话过期: {}", event);
        
        // TODO: 可以在这里添加会话过期后的处理逻辑
        // 如：清理会话资源、通知用户等
    }

    /**
     * 处理会话无效化事件
     * @param event 会话事件
     */
    private void handleSessionInvalidated(SessionEvent event) {
        logger.info("会话无效化: {}", event);
        
        // TODO: 可以在这里添加会话无效化后的处理逻辑
        // 如：清理会话资源、记录日志等
    }

    /**
     * 处理会话续期事件
     * @param event 会话事件
     */
    private void handleSessionRenewed(SessionEvent event) {
        logger.info("会话续期: {}", event);
        
        // TODO: 可以在这里添加会话续期后的处理逻辑
        // 如：更新会话信息、记录续期日志等
    }
}
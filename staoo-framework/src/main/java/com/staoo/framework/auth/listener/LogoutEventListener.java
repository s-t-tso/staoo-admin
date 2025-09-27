package com.staoo.framework.auth.listener;

import com.staoo.common.util.UserUtils;
import com.staoo.framework.auth.event.LogoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 登出事件监听器
 * 处理登出相关的事件，如清理会话等
 */
@Component
public class LogoutEventListener {
    private static final Logger logger = LoggerFactory.getLogger(LogoutEventListener.class);

    /**
     * 处理登出事件
     * @param event 登出事件
     */
    @EventListener
    public void handleLogoutEvent(LogoutEvent event) {
        // 记录登出日志
        logger.info("用户登出: {}", event);
        
        // 清理当前用户信息，避免ThreadLocal内存泄漏
        UserUtils.clear();
        logger.debug("已清理用户[{}]的ThreadLocal信息", event.getUsername());
        
        // TODO: 可以在这里添加登出后的处理逻辑
        // 如：清理用户会话信息、缓存等
        // 更新用户最后活跃时间等
    }
}
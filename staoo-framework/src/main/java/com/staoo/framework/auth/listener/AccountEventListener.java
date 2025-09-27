package com.staoo.framework.auth.listener;

import com.staoo.framework.auth.event.AccountEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 账号事件监听器
 * 处理账号相关的事件，如账号锁定、解锁、密码修改等
 */
@Component
public class AccountEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AccountEventListener.class);

    /**
     * 处理账号事件
     * @param event 账号事件
     */
    @EventListener
    public void handleAccountEvent(AccountEvent event) {
        switch (event.getEventType()) {
            case ACCOUNT_LOCKED:
                handleAccountLocked(event);
                break;
            case ACCOUNT_UNLOCKED:
                handleAccountUnlocked(event);
                break;
            case PASSWORD_CHANGED:
                handlePasswordChanged(event);
                break;
            case PROFILE_UPDATED:
                handleProfileUpdated(event);
                break;
            case ACCOUNT_CREATED:
                handleAccountCreated(event);
                break;
            case ACCOUNT_DELETED:
                handleAccountDeleted(event);
                break;
            case ACCOUNT_ENABLED:
                handleAccountEnabled(event);
                break;
            case ACCOUNT_DISABLED:
                handleAccountDisabled(event);
                break;
            default:
                logger.warn("未知的账号事件类型: {}", event.getEventType());
        }
    }

    /**
     * 处理账号锁定事件
     * @param event 账号事件
     */
    private void handleAccountLocked(AccountEvent event) {
        logger.info("账号锁定: {}", event);
        
        // TODO: 可以在这里添加账号锁定后的处理逻辑
        // 如：清理用户会话、发送锁定通知等
    }

    /**
     * 处理账号解锁事件
     * @param event 账号事件
     */
    private void handleAccountUnlocked(AccountEvent event) {
        logger.info("账号解锁: {}", event);
        
        // TODO: 可以在这里添加账号解锁后的处理逻辑
        // 如：发送解锁通知等
    }

    /**
     * 处理密码修改事件
     * @param event 账号事件
     */
    private void handlePasswordChanged(AccountEvent event) {
        logger.info("密码修改: {}", event);
        
        // TODO: 可以在这里添加密码修改后的处理逻辑
        // 如：清理旧会话、发送密码修改通知等
    }

    /**
     * 处理资料更新事件
     * @param event 账号事件
     */
    private void handleProfileUpdated(AccountEvent event) {
        logger.info("资料更新: {}", event);
        
        // TODO: 可以在这里添加资料更新后的处理逻辑
        // 如：更新缓存中的用户信息等
    }

    /**
     * 处理账号创建事件
     * @param event 账号事件
     */
    private void handleAccountCreated(AccountEvent event) {
        logger.info("账号创建: {}", event);
        
        // TODO: 可以在这里添加账号创建后的处理逻辑
        // 如：初始化用户权限、发送欢迎通知等
    }

    /**
     * 处理账号删除事件
     * @param event 账号事件
     */
    private void handleAccountDeleted(AccountEvent event) {
        logger.info("账号删除: {}", event);
        
        // TODO: 可以在这里添加账号删除后的处理逻辑
        // 如：清理用户数据、回收资源等
    }

    /**
     * 处理账号启用事件
     * @param event 账号事件
     */
    private void handleAccountEnabled(AccountEvent event) {
        logger.info("账号启用: {}", event);
        
        // TODO: 可以在这里添加账号启用后的处理逻辑
        // 如：发送启用通知等
    }

    /**
     * 处理账号禁用事件
     * @param event 账号事件
     */
    private void handleAccountDisabled(AccountEvent event) {
        logger.info("账号禁用: {}", event);
        
        // TODO: 可以在这里添加账号禁用后的处理逻辑
        // 如：清理用户会话、发送禁用通知等
    }
}
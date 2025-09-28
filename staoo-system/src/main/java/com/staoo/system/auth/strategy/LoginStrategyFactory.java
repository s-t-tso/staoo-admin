package com.staoo.system.auth.strategy;

import com.staoo.common.exception.BusinessException;
import com.staoo.common.enums.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录策略工厂类
 * 根据登录类型创建或获取对应的登录策略实例
 */
@Component
public class LoginStrategyFactory {
    
    // 存储所有登录策略实现
    private final Map<String, LoginStrategy> loginStrategyMap = new ConcurrentHashMap<>();
    
    /**
     * 构造函数，自动注入所有LoginStrategy实现
     * @param strategies 所有登录策略实现
     */
    @Autowired
    public LoginStrategyFactory(Map<String, LoginStrategy> strategies) {
        // 将所有登录策略实现放入map中，key为策略实现的登录类型
        strategies.forEach((beanName, strategy) -> {
            loginStrategyMap.put(strategy.getLoginType(), strategy);
        });
    }
    
    /**
     * 根据登录类型获取对应的登录策略
     * @param loginType 登录类型
     * @return 登录策略实例
     * @throws BusinessException 当不存在指定的登录类型时抛出异常
     */
    public LoginStrategy getStrategy(String loginType) {
        LoginStrategy strategy = loginStrategyMap.get(loginType);
        if (strategy == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, 
                "不支持的登录类型: " + loginType);
        }
        return strategy;
    }
    
    /**
     * 检查是否支持指定的登录类型
     * @param loginType 登录类型
     * @return 是否支持
     */
    public boolean supports(String loginType) {
        return loginStrategyMap.containsKey(loginType);
    }
}
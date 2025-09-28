package com.staoo.system.auth.factory;

import com.staoo.common.exception.BusinessException;
import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.system.auth.service.ThirdPartyLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 第三方登录工厂类
 * 根据登录类型创建相应的登录服务实例
 */
@Component
public class ThirdPartyLoginFactory {
    
    // 存储所有第三方登录服务实现
    private final Map<String, ThirdPartyLoginService> loginServiceMap = new ConcurrentHashMap<>();
    
    /**
     * 构造函数，自动注入所有ThirdPartyLoginService实现
     * @param loginServices 所有第三方登录服务实现
     */
    @Autowired
    public ThirdPartyLoginFactory(Map<String, ThirdPartyLoginService> loginServices) {
        // 将所有登录服务实现放入map中，key为服务实现的bean名称
        loginServices.forEach((beanName, service) -> {
            // 使用服务实现提供的登录类型作为map的key
            loginServiceMap.put(service.getLoginType(), service);
        });
    }
    
    /**
     * 根据登录类型获取对应的登录服务
     * @param loginType 登录类型
     * @return 第三方登录服务实例
     */
    public ThirdPartyLoginService getLoginService(String loginType) {
        ThirdPartyLoginService loginService = loginServiceMap.get(loginType);
        if (loginService == null) {
            throw new BusinessException(StatusCodeEnum.BUSINESS_ERROR, 
                "不支持的登录类型: " + loginType);
        }
        return loginService;
    }
}
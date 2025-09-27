package com.staoo.system.validator.thirdparty;

import com.staoo.system.validator.ValidationStrategy;
import com.staoo.system.service.ThirdPartyAppService;
import com.staoo.system.domain.ThirdPartyApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用名称唯一性验证策略
 * 验证第三方应用名称是否唯一
 */
@Component
public class AppNameUniqueValidator implements ValidationStrategy<String> {

    private static final String ERROR_MESSAGE = "应用名称已存在，请使用其他名称";
    
    @Autowired
    private ThirdPartyAppService thirdPartyAppService;
    
    @Override
    public boolean validate(String appName) {
        if (appName == null || appName.trim().isEmpty()) {
            return true; // 名称为空由@NotBlank处理
        }
        
        // 检查应用名称是否已存在
        ThirdPartyApp queryApp = new ThirdPartyApp();
        queryApp.setAppName(appName);
        List<ThirdPartyApp> list = thirdPartyAppService.getList(queryApp);
        return list == null || list.isEmpty();
    }
    
    @Override
    public String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
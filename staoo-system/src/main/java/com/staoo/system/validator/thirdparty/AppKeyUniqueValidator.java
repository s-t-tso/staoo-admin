package com.staoo.system.validator.thirdparty;

import com.staoo.system.validator.ValidationStrategy;
import com.staoo.system.service.ThirdPartyAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 应用密钥唯一性验证策略
 * 验证第三方应用密钥是否唯一
 */
@Component
public class AppKeyUniqueValidator implements ValidationStrategy<String> {

    private static final String ERROR_MESSAGE = "应用密钥已存在，请使用其他密钥";
    
    @Autowired
    private ThirdPartyAppService thirdPartyAppService;
    
    @Override
    public boolean validate(String appKey) {
        if (appKey == null || appKey.trim().isEmpty()) {
            return true; // 密钥为空由@NotBlank处理
        }
        
        // 检查应用密钥是否已存在
        return thirdPartyAppService.getByAppKey(appKey) == null;
    }
    
    @Override
    public String getErrorMessage() {
        return ERROR_MESSAGE;
    }
}
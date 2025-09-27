package com.staoo.system.service.impl;

import com.staoo.common.enums.StatusCodeEnum;
import com.staoo.common.exception.BusinessException;
import com.staoo.system.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 短信服务实现类
 * 实现短信发送相关的业务操作
 * 注意：当前为模拟实现，实际项目中需要集成真实的短信服务API
 */
@Service
public class SmsServiceImpl implements SmsService {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    
    // 短信发送频率限制（分钟）
    private static final int SMS_SEND_INTERVAL = 1;
    
    // 默认验证码长度
    private static final int DEFAULT_CODE_LENGTH = 6;
    
    // 存储短信发送记录的Map，用于限制发送频率
    private final ConcurrentHashMap<String, Long> smsSendRecordMap = new ConcurrentHashMap<>();
    
    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        // 验证手机号码格式
        if (!validatePhoneNumber(phoneNumber)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码格式不正确");
        }
        
        // 验证验证码
        if (!StringUtils.hasText(code)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "验证码不能为空");
        }
        
        // 检查发送频率
        if (!checkSendFrequency(phoneNumber)) {
            // 频率检查失败，这里已经在checkSendFrequency方法中抛出异常
            return false;
        }
        
        // 模拟发送验证码短信
        String message = String.format("您的验证码是：%s，有效期5分钟，请勿泄露给他人。", code);
        return sendSms(phoneNumber, message);
    }
    
    @Override
    public boolean sendNotification(String phoneNumber, String content) {
        // 验证手机号码格式
        if (!validatePhoneNumber(phoneNumber)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码格式不正确");
        }
        
        // 验证短信内容
        if (!StringUtils.hasText(content)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "短信内容不能为空");
        }
        
        // 检查发送频率
        if (!checkSendFrequency(phoneNumber)) {
            // 频率检查失败，这里已经在checkSendFrequency方法中抛出异常
            return false;
        }
        
        // 模拟发送通知短信
        return sendSms(phoneNumber, content);
    }
    
    @Override
    public boolean checkSendFrequency(String phoneNumber) {
        // 验证手机号码格式
        if (!validatePhoneNumber(phoneNumber)) {
            throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, "手机号码格式不正确");
        }
        
        Long lastSendTime = smsSendRecordMap.get(phoneNumber);
        if (lastSendTime != null) {
            long interval = System.currentTimeMillis() - lastSendTime;
            if (interval < TimeUnit.MINUTES.toMillis(SMS_SEND_INTERVAL)) {
                long remainingTime = (TimeUnit.MINUTES.toMillis(SMS_SEND_INTERVAL) - interval) / 1000;
                throw new BusinessException(StatusCodeEnum.PARAM_VALIDATION_ERROR, 
                    "短信发送过于频繁，请" + remainingTime + "秒后再试");
            }
        }
        
        return true;
    }
    
    @Override
    public String generateVerificationCode(int length) {
        // 确保长度在合理范围内
        if (length <= 0) {
            length = DEFAULT_CODE_LENGTH;
        }
        
        // 生成指定长度的数字验证码
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    /**
     * 验证手机号码格式
     * @param phoneNumber 手机号码
     * @return 是否格式正确
     */
    private boolean validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return false;
        }
        
        // 中国大陆手机号格式验证
        return phoneNumber.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 发送短信的实际实现
     * 注意：当前为模拟实现，实际项目中需要调用真实的短信服务API
     * @param phoneNumber 手机号码
     * @param content 短信内容
     * @return 是否发送成功
     */
    private boolean sendSms(String phoneNumber, String content) {
        try {
            // 记录发送时间
            smsSendRecordMap.put(phoneNumber, System.currentTimeMillis());
            
            // 模拟发送短信
            logger.info("向手机号[{}]发送短信: {}", phoneNumber, content);
            
            // 在实际项目中，这里应该调用真实的短信服务API发送短信
            // 例如：
            // return smsProvider.send(phoneNumber, content);
            
            return true;
        } catch (Exception e) {
            logger.error("发送短信失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
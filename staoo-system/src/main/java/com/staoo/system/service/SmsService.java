package com.staoo.system.service;

/**
 * 短信服务接口
 * 定义短信发送相关的业务操作
 */
public interface SmsService {
    
    /**
     * 发送验证码短信
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 是否发送成功
     */
    boolean sendVerificationCode(String phoneNumber, String code);
    
    /**
     * 发送通知短信
     * @param phoneNumber 手机号码
     * @param content 短信内容
     * @return 是否发送成功
     */
    boolean sendNotification(String phoneNumber, String content);
    
    /**
     * 检查短信发送频率
     * @param phoneNumber 手机号码
     * @return 是否可以发送
     */
    boolean checkSendFrequency(String phoneNumber);
    
    /**
     * 生成短信验证码
     * @param length 验证码长度
     * @return 生成的验证码
     */
    String generateVerificationCode(int length);
}
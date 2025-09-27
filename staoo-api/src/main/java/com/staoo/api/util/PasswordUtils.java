package com.staoo.api.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 密码工具类
 * 提供密码生成、验证等功能
 * @author staoo
 */
@Component
public class PasswordUtils {
    // 默认密码长度
    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    // 字符集
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
    
    // 安全随机数生成器
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * 生成默认密码
     * 密码包含大小写字母、数字和特殊字符，长度为12位
     * @return 生成的默认密码
     */
    public static String generateDefaultPassword() {
        return generatePassword(DEFAULT_PASSWORD_LENGTH);
    }
    
    /**
     * 生成指定长度的随机密码
     * 密码包含大小写字母、数字和特殊字符
     * @param length 密码长度
     * @return 生成的随机密码
     */
    public static String generatePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("密码长度不能少于8个字符");
        }
        
        // 确保密码包含至少一个小写字母、一个大写字母、一个数字和一个特殊字符
        StringBuilder password = new StringBuilder(length);
        password.append(LOWERCASE_CHARS.charAt(RANDOM.nextInt(LOWERCASE_CHARS.length())));
        password.append(UPPERCASE_CHARS.charAt(RANDOM.nextInt(UPPERCASE_CHARS.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));
        
        // 填充剩余字符
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }
        
        // 将密码字符打乱顺序
        return shuffleString(password.toString());
    }
    
    /**
     * 打乱字符串顺序
     * @param input 输入字符串
     * @return 打乱顺序后的字符串
     */
    private static String shuffleString(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters, RANDOM);
        StringBuilder output = new StringBuilder(input.length());
        for (char c : characters) {
            output.append(c);
        }
        return output.toString();
    }
    
    /**
     * 验证密码复杂度
     * @param password 待验证的密码
     * @return 是否符合复杂度要求
     */
    public static boolean validatePasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        
        return hasLowercase && hasUppercase && hasDigit && hasSpecial;
    }
}
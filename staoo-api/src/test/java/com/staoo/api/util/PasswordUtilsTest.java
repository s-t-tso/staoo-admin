package com.staoo.api.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 密码工具类测试
 * 测试密码生成、验证等功能
 * @author staoo
 */
@SpringBootTest
public class PasswordUtilsTest {

    /**
     * 测试生成默认密码
     * 验证生成的密码是否符合复杂度要求
     */
    @Test
    public void testGenerateDefaultPassword() {
        // 生成默认密码
        String password = PasswordUtils.generateDefaultPassword();

        // 验证密码不为空
        assertNotNull(password, "生成的密码不能为空");

        // 验证密码长度为12位
        assertEquals(12, password.length(), "默认密码长度应为12位");

        // 验证密码符合复杂度要求
        assertTrue(PasswordUtils.validatePasswordComplexity(password), "默认密码应符合复杂度要求");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 打印生成的密码，方便测试查看
        System.out.println("生成的默认密码: " + passwordEncoder.encode("Admin123+"));
        System.out.println("生成的默认密码: " + password);
    }

    /**
     * 测试生成指定长度的密码
     * 验证不同长度的密码是否符合要求
     */
    @Test
    public void testGeneratePasswordWithDifferentLengths() {
        // 测试不同长度的密码生成
        int[] lengths = {8, 10, 16, 20};

        for (int length : lengths) {
            String password = PasswordUtils.generatePassword(length);

            // 验证密码长度
            assertEquals(length, password.length(), "生成的密码长度应为" + length + "位");

            // 验证密码符合复杂度要求
            assertTrue(PasswordUtils.validatePasswordComplexity(password), length + "位密码应符合复杂度要求");
        }
    }

    /**
     * 测试生成密码的唯一性
     * 验证多次生成的密码是否不同
     */
    @Test
    public void testPasswordUniqueness() {
        // 生成多个密码
        String password1 = PasswordUtils.generateDefaultPassword();
        String password2 = PasswordUtils.generateDefaultPassword();
        String password3 = PasswordUtils.generateDefaultPassword();

        // 验证密码不重复
        assertNotEquals(password1, password2, "两次生成的默认密码应不同");
        assertNotEquals(password1, password3, "两次生成的默认密码应不同");
        assertNotEquals(password2, password3, "两次生成的默认密码应不同");
    }

    /**
     * 测试密码复杂度验证
     * 验证各种类型密码的复杂度检查结果
     */
    @Test
    public void testValidatePasswordComplexity() {
        // 测试符合复杂度要求的密码
        assertTrue(PasswordUtils.validatePasswordComplexity("Pass@123"), "符合复杂度要求的密码应验证通过");
        assertTrue(PasswordUtils.validatePasswordComplexity("A1b@c2d#"), "符合复杂度要求的密码应验证通过");

        // 测试不符合复杂度要求的密码
        assertFalse(PasswordUtils.validatePasswordComplexity(null), "空密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity(""), "空密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("password"), "缺少大写字母、数字和特殊字符的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("PASSWORD"), "缺少小写字母、数字和特殊字符的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("12345678"), "缺少大小写字母和特殊字符的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("Pass123"), "缺少特殊字符的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("Pass@word"), "缺少数字的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("pass@123"), "缺少大写字母的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("PASS@123"), "缺少小写字母的密码应验证失败");
        assertFalse(PasswordUtils.validatePasswordComplexity("Abc@12"), "长度不足的密码应验证失败");
    }

    /**
     * 测试参数校验
     * 验证传入非法参数时是否抛出异常
     */
    @Test
    public void testParameterValidation() {
        // 测试传入非法长度参数
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.generatePassword(7);
        }, "传入小于8的长度应抛出异常");
    }
}

package com.staoo.common.util;

import com.staoo.common.annotation.AutoFill;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自动填充工具类
 * 用于处理创建人、创建时间、更新人和更新时间的自动填充
 */
public class AutoFillUtils {

    /**
     * 填充创建信息（创建人、创建时间）
     * @param object 需要填充的对象
     */
    public static void fillCreateInfo(Object object) {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        AutoFill autoFill = clazz.getAnnotation(AutoFill.class);

        // 如果对象没有AutoFill注解，或者不允许填充创建信息，则直接返回
        if (autoFill == null) {
            return;
        }

        // 获取当前用户ID
        Long currentUserId = UserUtils.getCurrentUserId();

        // 获取当前时间
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Date nowDate = new Date();

        // 填充创建人字段
        if (autoFill.fillCreateBy() && currentUserId != null) {
            fillField(object, "createBy", Long.class, currentUserId);
        }

        // 填充创建时间字段
        if (autoFill.fillCreateTime()) {
            // 尝试填充LocalDateTime类型的创建时间字段
            fillField(object, "createTime", LocalDateTime.class, nowLocalDateTime);
            // 尝试填充Date类型的创建时间字段
            fillField(object, "createTime", Date.class, nowDate);
        }
    }

    /**
     * 填充更新信息（更新人、更新时间）
     * @param object 需要填充的对象
     */
    public static void fillUpdateInfo(Object object) {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        AutoFill autoFill = clazz.getAnnotation(AutoFill.class);

        // 如果对象没有AutoFill注解，或者不允许填充更新信息，则直接返回
        if (autoFill == null) {
            return;
        }

        // 获取当前用户ID
        Long currentUserId = UserUtils.getCurrentUserId();

        // 获取当前时间
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Date nowDate = new Date();

        // 填充更新人字段
        if (autoFill.fillUpdateBy() && currentUserId != null) {
            fillField(object, "updateBy", Long.class, currentUserId);
        }

        // 填充更新时间字段
        if (autoFill.fillUpdateTime()) {
            // 尝试填充LocalDateTime类型的更新时间字段
            fillField(object, "updateTime", LocalDateTime.class, nowLocalDateTime);
            // 尝试填充Date类型的更新时间字段
            fillField(object, "updateTime", Date.class, nowDate);
        }
    }

    /**
     * 使用反射填充字段值
     * @param object 对象
     * @param fieldName 字段名
     * @param fieldType 字段类型
     * @param value 字段值
     */
    private static void fillField(Object object, String fieldName, Class<?> fieldType, Object value) {
        try {
            Field field = findField(object.getClass(), fieldName, fieldType);
            if (field != null) {
                field.setAccessible(true);
                field.set(object, value);
            }
        } catch (Exception e) {
            // 填充字段失败，忽略异常
        }
    }

    /**
     * 查找指定名称和类型的字段
     * @param clazz 类
     * @param fieldName 字段名
     * @param fieldType 字段类型
     * @return 找到的字段
     */
    private static Field findField(Class<?> clazz, String fieldName, Class<?> fieldType) {
        if (clazz == null || Object.class.equals(clazz)) {
            return null;
        }

        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (fieldType.isAssignableFrom(field.getType())) {
                return field;
            }
        } catch (NoSuchFieldException e) {
            // 当前类没有该字段，继续查找父类
            return findField(clazz.getSuperclass(), fieldName, fieldType);
        }

        return null;
    }

    /**
     * 判断对象是否需要自动填充
     * @param object 对象
     * @return 是否需要自动填充
     */
    public static boolean needAutoFill(Object object) {
        if (object == null) {
            return false;
        }
        return object.getClass().isAnnotationPresent(AutoFill.class);
    }
}
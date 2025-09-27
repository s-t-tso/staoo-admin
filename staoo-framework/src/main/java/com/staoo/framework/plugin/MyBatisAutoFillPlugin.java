package com.staoo.framework.plugin;

import com.staoo.common.util.AutoFillUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * MyBatis自动填充插件
 * 用于在数据库操作时自动填充创建人、创建时间、更新人和更新时间字段
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class MyBatisAutoFillPlugin implements Interceptor {

    /**
     * 不需要自动填充的SQL ID前缀
     */
    private static final String[] IGNORE_SQL_PREFIX = {
            "com.staoo.system.mapper.TenantMapper.",
            "com.staoo.system.mapper.OperationLogMapper."
    };

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取参数
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        // 检查是否需要忽略自动填充
        if (shouldIgnoreAutoFill(ms)) {
            return invocation.proceed();
        }

        // 根据SQL类型进行自动填充
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT) {
            // 插入操作，填充创建信息
            AutoFillUtils.fillCreateInfo(parameter);
            // 同时填充更新信息，确保更新人和更新时间也被设置
            AutoFillUtils.fillUpdateInfo(parameter);
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            // 更新操作，填充更新信息
            AutoFillUtils.fillUpdateInfo(parameter);
        }

        // 继续执行原始操作
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 使用MyBatis提供的Plugin.wrap方法创建代理对象
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以通过properties获取配置信息
    }

    /**
     * 判断是否需要忽略自动填充
     * @param ms MappedStatement对象
     * @return 是否忽略
     */
    private boolean shouldIgnoreAutoFill(MappedStatement ms) {
        String sqlId = ms.getId();
        for (String prefix : IGNORE_SQL_PREFIX) {
            if (sqlId.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
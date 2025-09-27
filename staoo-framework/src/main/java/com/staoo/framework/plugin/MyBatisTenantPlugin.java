package com.staoo.framework.plugin;

import com.staoo.common.util.TenantContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * MyBatis多租户插件
 * 用于在SQL执行时自动添加租户ID过滤条件
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class MyBatisTenantPlugin implements Interceptor {

    // 不需要进行租户隔离的表
    private static final Set<String> IGNORE_TABLES = new HashSet<>(Arrays.asList(
            "sys_tenant", // 租户表本身不需要租户过滤
            "sys_config" // 系统配置表
    ));

    // 不需要进行租户隔离的SQL ID前缀
    private static final Set<String> IGNORE_SQL_PREFIX = new HashSet<>(Arrays.asList(
            "com.staoo.system.mapper.TenantMapper."
    ));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取参数
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        // 检查是否需要忽略租户过滤
        if (shouldIgnoreTenantFilter(ms)) {
            return invocation.proceed();
        }

        // 获取租户ID
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            // 没有租户ID时，不进行过滤
            return invocation.proceed();
        }

        // 获取BoundSql
        BoundSql boundSql = ms.getBoundSql(parameter);
        String originalSql = boundSql.getSql();

        // 处理SQL，添加租户ID过滤条件
        String newSql = addTenantFilter(originalSql, tenantId, ms.getSqlCommandType());

        // 创建新的BoundSql
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql, boundSql.getParameterMappings(), parameter);

        // 创建新的MappedStatement
        MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));

        // 替换参数
        args[0] = newMs;

        return invocation.proceed();
    }

    /**
     * 检查是否需要忽略租户过滤
     */
    private boolean shouldIgnoreTenantFilter(MappedStatement ms) {
        // 检查SQL ID是否需要忽略
        String sqlId = ms.getId();
        for (String prefix : IGNORE_SQL_PREFIX) {
            if (sqlId.startsWith(prefix)) {
                return true;
            }
        }

        // 检查表名是否需要忽略
        BoundSql boundSql = ms.getBoundSql(null);
        String sql = boundSql.getSql().toUpperCase();
        for (String table : IGNORE_TABLES) {
            if (sql.contains("FROM " + table.toUpperCase()) || sql.contains("UPDATE " + table.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 添加租户ID过滤条件
     */
    private String addTenantFilter(String sql, Long tenantId, SqlCommandType commandType) {
        if (commandType == SqlCommandType.SELECT) {
            // 处理查询语句
            return addWhereCondition(sql, "tenant_id = " + tenantId);
        } else if (commandType == SqlCommandType.UPDATE) {
            // 处理更新语句
            return addWhereCondition(sql, "tenant_id = " + tenantId);
        } else if (commandType == SqlCommandType.DELETE) {
            // 处理删除语句
            return addWhereCondition(sql, "tenant_id = " + tenantId);
        }
        return sql;
    }

    /**
     * 在SQL语句中添加WHERE条件
     */
    private String addWhereCondition(String sql, String condition) {
        sql = sql.trim();
        int whereIndex = sql.toUpperCase().indexOf("WHERE ");
        if (whereIndex > 0) {
            // 已有WHERE条件，添加AND
            return sql.substring(0, whereIndex + 6) + condition + " AND " + sql.substring(whereIndex + 6);
        } else {
            // 没有WHERE条件，添加WHERE
            return sql + " WHERE " + condition;
        }
    }

    /**
     * 复制MappedStatement
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以通过properties配置一些参数
    }

    /**
     * SqlSource实现类
     */
    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
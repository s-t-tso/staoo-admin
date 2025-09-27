package com.staoo.api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * MyBatis配置类
 * 配置MyBatis相关的设置，确保Mapper接口能够被正确扫描和注入
 */
@Configuration
@EnableTransactionManagement
// 扫描所有模块下的mapper接口
@MapperScan("com.staoo.**.mapper")
public class MyBatisConfig {

    /**
     * 配置SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        
        // 设置Mapper XML文件的位置
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml")
        );
        
        // 设置类型别名包
        factoryBean.setTypeAliasesPackage("com.staoo.**.domain");
        
        // 获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        if (sqlSessionFactory != null) {
            // 配置MyBatis的其他属性
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            // 开启下划线转驼峰
            configuration.setMapUnderscoreToCamelCase(true);
            // 设置日志实现
            configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        }
        
        return sqlSessionFactory;
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
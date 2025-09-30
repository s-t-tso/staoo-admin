package com.staoo.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Staoo Admin 应用主类
 * 作为整个系统的入口点
 */
@SpringBootApplication(scanBasePackages = {"com.staoo"})
@MapperScan("com.staoo.**.mapper") // 扫描所有模块下的mapper接口
public class StaooAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaooAdminApplication.class, args);
    }

}

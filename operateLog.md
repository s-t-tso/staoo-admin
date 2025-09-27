# 系统操作日志
# 系统操作日志

## 2025-08-28
- **操作类型**：[修改]
- **影响文件**：`staoo-common/src/main/java/com/staoo/common/auth/dto/LoginRequest.java`, `staoo-common/src/main/java/com/staoo/common/auth/dto/LoginResponse.java`
- **变更摘要**：移除lombok依赖，将@Data注解替换为标准的getter和setter方法
- **原因**：根据项目要求不使用lombok
- **测试状态**：[待测试]

## 2025-09-09 15:00
- **操作类型**：新增
- **影响文件**：/Users/shitao/wwws/learn/staoo-admin/staoo-common/src/main/java/com/staoo/common/util/EncryptionUtils.java
- **变更摘要**：创建加密工具类，实现了AES和RSA加密解密方法、密钥生成和管理方法
- **原因**：根据前后端接口加密传输实施计划，实现混合加密方案的核心组件
- **测试状态**：待测试

## 2025-09-21 13:00 [修改] 修复MyBatis Mapper扫描问题
- 影响文件：
  - /Users/shitao/wwws/learn/staoo-admin/staoo-api/src/main/java/com/staoo/api/StaooAdminApplication.java
  - /Users/shitao/wwws/learn/staoo-admin/staoo-api/src/main/java/com/staoo/api/config/MyBatisConfig.java
- 变更摘要：添加@MapperScan注解和专门的MyBatis配置类，解决UserMapper无法被自动注入的问题
- 原因：应用启动报错，无法找到UserMapper的Bean定义
- 测试状态：待测试

## 2025-09-21 13:10 [修改]
- 影响文件：
  - /Users/shitao/wwws/learn/staoo-admin/staoo-api/src/main/java/com/staoo/api/StaooAdminApplication.java

## 2025-09-21 [修改] 修复LoginLogMapper字段映射问题
- 影响文件：
  - /Users/shitao/wwws/learn/staoo-admin/staoo-system/src/main/resources/mapper/system/LoginLogMapper.xml
  - /Users/shitao/wwws/learn/staoo-admin/staoo-system/src/main/java/com/staoo/system/domain/LoginLog.java
- 变更摘要：修复LoginLogMapper.xml中的字段映射，使其与LoginLog实体类和数据库表结构保持一致；在LoginLog实体类中添加了tenantId字段
- 原因：修正字段命名不一致问题，确保数据正确映射
- 测试状态：待测试
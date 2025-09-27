-- 创建用户-租户关联表
drop table if exists sys_user_tenant;
create table `sys_user_tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `role_type` tinyint NOT NULL DEFAULT 3 COMMENT '租户内角色类型：1-创建者，2-管理者，3-普通用户',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tenant` (`user_id`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-租户关联表';

-- 修改sys_user表，移除tenant_id和tenant_code字段
alter table sys_user drop column if exists tenant_id;
alter table sys_user drop column if exists tenant_code;

-- 数据迁移：将现有用户的单租户关系迁移到sys_user_tenant表
-- 注意：此迁移语句需要根据实际情况调整
-- 假设当前所有用户都属于默认租户（tenant_id=1），角色类型为普通用户
insert into sys_user_tenant (user_id, tenant_id, role_type, status)
select id, 1, 3, status from sys_user;

-- 如果有其他租户，需要根据实际情况进行迁移
-- 例如，假设某些用户属于特定租户：
-- insert into sys_user_tenant (user_id, tenant_id, role_type, status)
-- select id, tenant_id, 3, status from sys_user where tenant_id is not null;
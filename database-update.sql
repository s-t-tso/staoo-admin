-- Staoo Admin 系统数据库更新脚本
-- 时间：2024-11-12

-- 使用数据库
USE staoo_admin;

-- 1. 用户表 - 添加admin字段
ALTER TABLE sys_user
ADD COLUMN admin INT DEFAULT 0 COMMENT '是否为管理员（0-否，1-是）' AFTER last_login_ip;

-- 2. 角色表 - 添加createBy和updateBy字段
ALTER TABLE sys_role
ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER data_scope,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 3. 部门表 - 添加createBy和updateBy字段
ALTER TABLE sys_dept
ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER update_time,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 4. 菜单表 - 添加visible、cacheable、createBy和updateBy字段
ALTER TABLE sys_menu
ADD COLUMN visible INT DEFAULT 1 COMMENT '是否显示（0-不显示，1-显示）' AFTER update_time,
ADD COLUMN cacheable INT DEFAULT 0 COMMENT '是否缓存（0-不缓存，1-缓存）' AFTER visible,
ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER cacheable,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 5. 数据字典表 - 添加createBy和updateBy字段
ALTER TABLE sys_dict
ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER update_time,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 6. 数据字典项表 - 添加createBy和updateBy字段
ALTER TABLE sys_dict_item
ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER update_time,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 7. 系统通知表 - 添加level、is_top、read_status、createBy和updateBy字段
ALTER TABLE sys_notice
ADD COLUMN level INT DEFAULT 1 COMMENT '通知级别（1-普通，2-重要，3-紧急）' AFTER expire_time,
ADD COLUMN is_top TINYINT DEFAULT 0 COMMENT '是否置顶（0-否，1-是）' AFTER level,
ADD COLUMN read_status INT DEFAULT 0 COMMENT '阅读状态（0-未读，1-已读）' AFTER is_top,
ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER create_by;

-- 8. 更新管理员用户为超级管理员
UPDATE sys_user SET admin = 1 WHERE username = 'admin';

-- 完成数据库更新
SELECT 'Staoo Admin 系统数据库更新完成！' AS message;
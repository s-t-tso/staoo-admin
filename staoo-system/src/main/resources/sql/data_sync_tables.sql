-- 数据同步模块数据库表结构

-- 1. 数据订阅表 - 存储第三方应用对数据变更的订阅信息
CREATE TABLE IF NOT EXISTS `data_subscription` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_key` varchar(64) NOT NULL COMMENT '应用标识',
  `data_type` varchar(64) NOT NULL COMMENT '数据类型',
  `callback_url` varchar(512) NOT NULL COMMENT '回调地址',
  `status` varchar(1) NOT NULL DEFAULT '0' COMMENT '状态：0-启用，1-禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_key_data_type` (`app_key`,`data_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据订阅表';

-- 2. 数据变更日志表 - 记录数据变更历史
CREATE TABLE IF NOT EXISTS `data_change_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `data_type` varchar(64) NOT NULL COMMENT '数据类型',
  `data_id` varchar(255) DEFAULT NULL COMMENT '数据ID',
  `change_type` varchar(64) NOT NULL COMMENT '变更类型：INSERT/UPDATE/DELETE',
  `change_content` text COMMENT '变更内容(JSON格式)',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(128) DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_data_type` (`tenant_id`,`data_type`),
  KEY `idx_data_id` (`data_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据变更日志表';

-- 3. 通知消息表 - 记录发送的通知消息
CREATE TABLE IF NOT EXISTS `notification_message` (
  `message_id` varchar(64) NOT NULL COMMENT '消息ID',
  `app_key` varchar(64) NOT NULL COMMENT '应用标识',
  `data_type` varchar(64) NOT NULL COMMENT '数据类型',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `data` text COMMENT '数据内容(JSON格式)',
  `change_type` varchar(64) DEFAULT NULL COMMENT '变更类型',
  `callback_url` varchar(512) DEFAULT NULL COMMENT '回调地址',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `status` varchar(1) DEFAULT '0' COMMENT '状态：0-待发送，1-发送成功，2-发送失败',
  `error_message` text COMMENT '错误信息',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  PRIMARY KEY (`message_id`),
  KEY `idx_app_key_data_type` (`app_key`,`data_type`),
  KEY `idx_status` (`status`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';

-- 4. 流程变更日志表 - 记录流程实例的状态变更
CREATE TABLE IF NOT EXISTS `flow_change_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_instance_id` varchar(64) NOT NULL COMMENT '流程实例ID',
  `status` varchar(64) NOT NULL COMMENT '流程状态',
  `result` text COMMENT '流程结果(JSON格式)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_flow_instance_id` (`flow_instance_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程变更日志表';
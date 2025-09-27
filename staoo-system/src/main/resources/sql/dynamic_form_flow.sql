-- ----------------------------
-- 动态表单和流程模块数据库表结构
-- ----------------------------

-- 1. 表单模板表 - 存储表单模板配置信息
CREATE TABLE IF NOT EXISTS `sys_form_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `form_key` varchar(64) NOT NULL COMMENT '表单标识',
  `form_name` varchar(128) NOT NULL COMMENT '表单名称',
  `description` varchar(512) DEFAULT NULL COMMENT '表单描述',
  `form_config` text COMMENT '表单配置(JSON格式)',
  `status` varchar(10) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_form_key_tenant_id` (`form_key`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单模板表';

-- 2. 流程模板表 - 存储流程模板配置信息
CREATE TABLE IF NOT EXISTS `sys_process_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_key` varchar(64) NOT NULL COMMENT '流程标识',
  `process_name` varchar(128) NOT NULL COMMENT '流程名称',
  `description` varchar(512) DEFAULT NULL COMMENT '流程描述',
  `bpmn_xml` text COMMENT 'BPMN流程定义XML',
  `status` varchar(10) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `category` varchar(64) DEFAULT NULL COMMENT '流程分类',
  `form_key` varchar(64) DEFAULT NULL COMMENT '关联的表单标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_process_key_tenant_id` (`process_key`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_status` (`status`),
  KEY `idx_category` (`category`),
  KEY `idx_form_key` (`form_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程模板表';

-- 3. 表单数据表 - 存储表单数据记录
CREATE TABLE IF NOT EXISTS `sys_form_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `form_key` varchar(64) NOT NULL COMMENT '表单标识',
  `template_id` bigint NOT NULL COMMENT '表单模板ID',
  `form_data` text NOT NULL COMMENT '表单数据(JSON格式)',
  `status` varchar(10) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，SUBMITTED-已提交，APPROVED-已审批，REJECTED-已拒绝',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `creator_name` varchar(128) DEFAULT NULL COMMENT '创建人姓名',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '关联的流程实例ID',
  `business_key` varchar(64) NOT NULL COMMENT '业务键',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_business_key_form_key_tenant_id` (`business_key`,`form_key`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_form_key` (`form_key`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_by` (`create_by`),
  KEY `idx_process_instance_id` (`process_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单数据表';

-- 4. 流程任务记录表 - 存储流程任务的处理记录
CREATE TABLE IF NOT EXISTS `sys_flow_task_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_instance_id` varchar(64) NOT NULL COMMENT '流程实例ID',
  `task_id` varchar(64) NOT NULL COMMENT '任务ID',
  `task_name` varchar(128) DEFAULT NULL COMMENT '任务名称',
  `assignee_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `assignee_name` varchar(128) DEFAULT NULL COMMENT '处理人姓名',
  `action` varchar(64) NOT NULL COMMENT '处理动作：APPROVE-审批通过，REJECT-拒绝，CLAIM-认领，ASSIGN-指派',
  `comment` varchar(512) DEFAULT NULL COMMENT '处理意见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_process_instance_id` (`process_instance_id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程任务记录表';
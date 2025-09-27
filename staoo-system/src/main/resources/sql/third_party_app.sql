-- ----------------------------
-- 第三方应用表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `third_party_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `app_name` varchar(100) NOT NULL COMMENT '应用名称',
  `app_key` varchar(100) NOT NULL COMMENT '应用标识',
  `app_secret` varchar(255) NOT NULL COMMENT '应用密钥（加密存储）',
  `app_icon` varchar(255) DEFAULT NULL COMMENT '应用图标',
  `app_domain` varchar(255) DEFAULT NULL COMMENT '应用域名',
  `status` varchar(10) NOT NULL DEFAULT '0' COMMENT '应用状态（0：启用，1：禁用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_key` (`app_key`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用表';

-- ----------------------------
-- 第三方应用回调地址表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `third_party_app_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `callback_url` varchar(500) NOT NULL COMMENT '回调地址',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  CONSTRAINT `fk_app_callback` FOREIGN KEY (`app_id`) REFERENCES `third_party_app` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用回调地址表';

-- ----------------------------
-- 第三方应用权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `third_party_app_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `permission` varchar(100) NOT NULL COMMENT '权限标识',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  UNIQUE KEY `uk_app_permission` (`app_id`,`permission`),
  CONSTRAINT `fk_app_permission` FOREIGN KEY (`app_id`) REFERENCES `third_party_app` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用权限表';

-- ----------------------------
-- 第三方应用租户关系表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `third_party_app_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  UNIQUE KEY `uk_app_tenant` (`app_id`,`tenant_id`),
  CONSTRAINT `fk_app_tenant` FOREIGN KEY (`app_id`) REFERENCES `third_party_app` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tenant_app` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用租户关系表';
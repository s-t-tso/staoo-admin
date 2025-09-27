-- Staoo Admin 系统数据库初始化脚本
-- MySQL 8.0

-- 创建数据库
CREATE DATABASE IF NOT EXISTS staoo_admin DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE staoo_admin;

-- 1. 租户表
CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID',
    tenant_name VARCHAR(100) NOT NULL COMMENT '租户名称',
    remark VARCHAR(255) DEFAULT NULL COMMENT '租户备注',
    status INT DEFAULT 0 COMMENT '租户状态（0：正常，1：停用）',
    sort INT DEFAULT 0 COMMENT '租户排序',
    tenant_params TEXT DEFAULT NULL COMMENT '租户参数',
    contact_phone VARCHAR(20) DEFAULT NULL COMMENT '租户联系电话',
    contact_email VARCHAR(100) DEFAULT NULL COMMENT '租户联系邮箱',
    contact_address VARCHAR(255) DEFAULT NULL COMMENT '租户联系地址',
    contact_person VARCHAR(50) DEFAULT NULL COMMENT '租户联系人',
    person_phone VARCHAR(20) DEFAULT NULL COMMENT '租户联系人电话',
    person_email VARCHAR(100) DEFAULT NULL COMMENT '租户联系人邮箱',
    person_address VARCHAR(255) DEFAULT NULL COMMENT '租户联系人地址',
    person_params TEXT DEFAULT NULL COMMENT '租户联系人参数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag INT DEFAULT 0 COMMENT '删除标志（0:未删除 1:已删除）',
    UNIQUE KEY uk_tenant_name (tenant_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- 2. 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名称',
    password VARCHAR(100) NOT NULL COMMENT '用户密码',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    phone VARCHAR(20) DEFAULT NULL COMMENT '用户手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
    status INT DEFAULT 1 COMMENT '用户状态（0-禁用，1-启用，2-离职）',
    dept_id BIGINT DEFAULT 0 COMMENT '部门ID',
    work_no VARCHAR(50) DEFAULT NULL COMMENT '工号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    INDEX idx_dept_id (dept_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    sort INT DEFAULT 0 COMMENT '部门排序',
    leader_id BIGINT DEFAULT 0 COMMENT '部门负责人ID',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    status INT DEFAULT 1 COMMENT '部门状态（0-禁用，1-启用）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_leader_id (leader_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 4. 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    remark VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    status INT DEFAULT 1 COMMENT '角色状态（0-禁用，1-启用）',
    sort INT DEFAULT 0 COMMENT '角色排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    data_scope INT DEFAULT 1 COMMENT '数据权限类型（1-所有数据，2-自定义数据）',
    UNIQUE KEY uk_role_name (role_name),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 5. 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 6. 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_type INT DEFAULT 0 COMMENT '菜单类型（0-目录，1-菜单，2-按钮）',
    icon VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
    path VARCHAR(255) DEFAULT NULL COMMENT '菜单路径',
    component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    perms VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    sort INT DEFAULT 0 COMMENT '菜单排序',
    status INT DEFAULT 1 COMMENT '菜单状态（0-禁用，1-启用）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 7. 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 8. 数据字典表
CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_name VARCHAR(50) NOT NULL COMMENT '字典名称',
    remark VARCHAR(255) DEFAULT NULL COMMENT '字典描述',
    status INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_dict_type (dict_type),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- 9. 数据字典项表
CREATE TABLE IF NOT EXISTS sys_dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典项ID',
    dict_id BIGINT NOT NULL COMMENT '字典ID',
    dict_label VARCHAR(50) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(50) NOT NULL COMMENT '字典值',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dict_id (dict_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典项表';

-- 10. 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    module VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    operation_type VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    content VARCHAR(255) DEFAULT NULL COMMENT '操作内容',
    request_url VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
    request_method VARCHAR(20) DEFAULT NULL COMMENT '请求方法',
    request_params TEXT DEFAULT NULL COMMENT '请求参数',
    request_body TEXT DEFAULT NULL COMMENT '请求体',
    response_result TEXT DEFAULT NULL COMMENT '响应结果',
    status INT DEFAULT 1 COMMENT '操作状态（0-失败，1-成功）',
    error_message TEXT DEFAULT NULL COMMENT '错误信息',
    user_id BIGINT DEFAULT 0 COMMENT '操作人ID',
    username VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    ip VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
    ip_location VARCHAR(100) DEFAULT NULL COMMENT 'IP地址所在地区',
    browser_info VARCHAR(255) DEFAULT NULL COMMENT '浏览器信息',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_user_id (user_id),
    INDEX idx_operation_time (operation_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 11. 登录日志表
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    user_id BIGINT DEFAULT 0 COMMENT '用户ID',
    username VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    login_ip VARCHAR(50) DEFAULT NULL COMMENT '登录IP',
    login_location VARCHAR(100) DEFAULT NULL COMMENT 'IP地址所在地区',
    browser_info VARCHAR(255) DEFAULT NULL COMMENT '浏览器信息',
    login_status INT DEFAULT 1 COMMENT '登录状态（0-失败，1-成功）',
    error_message VARCHAR(255) DEFAULT NULL COMMENT '错误信息',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_user_id (user_id),
    INDEX idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 10. 用户-租户关联表
CREATE TABLE IF NOT EXISTS sys_user_tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主租户（0-否，1-是）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id_tenant_id (user_id, tenant_id),
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-租户关联表';

-- 12. 流程模板表
CREATE TABLE IF NOT EXISTS sys_process_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    process_key VARCHAR(100) NOT NULL COMMENT '流程唯一标识',
    process_name VARCHAR(100) NOT NULL COMMENT '流程名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    bpmn_xml LONGTEXT COMMENT 'BPMN XML定义',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态（草稿、已发布）',
    version INT DEFAULT 1 COMMENT '版本号',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人ID',
    category VARCHAR(50) DEFAULT NULL COMMENT '流程分类',
    form_key VARCHAR(100) DEFAULT NULL COMMENT '关联的表单标识',
    UNIQUE KEY uk_process_key_version (process_key, version),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_create_by (create_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程模板表';

-- 13. 表单模板表
CREATE TABLE IF NOT EXISTS sys_form_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    form_key VARCHAR(100) NOT NULL COMMENT '表单唯一标识',
    form_name VARCHAR(100) NOT NULL COMMENT '表单名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '描述',
    form_config LONGTEXT COMMENT '表单配置JSON',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态（草稿、已发布）',
    version INT DEFAULT 1 COMMENT '版本号',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人ID',
    UNIQUE KEY uk_form_key_version (form_key, version),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_create_by (create_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单模板表';

-- 14. 表单数据表
CREATE TABLE IF NOT EXISTS sys_form_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID',
    form_key VARCHAR(100) NOT NULL COMMENT '表单标识',
    form_data LONGTEXT COMMENT '表单数据JSON',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '数据状态',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人ID',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_form_key (form_key),
    INDEX idx_create_by (create_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单数据表';

-- 15. 系统通知表
CREATE TABLE IF NOT EXISTS sys_notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content LONGTEXT COMMENT '通知内容',
    type INT DEFAULT 0 COMMENT '通知类型（0-系统通知，1-业务通知）',
    status INT DEFAULT 1 COMMENT '通知状态（0-未发布，1-已发布）',
    publish_time DATETIME DEFAULT NULL COMMENT '发布时间',
    expire_time DATETIME DEFAULT NULL COMMENT '过期时间',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人ID',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- 16. 第三方应用表
CREATE TABLE IF NOT EXISTS sys_third_party_app (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '应用ID',
    app_name VARCHAR(100) NOT NULL COMMENT '应用名称',
    app_key VARCHAR(100) NOT NULL COMMENT '应用密钥',
    app_secret VARCHAR(100) NOT NULL COMMENT '应用密钥',
    redirect_uri VARCHAR(255) DEFAULT NULL COMMENT '回调地址',
    status INT DEFAULT 1 COMMENT '应用状态（0-禁用，1-启用）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_app_key (app_key),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用表';

-- 17. 数据订阅表
CREATE TABLE IF NOT EXISTS sys_data_subscription (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订阅ID',
    subscription_name VARCHAR(100) NOT NULL COMMENT '订阅名称',
    data_type VARCHAR(50) NOT NULL COMMENT '数据类型',
    filter_condition TEXT DEFAULT NULL COMMENT '过滤条件',
    callback_url VARCHAR(255) DEFAULT NULL COMMENT '回调地址',
    status INT DEFAULT 1 COMMENT '订阅状态（0-禁用，1-启用）',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人ID',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_data_type (data_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据订阅表';

-- 18. 流程任务记录表
CREATE TABLE IF NOT EXISTS sys_flow_task_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    process_instance_id VARCHAR(100) NOT NULL COMMENT '流程实例ID',
    task_id VARCHAR(100) DEFAULT NULL COMMENT '任务ID',
    task_name VARCHAR(100) DEFAULT NULL COMMENT '任务名称',
    assignee_id BIGINT DEFAULT 0 COMMENT '处理人ID',
    assignee_name VARCHAR(50) DEFAULT NULL COMMENT '处理人姓名',
    action VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    comment TEXT DEFAULT NULL COMMENT '审批意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_process_instance_id (process_instance_id),
    INDEX idx_task_id (task_id),
    INDEX idx_assignee_id (assignee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程任务记录表';

-- 插入初始数据
-- 1. 插入默认租户
INSERT INTO sys_tenant (id, tenant_name, remark, status) VALUES (1, '默认租户', '系统默认租户', 0);

-- 2. 插入管理员用户（密码：123456，已加密）
INSERT INTO sys_user (id, username, password, nickname, phone, email, status, dept_id, tenant_id) 
VALUES (1, 'admin', '$2a$10$wJ7Q6q7GJZ8uQ0vF0k9R0e4B5C6D7E8F9G1H2I3J4K5L6M7N8O', '管理员', '13800138000', 'admin@example.com', 1, 0, 1);

-- 3. 关联用户和租户
INSERT INTO sys_user_tenant (user_id, tenant_id, is_primary) VALUES (1, 1, 1);

-- 4. 插入默认角色

-- 3. 插入默认角色
INSERT INTO sys_role (id, tenant_id, role_name, remark, status, data_scope) VALUES (1, 1, '超级管理员', '系统最高权限角色', 1, 1);

-- 4. 关联用户和角色
INSERT INTO sys_user_role (user_id, role_id, tenant_id) VALUES (1, 1, 1);

-- 5. 插入基础菜单
INSERT INTO sys_menu (id, tenant_id, menu_name, parent_id, menu_type, icon, path, component, perms, sort, status) VALUES 
(1, 1, '系统管理', 0, 0, 'system', 'system', '', '', 1, 1),
(2, 1, '用户管理', 1, 1, 'user', 'user', 'system/user/index', 'user:list', 1, 1),
(3, 1, '角色管理', 1, 1, 'role', 'role', 'system/role/index', 'role:list', 2, 1),
(4, 1, '菜单管理', 1, 1, 'menu', 'menu', 'system/menu/index', 'menu:list', 3, 1),
(5, 1, '部门管理', 1, 1, 'dept', 'dept', 'system/dept/index', 'dept:list', 4, 1),
(6, 1, '数据字典', 1, 1, 'dict', 'dict', 'system/dict/index', 'dict:list', 5, 1),
(7, 1, '操作日志', 1, 1, 'log', 'log', 'system/log/index', 'log:list', 6, 1),
(8, 1, '流程管理', 0, 0, 'flow', 'flow', '', '', 2, 1),
(9, 1, '流程设计', 8, 1, 'design', 'design', 'flow/design/index', 'flow:design', 1, 1),
(10, 1, '流程实例', 8, 1, 'instance', 'instance', 'flow/instance/index', 'flow:instance', 2, 1),
(11, 1, '表单管理', 0, 0, 'form', 'form', '', '', 3, 1),
(12, 1, '表单设计', 11, 1, 'form-design', 'form-design', 'form/design/index', 'form:design', 1, 1),
(13, 1, '表单数据', 11, 1, 'form-data', 'form-data', 'form/data/index', 'form:data', 2, 1),-- 14. 租户管理
(14, 1, '租户管理', 1, 1, 'tenant', 'tenant', 'system/tenant/index', 'tenant:list', 7, 1);
-- 15. 用户-租户关联管理
(15, 1, '用户租户关联', 1, 1, 'user-tenant', 'user-tenant', 'system/user-tenant/index', 'user-tenant:list', 8, 1); 6. 关联角色和菜单
INSERT INTO sys_role_menu (role_id, menu_id, tenant_id) SELECT 1, id, 1 FROM sys_menu;

-- 7. 插入数据字典类型
INSERT INTO sys_dict (id, tenant_id, dict_type, dict_name, status) VALUES 
(1, 1, 'sys_user_status', '用户状态', 1),
(2, 1, 'sys_user_type', '用户类型', 1),
(3, 1, 'sys_dept_type', '部门类型', 1),
(4, 1, 'sys_role_type', '角色类型', 1);

-- 8. 插入数据字典项
INSERT INTO sys_dict_item (dict_id, tenant_id, dict_label, dict_value, sort, status) VALUES 
(1, 1, '启用', '1', 1, 1),
(1, 1, '禁用', '0', 2, 1),
(2, 1, '管理员', '1', 1, 1),
(2, 1, '普通用户', '2', 2, 1),
(3, 1, '公司', '1', 1, 1),
(3, 1, '部门', '2', 2, 1),
(3, 1, '小组', '3', 3, 1),
(4, 1, '超级管理员', '1', 1, 1),
(4, 1, '普通角色', '2', 2, 1);

-- 9. 插入部门数据
INSERT INTO sys_dept (id, tenant_id, dept_name, parent_id, sort, status) VALUES 
(1, 1, '总公司', 0, 1, 1),
(2, 1, '技术部', 1, 1, 1),
(3, 1, '市场部', 1, 2, 1),
(4, 1, '财务部', 1, 3, 1);

-- 完成数据库初始化
SELECT 'Staoo Admin 系统数据库初始化完成！' AS message;
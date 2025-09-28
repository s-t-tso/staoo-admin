-- 菜单数据插入SQL语句
-- 包含用户管理、部门管理、菜单管理、模板管理、动态表单管理及其功能路由

-- 使用数据库
USE staoo_admin;

-- 系统管理目录
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('系统管理', 0, 0, 'Setting', '/system', 'modules/system/index.vue', 'system:manage', 1, 1, '系统管理模块');

-- 用户管理菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('用户管理', 1, 1, 'User', 'user', 'modules/system/user/UserList.vue', 'system:user:view', 1, 1, '用户管理菜单');

-- 用户管理功能路由（按钮权限）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('用户新增', 2, 2, 'system:user:add', 1, 1, '用户新增按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('用户编辑', 2, 2, 'system:user:edit', 2, 1, '用户编辑按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('用户删除', 2, 2, 'system:user:delete', 3, 1, '用户删除按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('用户查看', 2, 2, 'system:user:detail', 4, 1, '用户查看按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('用户重置密码', 2, 2, 'system:user:resetPwd', 5, 1, '用户重置密码按钮权限');

-- 部门管理菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('部门管理', 1, 1, 'OfficeBuilding', 'department', 'modules/system/department/DepartmentList.vue', 'system:dept:view', 2, 1, '部门管理菜单');

-- 部门管理功能路由（按钮权限）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('部门新增', 8, 2, 'system:dept:add', 1, 1, '部门新增按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('部门编辑', 8, 2, 'system:dept:edit', 2, 1, '部门编辑按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('部门删除', 8, 2, 'system:dept:delete', 3, 1, '部门删除按钮权限');

-- 菜单管理菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('菜单管理', 1, 1, 'Menu', 'menu', 'modules/system/menu/MenuList.vue', 'system:menu:view', 3, 1, '菜单管理菜单');

-- 菜单管理功能路由（按钮权限）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('菜单新增', 12, 2, 'system:menu:add', 1, 1, '菜单新增按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('菜单编辑', 12, 2, 'system:menu:edit', 2, 1, '菜单编辑按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('菜单删除', 12, 2, 'system:menu:delete', 3, 1, '菜单删除按钮权限');

-- 模板管理目录
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('模板管理', 0, 0, 'Template', '/template', 'modules/template/index.vue', 'template:manage', 2, 1, '模板管理模块');

-- 模板管理菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('模板列表', 16, 1, 'List', 'template-list', 'modules/template/TemplateList.vue', 'template:list:view', 1, 1, '模板列表菜单');

-- 模板管理功能路由（按钮权限）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('模板新增', 17, 2, 'template:add', 1, 1, '模板新增按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('模板编辑', 17, 2, 'template:edit', 2, 1, '模板编辑按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('模板删除', 17, 2, 'template:delete', 3, 1, '模板删除按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('模板预览', 17, 2, 'template:preview', 4, 1, '模板预览按钮权限');

-- 动态表单管理目录
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('动态表单管理', 0, 0, 'Document', '/form', 'modules/form/index.vue', 'form:manage', 3, 1, '动态表单管理模块');

-- 动态表单管理菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('表单设计', 22, 1, 'Edit', 'form-design', 'modules/form/design/FormDesign.vue', 'form:design:view', 1, 1, '表单设计菜单');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('表单实例', 22, 1, 'Files', 'form-instance', 'modules/form/instance/FormInstanceList.vue', 'form:instance:view', 2, 1, '表单实例菜单');

-- 动态表单管理功能路由（按钮权限）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单设计新增', 23, 2, 'form:design:add', 1, 1, '表单设计新增按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单设计编辑', 23, 2, 'form:design:edit', 2, 1, '表单设计编辑按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单设计删除', 23, 2, 'form:design:delete', 3, 1, '表单设计删除按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单设计发布', 23, 2, 'form:design:publish', 4, 1, '表单设计发布按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单实例查看', 24, 2, 'form:instance:view', 1, 1, '表单实例查看按钮权限');

INSERT INTO sys_menu (menu_name, parent_id, menu_type, perms, sort, status, remark)
VALUES ('表单实例删除', 24, 2, 'form:instance:delete', 2, 1, '表单实例删除按钮权限');

-- 首页菜单
INSERT INTO sys_menu (menu_name, parent_id, menu_type, icon, path, component, perms, sort, status, remark)
VALUES ('首页', 0, 1, 'Home', '/dashboard', 'modules/dashboard/Dashboard.vue', 'dashboard:view', 0, 1, '系统首页');
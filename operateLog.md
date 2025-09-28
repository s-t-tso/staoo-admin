## 2024-11-09 14:30
**操作类型：** 修改
**影响文件：** /Users/shitao/wwws/learn/staoo-admin/staoo-ui/src/utils/request.ts
**变更摘要：** 修改了request工具类中的get和delete方法，使其直接传递参数而不是包装在params对象中
**原因：** 使列表接口参数格式从嵌套格式(params[roleName]=)改为扁平化格式(roleName=)
**测试状态：** 已测试（编译通过）

**操作类型：** 修改
**影响文件：** /Users/shitao/wwws/learn/staoo-admin/staoo-ui/src/services/menuService.ts, /Users/shitao/wwws/learn/staoo-admin/staoo-ui/src/store/index.ts
**变更摘要：** 修复getMenuList函数处理后端返回的AjaxResult格式数据的问题，添加权限数据处理逻辑
**原因：** 原函数假设返回值直接是menuList数组，但实际返回的是包含menuList和permissions的对象
**测试状态：** 已测试（编译通过）

**操作类型：** 功能增强
**影响文件：** /Users/shitao/wwws/learn/staoo-admin/staoo-system/src/main/java/com/staoo/system/service/impl/MenuServiceImpl.java
**变更摘要：** 优化菜单接口，当用户是超级管理员或具有admin角色时返回所有激活的菜单
**原因：** 提高系统管理效率，让管理员能够查看和管理所有菜单
**测试状态：** 待测试

**操作类型：** 功能修改
**影响文件：** /Users/shitao/wwws/learn/staoo-admin/staoo-system/src/main/java/com/staoo/system/auth/strategy/AbstractLoginStrategy.java, /Users/shitao/wwws/learn/staoo-admin/staoo-framework/src/main/java/com/staoo/framework/auth/filter/JwtAuthenticationFilter.java
**变更摘要：** 修改超级管理员判断逻辑，由通过isSuperAdmin字段判断改为通过admin字段是否为1来判断
**原因：** 按照用户需求调整超级管理员判断逻辑
**测试状态：** 待测试

## 2024-11-09 14:30
**操作类型：** 修改
**影响文件：** /Users/shitao/wwws/learn/staoo-admin/staoo-ui/src/utils/request.ts
**变更摘要：** 修改了request工具类中的get和delete方法，使其直接传递参数而不是包装在params对象中
**原因：** 使列表接口参数格式从嵌套格式(params[roleName]=)改为扁平化格式(roleName=)
**测试状态：** 待测试

import { ElMessage } from 'element-plus';
import request from '@/utils/request';

/**
 * 菜单/路由项接口定义
 */
export interface MenuItem {
  id: number;
  parentId: number | null;
  path: string;
  name: string;
  component?: string;
  redirect?: string;
  meta: {
    title: string;
    icon?: string;
    hidden?: boolean;
    roles?: string[];
    [key: string]: any;
  };
  children?: MenuItem[];
}

/**
 * 菜单和权限数据接口
 */
export interface MenuAndPermissionResult {
  menuList: MenuItem[];
  permissions: string[];
}

/**
 * 获取当前用户的菜单列表和权限列表
 * @returns 包含菜单列表和权限列表的对象
 */
export const getMenuList = async (): Promise<MenuAndPermissionResult> => {
  try {
    const response: any = await request.get('/system/menu/nav');
    // 确保返回的是正确格式的数据
    // 处理AjaxResult格式：{code: 200, data: {menuList: [...], permissions: [...]}, message: "操作成功"}
    if (response && response.code === 200 && response.data && response.data.menuList && Array.isArray(response.data.menuList)) {
      return {
        menuList: response.data.menuList,
        permissions: response.data.permissions || []
      };
    }
    // 如果格式不匹配，返回模拟数据
    ElMessage.warning('使用模拟菜单和权限数据');
    return getMockMenuAndPermissionResult();
  } catch (error) {
    console.error('获取菜单列表失败:', error);
    ElMessage.error('获取菜单列表失败');
    // 失败时返回模拟数据，确保应用能正常运行
    return getMockMenuAndPermissionResult();
  }
}

/**
 * 获取模拟菜单和权限数据（当API调用失败时使用）
 * @returns 包含模拟菜单列表和权限列表的对象
 */
const getMockMenuAndPermissionResult = (): MenuAndPermissionResult => {
  return {
    menuList: [
    {
      id: 1,
      parentId: null,
      path: '/dashboard',
      name: 'Dashboard',
      component: 'modules/dashboard/Dashboard.vue',
      meta: {
        title: '首页',
        icon: 'Home',
        hidden: false
      }
    },
    {
      id: 2,
      parentId: null,
      path: '/system',
      name: 'System',
      component: 'modules/system/index.vue',
      meta: {
        title: '系统管理',
        icon: 'Setting',
        hidden: false
      },
      children: [
        {
          id: 21,
          parentId: 2,
          path: 'user',
          name: 'User',
          component: 'modules/system/user/UserList.vue',
          meta: {
            title: '用户管理',
            hidden: false
          }
        },
        {
          id: 22,
          parentId: 2,
          path: 'role',
          name: 'Role',
          component: 'modules/system/role/RoleList.vue',
          meta: {
            title: '角色管理',
            hidden: false
          }
        },
        {
          id: 23,
          parentId: 2,
          path: 'department',
          name: 'Department',
          component: 'modules/system/department/DepartmentList.vue',
          meta: {
            title: '部门管理',
            hidden: false
          }
        },
        {
          id: 24,
          parentId: 2,
          path: 'menu',
          name: 'Menu',
          component: 'modules/system/menu/MenuList.vue',
          meta: {
            title: '菜单管理',
            hidden: false
          }
        }
      ]
    },
    {
      id: 3,
      parentId: null,
      path: '/flow',
      name: 'Flow',
      component: 'modules/flow/index.vue',
      meta: {
        title: '流程管理',
        icon: 'flow-chart',
        hidden: false
      },
      children: [
        {
          id: 31,
          parentId: 3,
          path: 'process-template',
          name: 'ProcessTemplate',
          component: 'modules/flow/process-template/ProcessTemplateList.vue',
          meta: {
            title: '流程模板',
            icon: 'template',
            hidden: false
          }
        },
        {
          id: 32,
          parentId: 3,
          path: 'form-template',
          name: 'FormTemplate',
          component: 'modules/flow/form-template/FormTemplateList.vue',
          meta: {
            title: '表单模板',
            icon: 'form',
            hidden: false
          }
        }
      ]
    }
    ],
    permissions: ['*:*:*'] // 模拟的超级管理员权限
  }
};
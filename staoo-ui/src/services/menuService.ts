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
    if (response.data && response.data.menuList && Array.isArray(response.data.menuList)) {
      // 转换API返回的数据结构为MenuItem格式
      const menuItems = transformMenuData(response.data.menuList);
      return {
        menuList: menuItems,
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
};

/**
 * 将API返回的菜单数据转换为MenuItem格式
 * @param menuData API返回的菜单数据
 * @returns 转换后的MenuItem数组
 */
const transformMenuData = (menuData: any[]): MenuItem[] => {
  return menuData.map(item => ({
    id: item.id,
    parentId: item.parentId,
    path: item.path || '',
    name: item.menuName || '',
    component: item.component || '',
    meta: {
      title: item.menuName || '',
      icon: item.icon || 'Menu',
      hidden: item.status !== 1 // 假设status=1表示启用状态
    },
    children: item.children && item.children.length > 0 ? transformMenuData(item.children) : []
  }));
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
        },
        {
          id: 25,
          parentId: 2,
          path: 'tenant',
          name: 'Tenant',
          component: 'modules/system/tenant/TenantList.vue',
          meta: {
            title: '租户管理',
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
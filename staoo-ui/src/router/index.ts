import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Layout from '../layouts/Layout.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard',
    children: [
      // 直接访问管理页面的重定向路由
      {
        path: 'process-template',
        name: 'SystemProcessTemplateRedirect',
        meta: {
          title: '流程模板(重定向)',
          hidden: true
        },
        redirect: '/flow/process-template'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        meta: {
          title: '首页',
          icon: 'Home'
        },
        component: () => import('../modules/dashboard/Dashboard.vue')
      },
      {
        path: 'system',
        name: 'System',
        meta: {
          title: '系统管理',
          icon: 'Setting'
        },
        component: () => import('../modules/system/index.vue'),
        children: [
          {
            path: 'user',
            name: 'User',
            meta: {
              title: '用户管理'
            },
            component: () => import('../modules/system/user/UserList.vue')
          },
          {
            path: 'role',
            name: 'Role',
            meta: {
              title: '角色管理'
            },
            component: () => import('../modules/system/role/RoleList.vue')
          },
          {
            path: 'department',
            name: 'Department',
            meta: {
              title: '部门管理'
            },
            component: () => import('../modules/system/department/DepartmentList.vue')
          },
          {
            path: 'menu',
            name: 'Menu',
            meta: {
              title: '菜单管理'
            },
            component: () => import('../modules/system/menu/MenuList.vue')
          }
        ]
      }
    ]
  },
  
  // 流程管理模块
  {
    path: '/flow',
    component: Layout,
    redirect: '/flow/process-template',
    name: 'Flow',
    meta: {
      title: '流程管理',
      icon: 'flow-chart',
      roles: ['admin', 'flow:manage']
    },
    children: [
      // 流程模板管理
      {
        path: 'process-template',
        component: () => import('../modules/flow/process-template/ProcessTemplateList.vue'),
        name: 'ProcessTemplate',
        meta: {
          title: '流程模板',
          icon: 'template',
          roles: ['admin', 'flow:process-template:list']
        }
      },
      {
        path: 'process-template/edit',
        component: () => import('../modules/flow/process-template/ProcessTemplateEdit.vue'),
        name: 'ProcessTemplateEdit',
        meta: {
          title: '流程模板编辑',
          roles: ['admin', 'flow:process-template:edit'],
          hidden: true
        }
      },

      // 表单模板管理
      {
        path: 'form-template',
        component: () => import('../modules/flow/form-template/FormTemplateList.vue'),
        name: 'FormTemplate',
        meta: {
          title: '表单模板',
          icon: 'form',
          roles: ['admin', 'flow:form-template:list']
        }
      },
      {
        path: 'form-template/edit',
        component: () => import('../modules/flow/form-template/FormTemplateEdit.vue'),
        name: 'FormTemplateEdit',
        meta: {
          title: '表单模板编辑',
          roles: ['admin', 'flow:form-template:edit'],
          hidden: true
        }
      }
    ]
  },

  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录'
    },
    component: () => import('../modules/login/Login.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../modules/error/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to: any, _from: any, next: any) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - Staoo Admin`
  }

  // 登录拦截
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router

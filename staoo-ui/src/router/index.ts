import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Layout from '../layouts/Layout.vue'
import { useUserStore, useSystemStore } from '../store'
import { storeToRefs } from 'pinia'

// 基础路由（不涉及权限控制的路由）
const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录',
      hidden: true
    },
    component: () => import('../modules/login/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard',
    meta: {
      hidden: true
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        meta: {
          title: '首页',
          icon: 'Home'
        },
        component: () => import('../modules/dashboard/Dashboard.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    meta: {
      title: '页面不存在',
      hidden: true
    },
    component: () => import('../modules/error/NotFound.vue')
  }
]

// 初始化路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes
})

// 重置路由
export const resetRouter = () => {
  // 清空所有动态路由
  router.getRoutes().forEach(route => {
    if (!constantRoutes.find(cr => cr.path === route.path && cr.name === route.name)) {
      router.removeRoute(route.name || '')
    }
  })
  // 重新添加常量路由
  constantRoutes.forEach(route => {
    try {
      router.addRoute(route)
    } catch (e) {
      // 忽略已存在的路由
    }
  })
}

// 路由守卫
router.beforeEach(async (to: any, _from: any, next: any) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - Staoo Admin`
  }

  const userStore = useUserStore()
  const systemStore = useSystemStore()
  const { isLoggedIn } = storeToRefs(userStore)
  const { accessedRoutes } = storeToRefs(systemStore)
  
  // 已经登录但要去登录页，重定向到首页
  if (isLoggedIn.value && to.path === '/login') {
    next('/dashboard')
    return
  }

  // 登录拦截
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
    return
  }

  // 如果已经登录但还没有加载路由
  if (isLoggedIn.value && accessedRoutes.value.length === 0) {
    try {
      // 加载用户信息
      await userStore.getUserInfo()
      
      // 加载动态路由
      await systemStore.loadMenuData()
      
      // 重新跳转，确保路由已加载
      next({ ...to, replace: true })
      return
    } catch (error) {
      console.error('加载用户信息或路由失败:', error)
      userStore.logout()
      next('/login')
      return
    }
  }

  next()
})

export default router

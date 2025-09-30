import { createPinia, defineStore } from 'pinia'
import type { UserInfo } from '../types'
import { getUserInfo } from '../services/authService'
import type { MenuItem } from '../services/menuService'
import { getMenuList } from '../services/menuService'
import router from '../router'
import type { RouteRecordRaw } from 'vue-router'

// 创建Pinia实例
const pinia = createPinia()

// 用户信息Store
export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: {
      id: 0,
      username: '',
      nickname: '',
      avatar: '',
      roles: [] as string[],
      permissions: [] as string[]
    } as UserInfo,
    token: '',
    isLoggedIn: false
  }),
  
  getters: {
    hasPermission: (state) => (permission: string) => {
      return state.userInfo.permissions.includes(permission)
    }
  },
  
  actions: {
    setUserInfo(userInfo: any) {
      this.userInfo = userInfo
      this.isLoggedIn = true
    },
    
    setToken(token: string) {
      this.token = token
      localStorage.setItem('token', token)
    },

    setRefreshToken(refreshToken: string) {
      localStorage.setItem('refreshToken', refreshToken)
    },

    getRefreshToken(): string {
      return localStorage.getItem('refreshToken') || ''
    },

    removeRefreshToken() {
      localStorage.removeItem('refreshToken')
    },
    
    logout() {
      this.userInfo = {
        id: 0,
        username: '',
        nickname: '',
        avatar: '',
        roles: [],
        permissions: []
      }
      this.token = ''
      this.isLoggedIn = false
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      this.removeRefreshToken()
    },
    
    loadToken() {
      const token = localStorage.getItem('token')
      if (token) {
        this.token = token
        // 这里应该调用API获取用户信息
        this.getUserInfo()
      }
    },
    
    async getUserInfo() {
      try {
        // 尝试从本地存储获取用户信息
        const storedUserInfo = localStorage.getItem('userInfo')
        if (storedUserInfo) {
          try {
            const userInfo = JSON.parse(storedUserInfo)
            this.setUserInfo(userInfo)
            return
          } catch (e) {
            console.error('解析本地用户信息失败:', e)
          }
        }
        
        // 调用真实的获取用户信息API
        try {
          const response = await getUserInfo()
          if (response.code === 200 && response.data) {
            this.setUserInfo(response.data)
          }
        } catch (error) {
          console.error('调用API获取用户信息失败，使用模拟数据:', error)
          // 使用模拟数据
          const mockUserInfo = {
            id: 1,
            username: 'admin',
            nickname: '管理员',
            avatar: '',
            roles: ['admin'],
            permissions: ['*:*:*']
          }
          this.setUserInfo(mockUserInfo)
          localStorage.setItem('userInfo', JSON.stringify(mockUserInfo))
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        this.logout()
      }
    }
  }
})

// 系统配置Store
export const useSystemStore = defineStore('system', {
  state: () => ({
    sidebarCollapsed: false,
    theme: 'light',
    breadcrumbList: [] as Array<{ path: string; name: string }>,
    loading: false,
    // 动态菜单/路由相关状态
    menuList: [] as MenuItem[],
    accessedRoutes: [] as MenuItem[]
  }),
  
  actions: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    
    setTheme(theme: string) {
      this.theme = theme
    },
    
    setBreadcrumbList(list: Array<{ path: string; name: string }>) {
      this.breadcrumbList = list
    },
    
    setLoading(loading: boolean) {
      this.loading = loading
    },
    
    /**
     * 设置菜单列表
     */
    setMenuList(menuList: MenuItem[]) {
      this.menuList = menuList
    },
    
    /**
     * 设置可访问的路由列表
     */
    setAccessedRoutes(routes: MenuItem[]) {
      this.accessedRoutes = routes
    },
    
    /**
     * 加载菜单/路由数据
     */
    async loadMenuData() {
      try {
        this.setLoading(true)
        const result = await getMenuList()
        
        // 设置菜单列表
        this.setMenuList(result.menuList)
        
        // 设置用户权限
        const userStore = useUserStore()
        if (result.permissions && result.permissions.length > 0) {
          userStore.userInfo.permissions = result.permissions
        }
        
        // 生成可访问的路由
        const accessedRoutes = this.generateRoutes(result.menuList)
        this.setAccessedRoutes(accessedRoutes)
        // 动态添加路由到router
        this.addRoutesToRouter(accessedRoutes)
        
        return accessedRoutes
      } catch (error) {
        console.error('加载菜单数据失败:', error)
        throw error
      } finally {
        this.setLoading(false)
      }
    },
    
    /**
     * 根据菜单列表生成路由配置
     */
    generateRoutes(menuList: MenuItem[]) {
      const userStore = useUserStore()
      // 根据用户权限过滤路由
      return this.filterRoutes(menuList, userStore.userInfo.roles || [])
    },
    
    /**
     * 根据用户角色过滤路由
     */
    filterRoutes(routes: MenuItem[], roles: string[]) {
      const result: MenuItem[] = []
      
      routes.forEach(route => {
        // 如果路由有角色限制，但用户没有该角色，则跳过
        if (route.meta?.roles && route.meta.roles.length > 0) {
          const hasPermission = roles.some(role => route.meta?.roles?.includes(role))
          if (!hasPermission) {
            return
          }
        }
        
        const newRoute = { ...route }
        
        // 递归过滤子路由
        if (route.children && route.children.length > 0) {
          newRoute.children = this.filterRoutes(route.children, roles)
          // 如果子路由都被过滤掉了，且不是直接访问的路由，则跳过
          if (newRoute.children.length === 0 && !route.path.includes('/')) {
            return
          }
        }
        
        result.push(newRoute)
      })
      
      return result
    },
    
    /**
     * 将生成的路由添加到router
     */
    addRoutesToRouter(routes: MenuItem[]) {
      const Layout = () => import('../layouts/Layout.vue')
      
      // 转换菜单数据为路由配置
      const convertToRouteConfig = (menu: MenuItem): RouteRecordRaw => {
        // 对于Vite的动态导入，我们根据项目的实际模块结构进行适配
        // 项目组件位于modules目录下，按功能模块组织
        const getComponent = (componentName: string): () => Promise<any> => {
          try {
            // 根据项目结构，组件位于modules目录下
            // 这里需要根据实际的模块和组件名称进行路径映射
            // 我们假设组件名称遵循一定的约定，可以映射到对应的模块路径
            return () => {
              // 默认尝试从模块目录导入
                // 支持嵌套模块路径，如system/role
                if (componentName.includes('/')) {
                  // 如果组件名称已经包含路径分隔符，则直接使用
                  return import(/* @vite-ignore */ `/src/modules/${componentName.toLowerCase()}.vue`)
                } else {
                  // 尝试在对应模块目录下查找index.vue
                  try {
                    return import(/* @vite-ignore */ `/src/modules/${componentName.toLowerCase()}/index.vue`)
                  } catch (e) {
                    // 如果找不到index.vue，则尝试直接导入组件
                    return import(/* @vite-ignore */ `/src/modules/${componentName.toLowerCase()}.vue`)
                  }
                }
            }
          } catch (error) {
            console.error(`Failed to load component: ${componentName}`, error)
            // 提供一个默认组件
            return () => import('../modules/error/NotFound.vue')
          }
        }
        
        const routeConfig: RouteRecordRaw = {
          path: menu.path.startsWith('/') ? menu.path : `/${menu.path}`,
          name: menu.name,
          meta: menu.meta,
          component: menu.component ? getComponent(menu.component) : Layout,
          children: menu.children ? menu.children.map(child => convertToRouteConfig(child)) : []
        }
        
        if (menu.redirect) {
          routeConfig['redirect'] = menu.redirect
        }
        
        return routeConfig
      }
      
      // 添加路由到router前先检查路由是否存在
      routes.forEach(route => {
        try {
          const routeConfig = convertToRouteConfig(route)
          
          // 检查路由是否已存在（不使用parent属性，避免TypeScript错误）
          // 使用名称和路径组合来确定路由唯一性
          const existingRoute = router.getRoutes().find(r => 
            r.path === routeConfig.path && r.name === routeConfig.name
          )
          
          // 只有在路由不存在时才添加
          if (!existingRoute) {
            router.addRoute('Layout', routeConfig)
          }
        } catch (error) {
          console.error(`Failed to add route: ${route.name || route.path}`, error)
          // 继续处理下一个路由，避免因单个路由错误影响整体功能
        }
      })
    }
  }
})

export default pinia
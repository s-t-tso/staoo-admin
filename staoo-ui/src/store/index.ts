import { createPinia, defineStore } from 'pinia'
import type { UserInfo } from '../types'
import { getUserInfo } from '../services/authService'

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
        const response = await getUserInfo()
        if (response.code === 200 && response.data) {
          this.setUserInfo(response.data)
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
    loading: false
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
    }
  }
})

export default pinia
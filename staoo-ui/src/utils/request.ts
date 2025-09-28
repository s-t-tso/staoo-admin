import axios from 'axios'
import { useUserStore } from '../store'
import { refreshToken } from '../services/authService'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    // 添加token
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 是否正在刷新token的标志
let isRefreshing = false
// 存储待重发的请求队列
let requestQueue: Array<{ resolve: Function; reject: Function }> = []

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    
    // 统一处理响应数据
    if (res.code === 200) {
      return res.data
    } else {
      // 处理业务错误
      console.error('业务错误:', res.msg || '未知错误')
      return Promise.reject(new Error(res.msg || '未知错误'))
    }
  },
  async (error) => {
    console.error('响应错误:', error)
    
    // 处理HTTP错误
    if (error.response) {
      const status = error.response.status
      switch (status) {
        case 401:
          // 未授权，尝试刷新token
          const userStore = useUserStore()
          const config = error.config
    
          // 如果没有refreshToken或者请求的是登录/刷新token接口，则直接跳转到登录页
          if (!localStorage.getItem('refreshToken') || config.url?.includes('/auth/')) {
            userStore.logout()
            window.location.href = '/login'
            return Promise.reject(error)
          }
    
          // 如果不是正在刷新token的状态
          if (!isRefreshing) {
            isRefreshing = true
            try {
              // 调用刷新token接口
              const refreshTokenRes = await refreshToken(localStorage.getItem('refreshToken') || '')
              
              // 更新token
              userStore.setToken(refreshTokenRes.accessToken)
              
              // 重放所有队列中的请求
              requestQueue.forEach(({ resolve }) => {
                resolve(refreshTokenRes.accessToken)
              })
              requestQueue = []
              
              // 重新发起当前请求
              config.headers.Authorization = `Bearer ${refreshTokenRes.accessToken}`
              return service(config)
            } catch (refreshError) {
              // 刷新token失败，跳转到登录页
              console.error('刷新token失败:', refreshError)
              userStore.logout()
              window.location.href = '/login'
              return Promise.reject(refreshError)
            } finally {
              isRefreshing = false
            }
          } else {
            // 如果正在刷新token，则将当前请求加入队列
            return new Promise((resolve, reject) => {
              requestQueue.push({ resolve, reject })
            }).then((token) => {
              // 当刷新token成功后，重新设置请求的token并发起请求
              config.headers.Authorization = `Bearer ${token}`
              return service(config)
            }).catch((err) => {
              return Promise.reject(err)
            })
          }
          break
        case 403:
          console.error('没有权限')
          break
        case 404:
          console.error('请求的资源不存在')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error('未知错误')
      }
    }
    
    return Promise.reject(error)
  }
)

// 导出常用的请求方法
export const request = {
  get: <T = any>(url: string, params?: any): Promise<T> => service.get(url, params),
  post: <T = any>(url: string, data?: any, params?: any): Promise<T> => service.post(url, data, { params }),
  put: <T = any>(url: string, data?: any): Promise<T> => service.put(url, data),
  delete: <T = any>(url: string, params?: any): Promise<T> => service.delete(url, params),
  upload: <T = any>(url: string, file: File, params?: any): Promise<T> => {
    const formData = new FormData()
    formData.append('file', file)
    return service.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      params
    })
  }
}

export default service
import axios from 'axios'
import { useUserStore } from '../store'

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
  (error) => {
    console.error('响应错误:', error)
    
    // 处理HTTP错误
    if (error.response) {
      const status = error.response.status
      switch (status) {
        case 401:
          // 未授权，跳转到登录页
          const userStore = useUserStore()
          userStore.logout()
          window.location.href = '/login'
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
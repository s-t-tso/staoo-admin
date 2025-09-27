import { request } from '../../utils/request'

// 用户管理API服务
const userService = {
  // 获取用户列表
  getUserList: (params: any) => {
    return request.get('/system/user/list', { params })
  },
  
  // 获取用户详情
  getUserDetail: (id: string) => {
    return request.get(`/system/user/${id}`)
  },
  
  // 新增用户
  addUser: (data: any) => {
    return request.post('/system/user', data)
  },
  
  // 更新用户
  updateUser: (id: string, data: any) => {
    return request.put(`/system/user/${id}`, data)
  },
  
  // 删除用户
  deleteUser: (id: string) => {
    return request.delete(`/system/user/${id}`)
  },
  
  // 批量删除用户
  batchDeleteUsers: (ids: string[]) => {
    return request.delete('/system/user/batch', { data: { ids } })
  },
  
  // 启用/停用用户
  changeUserStatus: (id: string, status: number) => {
    return request.put(`/system/user/${id}/status`, { status })
  },
  
  // 重置密码
  resetPassword: (id: string, newPassword: string) => {
    return request.put(`/system/user/${id}/reset-password`, { newPassword })
  },
  
  // 导入用户
  importUsers: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.upload('/system/user/import', file)
  },
  
  // 导出用户
  exportUsers: (params: any) => {
    return request.get('/system/user/export', { params, responseType: 'blob' })
  }
}

export default userService
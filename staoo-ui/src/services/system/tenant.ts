import { request } from '../../utils/request'

// 租户管理API服务
const tenantService = {
  // 获取租户列表
  getTenantList: (params: any = {}) => {
    return request.get('/system/tenant/list', { params })
  },
  
  // 获取租户详情
  getTenantDetail: (id: string) => {
    return request.get(`/system/tenant/${id}`)
  },
  
  // 新增租户
  addTenant: (data: any) => {
    return request.post('/system/tenant', data)
  },
  
  // 更新租户
  updateTenant: (id: string, data: any) => {
    return request.put(`/system/tenant/${id}`, data)
  },
  
  // 删除租户
  deleteTenant: (id: string) => {
    return request.delete(`/system/tenant/${id}`)
  },
  
  // 批量删除租户
  batchDeleteTenants: (ids: string[]) => {
    return request.delete('/system/tenant/batch', { data: { ids } })
  },
  
  // 启用/停用租户
  changeTenantStatus: (id: string, status: number) => {
    return request.put(`/system/tenant/${id}/status`, { status })
  },
  
  // 检查租户名称是否重复
  checkTenantNameDuplicate: (tenantName: string, id: string = '') => {
    return request.get('/system/tenant/check-name', {
      params: { tenantName, id }
    })
  },
  
  // 获取租户下的用户列表
  getTenantUsers: (tenantId: string, params: any = {}) => {
    return request.get(`/system/tenant/${tenantId}/users`, { params })
  },
  
  // 租户分配用户
  assignTenantUsers: (tenantId: string, userIds: string[]) => {
    return request.put(`/system/tenant/${tenantId}/users`, { userIds })
  }
}

export default tenantService
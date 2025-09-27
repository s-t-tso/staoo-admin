import { request } from '../../utils/request'

// 部门管理API服务
const departmentService = {
  // 获取部门列表（树结构）
  getDepartmentList: (params: any = {}) => {
    return request.get('/system/department/list', { params })
  },
  
  // 获取部门详情
  getDepartmentDetail: (id: string) => {
    return request.get(`/system/department/${id}`)
  },
  
  // 新增部门
  addDepartment: (data: any) => {
    return request.post('/system/department', data)
  },
  
  // 更新部门
  updateDepartment: (id: string, data: any) => {
    return request.put(`/system/department/${id}`, data)
  },
  
  // 删除部门
  deleteDepartment: (id: string) => {
    return request.delete(`/system/department/${id}`)
  },
  
  // 批量删除部门
  batchDeleteDepartments: (ids: string[]) => {
    return request.delete('/system/department/batch', { data: { ids } })
  },
  
  // 获取部门树（用于选择）
  getDepartmentTree: (params: any = {}) => {
    return request.get('/system/department/tree', { params })
  },
  
  // 检查部门名称是否重复
  checkDepartmentNameDuplicate: (departmentName: string, parentId: string = '0', id: string = '') => {
    return request.get('/system/department/check-name', {
      params: { departmentName, parentId, id }
    })
  },
  
  // 获取部门下的用户列表
  getDepartmentUsers: (departmentId: string, params: any = {}) => {
    return request.get(`/system/department/${departmentId}/users`, { params })
  },
  
  // 移动部门（调整父部门）
  moveDepartment: (id: string, newParentId: string) => {
    return request.put(`/system/department/${id}/move`, { newParentId })
  }
}

export default departmentService
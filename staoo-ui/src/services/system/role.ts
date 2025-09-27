import { request } from '../../utils/request'

// 角色管理API服务
const roleService = {
  // 获取角色列表
  getRoleList: (params: any) => {
    return request.get('/system/role/list', { params })
  },
  
  // 获取角色详情
  getRoleDetail: (id: string) => {
    return request.get(`/system/role/${id}`)
  },
  
  // 新增角色
  addRole: (data: any) => {
    return request.post('/system/role', data)
  },
  
  // 更新角色
  updateRole: (id: string, data: any) => {
    return request.put(`/system/role/${id}`, data)
  },
  
  // 删除角色
  deleteRole: (id: string) => {
    return request.delete(`/system/role/${id}`)
  },
  
  // 批量删除角色
  batchDeleteRoles: (ids: string[]) => {
    return request.delete('/system/role/batch', { data: { ids } })
  },
  
  // 配置角色菜单权限
  configureRoleMenus: (roleId: string, menuIds: string[]) => {
    return request.put(`/system/role/${roleId}/menus`, { menuIds })
  },
  
  // 获取角色菜单权限
  getRoleMenus: (roleId: string) => {
    return request.get(`/system/role/${roleId}/menus`)
  },
  
  // 配置角色数据权限
  configureRoleDataPermission: (roleId: string, dataPermission: any) => {
    return request.put(`/system/role/${roleId}/data-permission`, dataPermission)
  },
  
  // 获取角色数据权限
  getRoleDataPermission: (roleId: string) => {
    return request.get(`/system/role/${roleId}/data-permission`)
  },
  
  // 关联角色用户
  associateRoleUsers: (roleId: string, userIds: string[]) => {
    return request.post(`/system/role/${roleId}/users`, { userIds })
  },
  
  // 获取角色关联用户列表
  getRoleUsers: (roleId: string, params: any) => {
    return request.get(`/system/role/${roleId}/users`, { params })
  }
}

export default roleService
import { request } from '../../utils/request'

// 菜单管理API服务
const menuService = {
  // 获取菜单列表（树结构）
  getMenuList: (params: any = {}) => {
    return request.get('/system/menu/list', { params })
  },
  
  // 获取菜单详情
  getMenuDetail: (id: string) => {
    return request.get(`/system/menu/${id}`)
  },
  
  // 新增菜单
  addMenu: (data: any) => {
    return request.post('/system/menu', data)
  },
  
  // 更新菜单
  updateMenu: (id: string, data: any) => {
    return request.put(`/system/menu/${id}`, data)
  },
  
  // 删除菜单
  deleteMenu: (id: string) => {
    return request.delete(`/system/menu/${id}`)
  },
  
  // 批量删除菜单
  batchDeleteMenus: (ids: string[]) => {
    return request.delete('/system/menu/batch', { data: { ids } })
  },
  
  // 获取菜单树（用于选择）
  getMenuTree: (params: any = {}) => {
    return request.get('/system/menu/tree', { params })
  },
  
  // 检查菜单名称是否重复
  checkMenuNameDuplicate: (menuName: string, parentId: string = '0', id: string = '') => {
    return request.get('/system/menu/check-name', {
      params: { menuName, parentId, id }
    })
  },
  
  // 更新菜单排序
  updateMenuOrder: (id: string, orderNum: number) => {
    return request.put(`/system/menu/${id}/order`, { orderNum })
  }
}

export default menuService
import { request } from '../../utils/request'

// 流程模板列表请求参数
export interface ProcessTemplateListParams {
  pageNum?: number
  pageSize?: number
  processName?: string
  processKey?: string
  status?: string
  tenantId: number
}

// 流程模板数据结构
export interface ProcessTemplate {
  id?: number
  processName: string
  processKey: string
  description?: string
  bpmnXml?: string
  status?: string
  version?: number
  tenantId?: number
  createTime?: string
  createBy?: string
  updateTime?: string
  updateBy?: string
}

// 复制流程模板参数
export interface CopyProcessTemplateParams {
  id: number
  newName?: string
}

// 获取流程模板列表
export const getProcessTemplateList = (params: ProcessTemplateListParams) => {
  return request.get('/api/flow/processTemplate/list', { params })
}

// 根据ID获取流程模板详情
export const getProcessTemplateById = (id: number) => {
  return request.get(`/api/flow/processTemplate/getById/${id}`)
}

// 根据流程标识获取流程模板详情
export const getProcessTemplateByKey = (processKey: string) => {
  return request.get(`/api/flow/processTemplate/getByKey/${processKey}`)
}

// 保存流程模板
export const saveProcessTemplate = (data: ProcessTemplate) => {
  return request.post('/api/flow/processTemplate/save', data)
}

// 更新流程模板
export const updateProcessTemplate = (data: ProcessTemplate & { id: number }) => {
  return request.put('/api/flow/processTemplate/update', data)
}

// 删除流程模板
export const deleteProcessTemplate = (id: number) => {
  return request.delete(`/api/flow/processTemplate/delete/${id}`)
}

// 批量删除流程模板
export const batchDeleteProcessTemplate = (ids: number[]) => {
  return request.delete('/api/flow/processTemplate/batchDelete', { data: ids })
}

// 发布流程模板
export const publishProcessTemplate = (id: number) => {
  return request.post(`/api/flow/processTemplate/publish/${id}`)
}

// 复制流程模板
export const copyProcessTemplate = (params: CopyProcessTemplateParams) => {
  return request.post(`/api/flow/processTemplate/copy/${params.id}`, params)
}
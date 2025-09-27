import { request } from '../../utils/request'

// 表单模板列表请求参数
export interface FormTemplateListParams {
  pageNum?: number
  pageSize?: number
  formName?: string
  formKey?: string
  status?: string
  tenantId: number
}

// 表单模板数据结构
export interface FormTemplate {
  id?: number
  formName: string
  formKey: string
  description?: string
  formConfig: string
  status?: string
  version?: number
  tenantId?: number
  createTime?: string
  createBy?: string
  updateTime?: string
  updateBy?: string
}

// 获取表单模板列表
export const getFormTemplateList = (params: FormTemplateListParams) => {
  return request.get('/api/flow/formTemplate/list', { params })
}

// 根据ID获取表单模板详情
export const getFormTemplateById = (id: number) => {
  return request.get(`/api/flow/formTemplate/getById/${id}`)
}

// 根据表单标识获取表单模板详情
export const getFormTemplateByKey = (formKey: string) => {
  return request.get(`/api/flow/formTemplate/getByKey/${formKey}`)
}

// 保存表单模板
export const saveFormTemplate = (data: FormTemplate) => {
  return request.post('/api/flow/formTemplate/save', data)
}

// 更新表单模板
export const updateFormTemplate = (data: FormTemplate & { id: number }) => {
  return request.put('/api/flow/formTemplate/update', data)
}

// 删除表单模板
export const deleteFormTemplate = (id: number) => {
  return request.delete(`/api/flow/formTemplate/delete/${id}`)
}

// 批量删除表单模板
export const batchDeleteFormTemplate = (ids: number[]) => {
  return request.delete('/api/flow/formTemplate/batchDelete', { data: ids })
}

// 发布表单模板
export const publishFormTemplate = (id: number) => {
  return request.post(`/api/flow/formTemplate/publish/${id}`)
}

// 复制表单模板
export const copyFormTemplate = (id: number) => {
  return request.post(`/api/flow/formTemplate/copy/${id}`)
}
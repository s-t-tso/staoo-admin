import { request } from '../../utils/request';
import type { ThirdPartyApp, ApiResponse } from '../../types';

/**
 * 第三方应用管理服务
 */
const thirdPartyAppService = {
  /**
   * 获取第三方应用列表
   */
  getThirdPartyAppList: async (params?: {
    pageNum?: number;
    pageSize?: number;
    appName?: string;
    status?: number;
  }): Promise<ApiResponse<{
    list: ThirdPartyApp[];
    total: number;
  }>> => {
    return request.get('/api/third-party/app/list', { params });
  },

  /**
   * 获取第三方应用详情
   */
  getThirdPartyAppDetail: async (id: number): Promise<ApiResponse<ThirdPartyApp>> => {
    return request.get(`/api/third-party/app/detail/${id}`);
  },

  /**
   * 创建第三方应用
   */
  createThirdPartyApp: async (data: Omit<ThirdPartyApp, 'id' | 'createTime' | 'updateTime'>): Promise<ApiResponse<ThirdPartyApp>> => {
    return request.post('/api/third-party/app/create', data);
  },

  /**
   * 更新第三方应用
   */
  updateThirdPartyApp: async (data: Omit<ThirdPartyApp, 'createTime'>): Promise<ApiResponse<ThirdPartyApp>> => {
    return request.put('/api/third-party/app/update', data);
  },

  /**
   * 删除第三方应用
   */
  deleteThirdPartyApp: async (id: number): Promise<ApiResponse<null>> => {
    return request.delete(`/api/third-party/app/delete/${id}`);
  },

  /**
   * 启用/禁用第三方应用
   */
  toggleThirdPartyAppStatus: async (id: number, status: number): Promise<ApiResponse<null>> => {
    return request.put(`/api/third-party/app/status/${id}`, { params: { status } });
  },

  /**
   * 生成新的App Key和App Secret
   */
  generateAppCredentials: async (id: number): Promise<ApiResponse<{ 
    appKey: string;
    appSecret: string;
  }>> => {
    return request.put(`/api/third-party/app/generate-credentials/${id}`);
  },

  /**
   * 获取所有可用的第三方应用（用于数据订阅选择）
   */
  getActiveThirdPartyApps: async (): Promise<ApiResponse<ThirdPartyApp[]>> => {
    return request.get('/api/third-party/app/active-list');
  }
};

export default thirdPartyAppService;
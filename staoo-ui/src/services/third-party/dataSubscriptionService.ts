import { request } from '../../utils/request';
import type { DataSubscription, ApiResponse } from '../../types';

/**
 * 数据订阅管理服务
 */
const dataSubscriptionService = {
  /**
   * 获取数据订阅列表
   */
  getDataSubscriptionList: async (params?: {
    pageNum?: number;
    pageSize?: number;
    appId?: number;
    dataType?: string;
    status?: number;
  }): Promise<ApiResponse<{
    list: DataSubscription[];
    total: number;
  }>> => {
    return request.get('/api/third-party/subscription/list', { params });
  },

  /**
   * 获取数据订阅详情
   */
  getDataSubscriptionDetail: async (id: number): Promise<ApiResponse<DataSubscription>> => {
    return request.get(`/api/third-party/subscription/detail/${id}`);
  },

  /**
   * 创建数据订阅
   */
  createDataSubscription: async (data: Omit<DataSubscription, 'id' | 'createTime' | 'updateTime'>): Promise<ApiResponse<DataSubscription>> => {
    return request.post('/api/third-party/subscription/create', data);
  },

  /**
   * 更新数据订阅
   */
  updateDataSubscription: async (data: Omit<DataSubscription, 'createTime'>): Promise<ApiResponse<DataSubscription>> => {
    return request.put('/api/third-party/subscription/update', data);
  },

  /**
   * 删除数据订阅
   */
  deleteDataSubscription: async (id: number): Promise<ApiResponse<null>> => {
    return request.delete(`/api/third-party/subscription/delete/${id}`);
  },

  /**
   * 启用/禁用数据订阅
   */
  toggleDataSubscriptionStatus: async (id: number, status: number): Promise<ApiResponse<null>> => {
    return request.put(`/api/third-party/subscription/status/${id}`, { params: { status } });
  },

  /**
   * 获取可订阅的数据类型列表
   */
  getAvailableDataTypes: async (): Promise<ApiResponse<Array<{ 
    value: string;
    label: string;
  }>>> => {
    return request.get('/api/third-party/subscription/data-types');
  }
};

export default dataSubscriptionService;
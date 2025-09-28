import { request } from '../utils/request';
import type { LoginRequest, LoginResponse, ApiResponse, UserInfo } from '../types';

/**
 * 获取客户端IP地址
 * @returns IP地址
 */
const getClientIp = (): string => {
  // 简化实现，实际项目中可能需要从请求头或其他方式获取真实IP
  return '127.0.0.1';
};

/**
 * 登录接口
 * @param params 登录参数
 * @returns 登录响应
 */
export const login = async (params: LoginRequest): Promise<LoginResponse> => {
  // 补充登录请求参数
  const loginParams = {
    ...params,
    loginType: params.loginType || 'PASSWORD',
    ip: params.ip || getClientIp(),
    userAgent: params.userAgent || navigator.userAgent
  };

  // 实际API调用代码
  const data = await request.post<LoginResponse>('/auth/login', loginParams);
  
  // 保存refreshToken到本地存储
  if (data.refreshToken) {
    localStorage.setItem('refreshToken', data.refreshToken);
  }
  
  return data;
};

/**
 * 刷新token接口
 * @param refreshToken 刷新令牌
 * @returns 登录响应（包含新的accessToken）
 */
export const refreshToken = async (refreshToken: string): Promise<LoginResponse> => {
  // 实际API调用代码
  const data = await request.post<LoginResponse>('/auth/refresh', { refreshToken });
  
  // 保存新的refreshToken
  if (data.refreshToken) {
    localStorage.setItem('refreshToken', data.refreshToken);
  }
  
  return data;
};

/**
 * 登出接口
 * @returns 登出响应
 */
export const logout = async (): Promise<ApiResponse> => {
  // 实际API调用代码
  const response = await request.post<ApiResponse>('/auth/logout');

  // 清除本地存储的token
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');

  // 由于request的响应拦截器已经处理了返回值，直接返回响应结果
  return response;

  // 保留模拟数据用于开发测试
  /*
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        message: '登出成功',
        data: null
      });
    }, 500);
  });
  */
};

/**
 * 获取用户信息接口
 * @returns 用户信息响应
 */
export const getUserInfo = async (): Promise<ApiResponse<UserInfo>> => {
  // 实际API调用代码
  const result = await request.get<UserInfo>('/auth/info');

  // 保存用户信息到本地存储
  if (typeof result === 'object' && result !== null) {
    localStorage.setItem('userInfo', JSON.stringify(result));
  }

  // 由于request的响应拦截器已经处理了返回值，这里需要调整类型处理
  return {
    code: 200,
    message: 'success',
    data: result
  };
};

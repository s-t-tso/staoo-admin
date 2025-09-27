// 用户信息类型
export interface UserInfo {
    id: number;
    username: string;
    nickname: string;
    avatar: string;
    tenantId?: number;
    tenantCode?: string;
    roles?: string[];
    permissions: string[];
    department?: Record<string, any>;
    extraInfo?: Record<string, any>;
}

// 登录请求类型
export interface LoginRequest {
    username: string;
    password: string;
    loginType?: string;
    ip?: string;
    userAgent?: string;
    deviceId?: string;
    customParams?: any;
}

// 登录响应类型
export interface LoginResponse {
    accessToken: string;
    refreshToken?: string;
    tokenType?: string;
    expiresIn?: number;
    userInfo: UserInfo;
}

// API响应通用类型
export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
}

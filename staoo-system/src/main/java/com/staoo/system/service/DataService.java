package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.system.pojo.request.OrganizationDataRequest;
import com.staoo.system.pojo.request.UserListDataRequest;
import com.staoo.system.pojo.response.OrganizationNode;
import com.staoo.system.pojo.response.UserResponse;

/**
 * 数据服务接口
 * 提供组织架构和用户数据的查询服务
 */
public interface DataService {
    
    /**
     * 获取组织架构数据
     * @param request 组织架构数据请求参数
     * @return 组织架构数据表格结果
     */
    TableResult<OrganizationNode> getOrganizationData(OrganizationDataRequest request);
    
    /**
     * 获取用户列表数据
     * @param request 用户列表数据请求参数
     * @return 用户列表数据表格结果
     */
    TableResult<UserResponse> getUserListData(UserListDataRequest request);
}
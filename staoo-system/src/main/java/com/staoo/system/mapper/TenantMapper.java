package com.staoo.system.mapper;

import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.Tenant;
import com.staoo.system.pojo.request.TenantQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 租户Mapper接口
 */
@Mapper
public interface TenantMapper {

    /**
     * 根据ID查询租户
     * @param id 租户ID
     * @return 租户信息
     */
    Tenant getById(Long id);

    /**
     * 根据租户名称查询租户
     * @param tenantName 租户名称
     * @return 租户信息
     */
    Tenant getByTenantName(String tenantName);

    /**
     * 查询租户列表
     * @param tenant 租户信息
     * @return 租户列表
     */
    List<Tenant> getList(Tenant tenant);

    /**
     * 分页查询租户
     * @param pageQuery 分页查询条件
     * @return 租户列表
     */
    List<Tenant> getPage(PageQuery pageQuery);

    /**
     * 新增租户
     * @param tenant 租户信息
     * @return 影响行数
     */
    int insert(Tenant tenant);

    /**
     * 修改租户
     * @param tenant 租户信息
     * @return 影响行数
     */
    int update(Tenant tenant);

    /**
     * 删除租户
     * @param id 租户ID
     * @return 影响行数
     */
    int delete(Long id);

    /**
     * 批量删除租户
     * @param ids 租户ID集合
     * @return 影响行数
     */
    int deleteBatch(Long[] ids);

    /**
     * 查询租户总数
     * @param tenant 租户信息
     * @return 租户总数
     */
    int count(Tenant tenant);
    
    /**
     * 根据请求参数查询租户列表
     * @param request 租户查询请求参数
     * @return 租户列表
     */
    List<Tenant> getListByRequest(TenantQueryRequest request);
}
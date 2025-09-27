package com.staoo.system.mapper;

import com.staoo.system.domain.ThirdPartyApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 第三方应用Mapper接口
 */
@Mapper
public interface ThirdPartyAppMapper {
    /**
     * 根据ID查询应用
     * @param id 应用ID
     * @return 应用信息
     */
    ThirdPartyApp getById(@Param("id") Long id);

    /**
     * 根据应用标识查询应用
     * @param appKey 应用标识
     * @return 应用信息
     */
    ThirdPartyApp getByAppKey(@Param("appKey") String appKey);

    /**
     * 查询应用列表
     * @param app 查询条件
     * @return 应用列表
     */
    List<ThirdPartyApp> getList(ThirdPartyApp app);

    /**
     * 分页查询应用
     * @param app 查询条件
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 应用列表
     */
    List<ThirdPartyApp> getPage(@Param("app") ThirdPartyApp app, @Param("offset") Long offset, @Param("limit") Long limit);

    /**
     * 查询总数
     * @param app 查询条件
     * @return 总数
     */
    Long getCount(ThirdPartyApp app);

    /**
     * 新增应用
     * @param app 应用信息
     * @return 影响行数
     */
    int insert(ThirdPartyApp app);

    /**
     * 更新应用
     * @param app 应用信息
     * @return 影响行数
     */
    int update(ThirdPartyApp app);

    /**
     * 删除应用
     * @param id 应用ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除应用
     * @param ids 应用ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 新增应用回调地址
     * @param appId 应用ID
     * @param callbackUrl 回调地址
     * @return 影响行数
     */
    int insertCallbackUrl(@Param("appId") Long appId, @Param("callbackUrl") String callbackUrl);

    /**
     * 删除应用回调地址
     * @param appId 应用ID
     * @return 影响行数
     */
    int deleteCallbackUrls(@Param("appId") Long appId);

    /**
     * 查询应用回调地址列表
     * @param appId 应用ID
     * @return 回调地址列表
     */
    List<String> getCallbackUrls(@Param("appId") Long appId);

    /**
     * 新增应用权限
     * @param appId 应用ID
     * @param permission 权限标识
     * @return 影响行数
     */
    int insertPermission(@Param("appId") Long appId, @Param("permission") String permission);

    /**
     * 删除应用权限
     * @param appId 应用ID
     * @return 影响行数
     */
    int deletePermissions(@Param("appId") Long appId);

    /**
     * 查询应用权限列表
     * @param appId 应用ID
     * @return 权限列表
     */
    List<String> getPermissions(@Param("appId") Long appId);

    /**
     * 新增应用租户关系
     * @param appId 应用ID
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int insertTenantRelation(@Param("appId") Long appId, @Param("tenantId") Long tenantId);

    /**
     * 删除应用租户关系
     * @param appId 应用ID
     * @return 影响行数
     */
    int deleteTenantRelations(@Param("appId") Long appId);

    /**
     * 查询应用可访问的租户ID列表
     * @param appId 应用ID
     * @return 租户ID列表
     */
    List<Long> getTenantIds(@Param("appId") Long appId);
}
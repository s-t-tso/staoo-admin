package com.staoo.system.service;

import com.staoo.common.domain.TableResult;
import com.staoo.common.domain.PageQuery;
import com.staoo.system.domain.ThirdPartyApp;

import java.util.List;

/**
 * 第三方应用Service接口
 */
public interface ThirdPartyAppService {
    /**
     * 根据ID查询应用
     * @param id 应用ID
     * @return 应用信息
     */
    ThirdPartyApp getById(Long id);

    /**
     * 根据应用标识查询应用
     * @param appKey 应用标识
     * @return 应用信息
     */
    ThirdPartyApp getByAppKey(String appKey);

    /**
     * 查询应用列表
     * @param app 查询条件
     * @return 应用列表
     */
    List<ThirdPartyApp> getList(ThirdPartyApp app);

    /**
     * 分页查询应用
     * @param app 查询条件
     * @param pageQuery 分页参数
     * @return 分页结果
     */
    TableResult<ThirdPartyApp> getPage(ThirdPartyApp app, PageQuery pageQuery);

    /**
     * 新增应用
     * @param app 应用信息
     * @return 应用ID
     */
    Long save(ThirdPartyApp app);

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
    int deleteById(Long id);

    /**
     * 批量删除应用
     * @param ids 应用ID列表
     * @return 影响行数
     */
    int deleteByIds(List<Long> ids);

    /**
     * 生成应用密钥
     * @return 应用密钥
     */
    String generateAppSecret();

    /**
     * 重置应用密钥
     * @param id 应用ID
     * @return 新的应用密钥
     */
    String resetAppSecret(Long id);

    /**
     * 启用/禁用应用
     * @param id 应用ID
     * @param status 状态（0：启用，1：禁用）
     * @return 影响行数
     */
    int changeStatus(Long id, String status);

    /**
     * 配置应用权限
     * @param appId 应用ID
     * @param permissions 权限列表
     */
    void configurePermissions(Long appId, List<String> permissions);

    /**
     * 配置应用回调地址
     * @param appId 应用ID
     * @param callbackUrls 回调地址列表
     */
    void configureCallbackUrls(Long appId, List<String> callbackUrls);

    /**
     * 配置应用租户访问权限
     * @param appId 应用ID
     * @param tenantIds 租户ID列表
     */
    void configureTenantAccess(Long appId, List<Long> tenantIds);

    /**
     * 检查应用是否有权限访问指定资源
     * @param appKey 应用标识
     * @param permission 权限标识
     * @return 是否有权限
     */
    boolean checkPermission(String appKey, String permission);

    /**
     * 检查应用是否可以访问指定租户
     * @param appKey 应用标识
     * @param tenantId 租户ID
     * @return 是否可以访问
     */
    boolean checkTenantAccess(String appKey, Long tenantId);
}
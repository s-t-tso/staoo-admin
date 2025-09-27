package com.staoo.flow.mapper;

import com.staoo.flow.domain.ProcessTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程模板Mapper接口
 * 提供流程模板相关的数据库操作方法
 */
@Mapper
public interface ProcessTemplateMapper {
    /**
     * 根据ID查询流程模板
     * @param id 模板ID
     * @return 流程模板信息
     */
    ProcessTemplate getById(@Param("id") Long id);

    /**
     * 根据流程标识查询流程模板
     * @param processKey 流程唯一标识
     * @param tenantId 租户ID
     * @return 流程模板信息
     */
    ProcessTemplate getByProcessKey(@Param("processKey") String processKey, @Param("tenantId") Long tenantId);

    /**
     * 查询流程模板列表
     * @param processTemplate 查询条件
     * @return 流程模板列表
     */
    List<ProcessTemplate> getList(ProcessTemplate processTemplate);

    /**
     * 新增流程模板
     * @param processTemplate 流程模板信息
     * @return 影响行数
     */
    int insert(ProcessTemplate processTemplate);

    /**
     * 更新流程模板
     * @param processTemplate 流程模板信息
     * @return 影响行数
     */
    int update(ProcessTemplate processTemplate);

    /**
     * 删除流程模板
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除流程模板
     * @param ids 模板ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 查询流程模板总数
     * @param processTemplate 查询条件
     * @return 流程模板总数
     */
    int getCount(ProcessTemplate processTemplate);

    /**
     * 根据租户ID查询所有流程模板
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 更新流程模板状态
     * @param id 模板ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 根据分类查询流程模板
     * @param category 流程分类
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByCategory(@Param("category") String category, @Param("tenantId") Long tenantId);

    /**
     * 根据表单标识查询关联的流程模板
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 流程模板列表
     */
    List<ProcessTemplate> getListByFormKey(@Param("formKey") String formKey, @Param("tenantId") Long tenantId);
}
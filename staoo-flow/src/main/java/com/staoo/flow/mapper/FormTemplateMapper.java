package com.staoo.flow.mapper;

import com.staoo.flow.domain.FormTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单模板Mapper接口
 * 提供表单模板相关的数据库操作方法
 */
@Mapper
public interface FormTemplateMapper {
    /**
     * 根据ID查询表单模板
     * @param id 模板ID
     * @return 表单模板信息
     */
    FormTemplate getById(@Param("id") Long id);

    /**
     * 根据表单标识查询表单模板
     * @param formKey 表单唯一标识
     * @param tenantId 租户ID
     * @return 表单模板信息
     */
    FormTemplate getByFormKey(@Param("formKey") String formKey, @Param("tenantId") Long tenantId);

    /**
     * 查询表单模板列表
     * @param formTemplate 查询条件
     * @return 表单模板列表
     */
    List<FormTemplate> getList(FormTemplate formTemplate);

    /**
     * 新增表单模板
     * @param formTemplate 表单模板信息
     * @return 影响行数
     */
    int insert(FormTemplate formTemplate);

    /**
     * 更新表单模板
     * @param formTemplate 表单模板信息
     * @return 影响行数
     */
    int update(FormTemplate formTemplate);

    /**
     * 删除表单模板
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除表单模板
     * @param ids 模板ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 查询表单模板总数
     * @param formTemplate 查询条件
     * @return 表单模板总数
     */
    int getCount(FormTemplate formTemplate);

    /**
     * 根据租户ID查询所有表单模板
     * @param tenantId 租户ID
     * @return 表单模板列表
     */
    List<FormTemplate> getListByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 更新表单模板状态
     * @param id 模板ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
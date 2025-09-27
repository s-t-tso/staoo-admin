package com.staoo.flow.mapper;

import com.staoo.flow.domain.FormData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单数据Mapper接口
 * 提供表单数据相关的数据库操作方法
 */
@Mapper
public interface FormDataMapper {
    /**
     * 根据ID查询表单数据
     * @param id 数据ID
     * @return 表单数据信息
     */
    FormData getById(@Param("id") Long id);

    /**
     * 根据业务键查询表单数据
     * @param businessKey 业务键
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单数据信息
     */
    FormData getByBusinessKey(@Param("businessKey") String businessKey, @Param("formKey") String formKey, @Param("tenantId") Long tenantId);

    /**
     * 查询表单数据列表
     * @param formData 查询条件
     * @return 表单数据列表
     */
    List<FormData> getList(FormData formData);

    /**
     * 新增表单数据
     * @param formData 表单数据信息
     * @return 影响行数
     */
    int insert(FormData formData);

    /**
     * 更新表单数据
     * @param formData 表单数据信息
     * @return 影响行数
     */
    int update(FormData formData);

    /**
     * 删除表单数据
     * @param id 数据ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除表单数据
     * @param ids 数据ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 查询表单数据总数
     * @param formData 查询条件
     * @return 表单数据总数
     */
    int getCount(FormData formData);

    /**
     * 根据表单标识查询表单数据
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单数据列表
     */
    List<FormData> getListByFormKey(@Param("formKey") String formKey, @Param("tenantId") Long tenantId);

    /**
     * 根据创建人ID查询表单数据
     * @param createBy 创建人ID
     * @param tenantId 租户ID
     * @return 表单数据列表
     */
    List<FormData> getListByCreateBy(@Param("createBy") Long createBy, @Param("tenantId") Long tenantId);

    /**
     * 更新表单数据状态
     * @param id 数据ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 根据流程实例ID查询表单数据
     * @param processInstanceId 流程实例ID
     * @param tenantId 租户ID
     * @return 表单数据信息
     */
    FormData getByProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("tenantId") Long tenantId);
}
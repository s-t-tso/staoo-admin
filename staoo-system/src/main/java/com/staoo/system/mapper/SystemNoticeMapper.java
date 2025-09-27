package com.staoo.system.mapper;

import com.staoo.system.domain.SystemNotice;
import com.staoo.system.pojo.request.SystemNoticeQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统通知Mapper接口
 * 定义系统通知的数据访问操作，支持批量删除功能
 */
@Mapper
public interface SystemNoticeMapper {
    /**
     * 根据ID查询系统通知
     * @param id 通知ID
     * @return 系统通知对象
     */
    SystemNotice selectById(Long id);

    /**
     * 查询系统通知列表
     * @param systemNotice 查询条件
     * @return 系统通知列表
     */
    List<SystemNotice> selectList(SystemNotice systemNotice);

    /**
     * 新增系统通知
     * @param systemNotice 系统通知对象
     * @return 影响行数
     */
    int insert(SystemNotice systemNotice);

    /**
     * 更新系统通知
     * @param systemNotice 系统通知对象
     * @return 影响行数
     */
    int update(SystemNotice systemNotice);

    /**
     * 根据ID删除系统通知
     * @param id 通知ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除系统通知
     * 支持批量删除功能，根据通知ID列表删除多条通知记录
     * @param ids 通知ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 批量更新系统通知状态
     * @param ids 通知ID列表
     * @param status 状态
     * @return 影响行数
     */
    int updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询系统通知总数
     * @param systemNotice 查询条件
     * @return 总数
     */
    Long selectCount(SystemNotice systemNotice);

    /**
     * 根据请求参数查询系统通知列表
     * @param request 查询请求
     * @return 系统通知列表
     */
    List<SystemNotice> selectListByRequest(SystemNoticeQueryRequest request);
}
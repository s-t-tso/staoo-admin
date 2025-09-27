package com.staoo.system.mapper;

import com.staoo.system.domain.DataSubscription;
import com.staoo.system.pojo.request.SubscriptionQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据订阅Mapper接口
 * 定义数据订阅的数据访问操作
 */
@Mapper
public interface DataSubscriptionMapper {
    /**
     * 根据ID查询数据订阅
     * @param id 订阅ID
     * @return 数据订阅对象
     */
    DataSubscription selectById(Long id);

    /**
     * 根据应用标识和数据类型查询订阅
     * @param appKey 应用标识
     * @param dataType 数据类型
     * @return 数据订阅对象
     */
    DataSubscription selectByAppKeyAndDataType(@Param("appKey") String appKey, @Param("dataType") String dataType);

    /**
     * 查询应用的所有订阅
     * @param appKey 应用标识
     * @return 订阅列表
     */
    List<DataSubscription> selectByAppKey(@Param("appKey") String appKey);

    /**
     * 查询指定数据类型的所有订阅
     * @param dataType 数据类型
     * @return 订阅列表
     */
    List<DataSubscription> selectByDataType(@Param("dataType") String dataType);

    /**
     * 新增数据订阅
     * @param dataSubscription 数据订阅对象
     * @return 影响行数
     */
    int insert(DataSubscription dataSubscription);

    /**
     * 更新数据订阅
     * @param dataSubscription 数据订阅对象
     * @return 影响行数
     */
    int update(DataSubscription dataSubscription);

    /**
     * 根据ID删除数据订阅
     * @param id 订阅ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据应用标识和数据类型删除订阅
     * @param appKey 应用标识
     * @param dataType 数据类型
     * @return 影响行数
     */
    int deleteByAppKeyAndDataType(@Param("appKey") String appKey, @Param("dataType") String dataType);

    /**
     * 更新订阅状态
     * @param id 订阅ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 根据请求参数查询数据订阅列表
     * @param request 查询请求参数
     * @return 数据订阅列表
     */
    List<DataSubscription> selectListByRequest(SubscriptionQueryRequest request);
}
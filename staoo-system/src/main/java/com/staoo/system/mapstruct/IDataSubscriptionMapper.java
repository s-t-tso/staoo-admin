package com.staoo.system.mapstruct;

import com.staoo.system.domain.DataSubscription;
import com.staoo.system.pojo.request.DataSubscriptionRequest;
import com.staoo.system.pojo.response.DataSubscriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 数据订阅实体转换接口
 * 用于DataSubscription实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDataSubscriptionMapper {

    /**
     * 将DataSubscriptionRequest转换为DataSubscription实体
     * @param request 数据订阅请求对象
     * @return 数据订阅实体
     */
    DataSubscription toEntity(DataSubscriptionRequest request);

    /**
     * 将DataSubscription实体转换为DataSubscriptionResponse
     * @param subscription 数据订阅实体
     * @return 数据订阅响应对象
     */
    DataSubscriptionResponse toResponse(DataSubscription subscription);

    /**
     * 将DataSubscriptionRequest更新到DataSubscription实体
     * @param request 数据订阅请求对象
     * @param subscription 数据订阅实体
     */
    void updateEntity(DataSubscriptionRequest request, @MappingTarget DataSubscription subscription);

    /**
     * 将DataSubscription实体列表转换为DataSubscriptionResponse列表
     * @param subscriptions 数据订阅实体列表
     * @return 数据订阅响应对象列表
     */
    List<DataSubscriptionResponse> toResponseList(List<DataSubscription> subscriptions);
}
package com.staoo.system.mapstruct;

import com.staoo.system.domain.ThirdPartyApp;
import com.staoo.system.pojo.request.ThirdPartyAppRequest;
import com.staoo.system.pojo.response.ThirdPartyAppResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 第三方应用实体转换接口
 * 用于ThirdPartyApp实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IThirdPartyAppMapper {

    /**
     * 将ThirdPartyAppRequest转换为ThirdPartyApp实体
     * @param request 第三方应用请求对象
     * @return 第三方应用实体
     */
    ThirdPartyApp toEntity(ThirdPartyAppRequest request);

    /**
     * 将ThirdPartyApp实体转换为ThirdPartyAppResponse
     * @param app 第三方应用实体
     * @return 第三方应用响应对象
     */
    ThirdPartyAppResponse toResponse(ThirdPartyApp app);

    /**
     * 将ThirdPartyAppRequest更新到ThirdPartyApp实体
     * @param request 第三方应用请求对象
     * @param app 第三方应用实体
     */
    void updateEntity(ThirdPartyAppRequest request, @MappingTarget ThirdPartyApp app);

    /**
     * 将ThirdPartyApp实体列表转换为ThirdPartyAppResponse列表
     * @param apps 第三方应用实体列表
     * @return 第三方应用响应对象列表
     */
    List<ThirdPartyAppResponse> toResponseList(List<ThirdPartyApp> apps);
}
package com.staoo.system.mapstruct;

import com.staoo.system.domain.UserTenant;
import com.staoo.system.pojo.request.UserTenantRequest;
import com.staoo.system.pojo.response.UserTenantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 用户-租户关联实体转换接口
 * 用于UserTenant实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserTenantMapper {

    /**
     * 将UserTenantRequest转换为UserTenant实体
     * @param request 用户-租户关联请求对象
     * @return 用户-租户关联实体
     */
    UserTenant toEntity(UserTenantRequest request);

    /**
     * 将UserTenant实体转换为UserTenantResponse
     * @param userTenant 用户-租户关联实体
     * @return 用户-租户关联响应对象
     */
    UserTenantResponse toResponse(UserTenant userTenant);

    /**
     * 将UserTenantRequest更新到UserTenant实体
     * @param request 用户-租户关联请求对象
     * @param userTenant 用户-租户关联实体
     */
    void updateEntity(UserTenantRequest request, @MappingTarget UserTenant userTenant);

    /**
     * 将UserTenant实体列表转换为UserTenantResponse列表
     * @param userTenants 用户-租户关联实体列表
     * @return 用户-租户关联响应对象列表
     */
    List<UserTenantResponse> toResponseList(List<UserTenant> userTenants);
}
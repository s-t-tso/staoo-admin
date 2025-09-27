package com.staoo.system.mapstruct;

import com.staoo.system.domain.Tenant;
import com.staoo.system.pojo.request.TenantRequest;
import com.staoo.system.pojo.response.TenantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 租户实体转换接口
 * 用于Tenant实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITenantMapper {

    /**
     * 将TenantRequest转换为Tenant实体
     * @param request 租户请求对象
     * @return 租户实体
     */
    Tenant toEntity(TenantRequest request);

    /**
     * 将Tenant实体转换为TenantResponse
     * @param tenant 租户实体
     * @return 租户响应对象
     */
    TenantResponse toResponse(Tenant tenant);

    /**
     * 更新Tenant实体
     * @param request 租户请求对象
     * @param tenant 要更新的租户实体
     */
    void updateEntity(TenantRequest request, @MappingTarget Tenant tenant);

    /**
     * 将Tenant列表转换为TenantResponse列表
     * @param tenants 租户实体列表
     * @return 租户响应对象列表
     */
    List<TenantResponse> toResponseList(List<Tenant> tenants);
}
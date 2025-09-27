package com.staoo.system.mapstruct;

import com.staoo.system.domain.Role;
import com.staoo.system.pojo.request.RoleRequest;
import com.staoo.system.pojo.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 角色实体转换接口
 * 用于Role实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleMapper {
    
    /**
     * 将RoleRequest转换为Role实体
     * @param request 角色请求对象
     * @return 角色实体
     */
    Role toEntity(RoleRequest request);
    
    /**
     * 将Role实体转换为RoleResponse
     * @param role 角色实体
     * @return 角色响应对象
     */
    RoleResponse toResponse(Role role);
    
    /**
     * 将RoleRequest更新到Role实体
     * @param request 角色请求对象
     * @param role 角色实体
     */
    void updateEntity(RoleRequest request, @MappingTarget Role role);
    
    /**
     * 将Role实体列表转换为RoleResponse列表
     * @param roles 角色实体列表
     * @return 角色响应对象列表
     */
    List<RoleResponse> toResponseList(List<Role> roles);
}
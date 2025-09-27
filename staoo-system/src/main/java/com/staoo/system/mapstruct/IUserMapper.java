package com.staoo.system.mapstruct;

import com.staoo.system.domain.User;
import com.staoo.system.pojo.request.UserRequest;
import com.staoo.system.pojo.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 用户实体转换接口
 * 用于User实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    
    /**
     * 将UserRequest转换为User实体
     * @param request 用户请求对象
     * @return 用户实体
     */
    User toEntity(UserRequest request);
    
    /**
     * 将User实体转换为UserResponse
     * @param user 用户实体
     * @return 用户响应对象
     */
    UserResponse toResponse(User user);
    
    /**
     * 将UserRequest更新到User实体
     * @param request 用户请求对象
     * @param user 用户实体
     */
    void updateEntity(UserRequest request, @MappingTarget User user);
    
    /**
     * 将User实体列表转换为UserResponse列表
     * @param users 用户实体列表
     * @return 用户响应对象列表
     */
    List<UserResponse> toResponseList(List<User> users);
}
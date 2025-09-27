package com.staoo.system.mapstruct;

import com.staoo.system.domain.Menu;
import com.staoo.system.pojo.request.MenuRequest;
import com.staoo.system.pojo.response.MenuResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 菜单实体转换接口
 * 用于Menu实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMenuMapper {
    
    /**
     * 将MenuRequest转换为Menu实体
     * @param request 菜单请求对象
     * @return 菜单实体
     */
    Menu toEntity(MenuRequest request);
    
    /**
     * 将Menu实体转换为MenuResponse
     * @param menu 菜单实体
     * @return 菜单响应对象
     */
    MenuResponse toResponse(Menu menu);
    
    /**
     * 将MenuRequest更新到Menu实体
     * @param request 菜单请求对象
     * @param menu 菜单实体
     */
    void updateEntity(MenuRequest request, @MappingTarget Menu menu);
    
    /**
     * 将Menu实体列表转换为MenuResponse列表
     * @param menus 菜单实体列表
     * @return 菜单响应对象列表
     */
    List<MenuResponse> toResponseList(List<Menu> menus);
}
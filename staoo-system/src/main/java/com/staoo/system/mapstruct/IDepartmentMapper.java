package com.staoo.system.mapstruct;

import com.staoo.system.domain.Department;
import com.staoo.system.pojo.request.DepartmentRequest;
import com.staoo.system.pojo.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 部门实体转换接口
 * 用于Department实体与请求/响应对象之间的转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDepartmentMapper {

    /**
     * 将DepartmentRequest转换为Department实体
     * @param request 部门请求对象
     * @return 部门实体
     */
    Department toEntity(DepartmentRequest request);

    /**
     * 将Department实体转换为DepartmentResponse
     * @param department 部门实体
     * @return 部门响应对象
     */
    DepartmentResponse toResponse(Department department);

    /**
     * 更新Department实体
     * @param request 部门请求对象
     * @param department 要更新的部门实体
     */
    void updateEntity(DepartmentRequest request, @MappingTarget Department department);

    /**
     * 将Department列表转换为DepartmentResponse列表
     * @param departments 部门实体列表
     * @return 部门响应对象列表
     */
    List<DepartmentResponse> toResponseList(List<Department> departments);
}
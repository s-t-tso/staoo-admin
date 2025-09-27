package com.staoo.flow.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.staoo.flow.domain.ProcessTemplate;
import com.staoo.flow.pojo.request.ProcessTemplateRequest;
import com.staoo.flow.pojo.response.ProcessTemplateResponse;

/**
 * 流程模板转换器
 * 实现流程模板相关实体与DTO之间的转换
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProcessTemplateConverter {
    
    /**
     * 将请求类转换为实体类
     * @param request 流程模板请求类
     * @return 流程模板实体类
     */
    ProcessTemplate toEntity(ProcessTemplateRequest request);
    
    /**
     * 将实体类转换为响应类
     * @param entity 流程模板实体类
     * @return 流程模板响应类
     */
    @Mapping(target = "statusDesc", expression = "java(entity.getStatus() != null ? com.staoo.flow.enums.StatusEnum.getByCode(entity.getStatus()).getDesc() : null)")
    ProcessTemplateResponse toResponse(ProcessTemplate entity);
    
    /**
     * 将实体类列表转换为响应类列表
     * @param entities 流程模板实体类列表
     * @return 流程模板响应类列表
     */
    List<ProcessTemplateResponse> toResponseList(List<ProcessTemplate> entities);
}
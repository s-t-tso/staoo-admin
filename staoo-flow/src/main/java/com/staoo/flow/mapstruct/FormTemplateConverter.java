package com.staoo.flow.mapstruct;

import com.staoo.flow.domain.FormTemplate;
import com.staoo.flow.enums.StatusEnum;
import com.staoo.flow.pojo.request.FormTemplateRequest;
import com.staoo.flow.pojo.response.FormTemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 表单模板转换器
 * 实现表单模板相关实体与DTO之间的转换
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FormTemplateConverter {
    
    /**
     * 将请求类转换为实体类
     * @param request 表单模板请求类
     * @return 表单模板实体类
     */
    FormTemplate toEntity(FormTemplateRequest request);
    
    /**
     * 将实体类转换为响应类
     * @param entity 表单模板实体类
     * @return 表单模板响应类
     */
    @Mapping(target = "statusDesc", expression = "java(entity.getStatus() != null ? com.staoo.flow.enums.StatusEnum.getByCode(entity.getStatus()).getDesc() : null)")
    FormTemplateResponse toResponse(FormTemplate entity);
    
    /**
     * 将实体类列表转换为响应类列表
     * @param entities 表单模板实体类列表
     * @return 表单模板响应类列表
     */
    List<FormTemplateResponse> toResponseList(List<FormTemplate> entities);
}
package com.staoo.flow.mapstruct;

import com.staoo.flow.domain.FormData;
import com.staoo.flow.pojo.request.FormDataRequest;
import com.staoo.flow.pojo.response.FormDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 表单数据转换器
 * 实现表单数据相关实体与DTO之间的转换
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FormDataConverter {
    
    /**
     * 将请求类转换为实体类
     * @param request 表单数据请求类
     * @return 表单数据实体类
     */
    FormData toEntity(FormDataRequest request);
    
    /**
     * 将实体类转换为响应类
     * @param entity 表单数据实体类
     * @return 表单数据响应类
     */
    @Mapping(target = "statusDesc", expression = "java(entity.getStatus() != null ? com.staoo.flow.enums.StatusEnum.getByCode(entity.getStatus()).getDesc() : null)")
    FormDataResponse toResponse(FormData entity);
    
    /**
     * 将实体类列表转换为响应类列表
     * @param entities 表单数据实体类列表
     * @return 表单数据响应类列表
     */
    List<FormDataResponse> toResponseList(List<FormData> entities);
}
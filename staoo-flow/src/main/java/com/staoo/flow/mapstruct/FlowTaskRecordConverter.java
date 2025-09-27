package com.staoo.flow.mapstruct;

import com.staoo.flow.domain.FlowTaskRecord;
import com.staoo.flow.enums.StatusEnum;
import com.staoo.flow.pojo.request.FlowTaskRecordRequest;
import com.staoo.flow.pojo.response.FlowTaskRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 流程任务记录转换器
 * 实现流程任务记录相关实体与DTO之间的转换
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FlowTaskRecordConverter {
    
    /**
     * 将请求类转换为实体类
     * @param request 流程任务记录请求类
     * @return 流程任务记录实体类
     */
    FlowTaskRecord toEntity(FlowTaskRecordRequest request);
    
    /**
     * 将实体类转换为响应类
     * @param entity 流程任务记录实体类
     * @return 流程任务记录响应类
     */
    @Mapping(target = "actionDesc", expression = "java(entity.getAction() != null ? com.staoo.flow.enums.StatusEnum.getByCode(entity.getAction()).getDesc() : null)")
    FlowTaskRecordResponse toResponse(FlowTaskRecord entity);
    
    /**
     * 将实体类列表转换为响应类列表
     * @param entities 流程任务记录实体类列表
     * @return 流程任务记录响应类列表
     */
    List<FlowTaskRecordResponse> toResponseList(List<FlowTaskRecord> entities);
}
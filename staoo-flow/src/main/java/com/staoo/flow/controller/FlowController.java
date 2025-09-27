package com.staoo.flow.controller;

import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.flow.pojo.request.StartProcessRequest;
import com.staoo.flow.pojo.request.TriggerFlowEventRequest;
import com.staoo.flow.pojo.response.StartProcessResponse;
import com.staoo.flow.pojo.response.TriggerFlowEventResponse;

/**
 * 动态流程接口控制器
 * 提供流程启动和管理相关的API接口
 */
@RestController
@RequestMapping("/api/flow")
public class FlowController {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 启动流程实例
     * @param request 请求参数，包含流程定义Key、租户ID和流程变量
     * @return 流程启动结果
     */
    @PreAuthorize("hasAuthority('flow:start')")
    @LogOperation(module = "流程管理", operationType = "启动", content = "流程实例启动")
    @PostMapping("/start")
    public AjaxResult<StartProcessResponse> startProcessInstance(@Validated @RequestBody StartProcessRequest request) {
        try {
            // 获取必要参数
            String processDefinitionKey = request.getProcessDefinitionKey();
            Long tenantId = request.getTenantId();
            Map<String, Object> variables = request.getVariables();

            // 如果变量为空，初始化为空Map
            if (variables == null) {
                variables = Map.of();
            }

            // 添加租户ID到变量中
            variables.put("tenantId", tenantId);

            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

            // 构建响应对象
            StartProcessResponse response = new StartProcessResponse();
            response.setProcessInstanceId(processInstance.getId());
            response.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            response.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            response.setTenantId(tenantId);

            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("流程启动失败: " + e.getMessage());
        }
    }

    /**
     * 触发流程事件
     * @param request 请求参数，包含流程实例ID、事件类型和事件数据
     * @return 事件触发结果
     */
    @PreAuthorize("hasAuthority('flow:event')")
    @LogOperation(module = "流程管理", operationType = "触发", content = "流程事件触发")
    @PostMapping("/event")
    public AjaxResult<TriggerFlowEventResponse> triggerFlowEvent(@Validated @RequestBody TriggerFlowEventRequest request) {
        try {
            // 获取必要参数
            String processInstanceId = request.getProcessInstanceId();
            String eventType = request.getEventType();

            // 参数校验
            if (processInstanceId == null || processInstanceId.isEmpty()) {
                return AjaxResult.error("流程实例ID不能为空");
            }
            if (eventType == null || eventType.isEmpty()) {
                return AjaxResult.error("事件类型不能为空");
            }

            // 这里可以根据事件类型和事件数据执行相应的业务逻辑
            // 例如，完成任务、更新流程变量等

            // 示例：根据事件类型执行不同的操作
            switch (eventType) {
                case "approve":
                    // 审批通过逻辑
                    break;
                case "reject":
                    // 审批拒绝逻辑
                    break;
                default:
                    // 其他事件类型处理
                    break;
            }

            // 构建响应对象
            TriggerFlowEventResponse response = new TriggerFlowEventResponse();
            response.setProcessInstanceId(processInstanceId);
            response.setEventType(eventType);
            response.setStatus("success");
            response.setMessage("流程事件触发成功");

            return AjaxResult.success(response);
        } catch (Exception e) {
            // 构建错误响应对象
            TriggerFlowEventResponse errorResponse = new TriggerFlowEventResponse();
            if (request != null) {
                errorResponse.setProcessInstanceId(request.getProcessInstanceId());
                errorResponse.setEventType(request.getEventType());
            }
            errorResponse.setStatus("error");
            errorResponse.setMessage("流程事件触发失败: " + e.getMessage());
            
            return AjaxResult.success(errorResponse);
        }
    }
}
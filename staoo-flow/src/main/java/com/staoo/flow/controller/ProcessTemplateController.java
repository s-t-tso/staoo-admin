package com.staoo.flow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.flow.domain.ProcessTemplate;
import com.staoo.flow.pojo.request.ProcessTemplateRequest;
import com.staoo.flow.service.ProcessTemplateService;

/**
 * 流程模板控制器
 * 提供流程模板相关的REST API接口
 */
@RestController
@RequestMapping("/api/process-template")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateService processTemplateService;

    /**
     * 根据ID查询流程模板
     * @param id 流程模板ID
     * @return 流程模板信息
     */
    @PreAuthorize("hasAuthority('process:template:view')")
    @GetMapping("/{id}")
    public AjaxResult<ProcessTemplate> getById(@PathVariable Long id) {
        try {
            ProcessTemplate processTemplate = processTemplateService.getById(id);
            if (processTemplate == null) {
                return AjaxResult.error("流程模板不存在");
            }
            return AjaxResult.success(processTemplate);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据流程标识查询流程模板
     * @param processKey 流程标识
     * @param tenantId 租户ID
     * @return 流程模板信息
     */
    @PreAuthorize("hasAuthority('process:template:view')")
    @GetMapping("/by-key")
    public AjaxResult<ProcessTemplate> getByProcessKey(@RequestParam String processKey, @RequestParam Long tenantId) {
        try {
            ProcessTemplate processTemplate = processTemplateService.getByProcessKey(processKey, tenantId);
            if (processTemplate == null) {
                return AjaxResult.error("流程模板不存在");
            }
            return AjaxResult.success(processTemplate);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询流程模板列表
     * @param processTemplate 查询条件
     * @return 流程模板列表
     */
    @PreAuthorize("hasAuthority('process:template:list')")
    @GetMapping("/list")
    public AjaxResult<List<ProcessTemplate>> getList(ProcessTemplate processTemplate) {
        try {
            List<ProcessTemplate> list = processTemplateService.getList(processTemplate);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增流程模板
     * @param request 流程模板请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('process:template:add')")
    @LogOperation(module = "流程管理", operationType = "新增", content = "新增流程模板")
    @PostMapping
    public AjaxResult<Integer> add(@RequestBody ProcessTemplateRequest request) {
        try {
            // 验证流程标识唯一性
            if (!processTemplateService.checkProcessKeyUnique(request.getProcessKey(), request.getTenantId(), null)) {
                return AjaxResult.error("流程标识已存在");
            }
            
            // 转换请求对象为实体对象
            ProcessTemplate processTemplate = new ProcessTemplate();
            processTemplate.setProcessKey(request.getProcessKey());
            processTemplate.setProcessName(request.getProcessName());
            processTemplate.setDescription(request.getDescription());
            processTemplate.setBpmnXml(request.getBpmnXml());
            processTemplate.setStatus(request.getStatus());
            processTemplate.setVersion(request.getVersion());
            processTemplate.setTenantId(request.getTenantId());
            processTemplate.setCategory(request.getCategory());
            processTemplate.setFormKey(request.getFormKey());
            
            int result = processTemplateService.save(processTemplate);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新流程模板
     * @param request 流程模板请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('process:template:edit')")
    @LogOperation(module = "流程管理", operationType = "更新", content = "更新流程模板")
    @PutMapping
    public AjaxResult<Integer> update(@RequestBody ProcessTemplateRequest request) {
        try {
            // 验证流程标识唯一性
            if (!processTemplateService.checkProcessKeyUnique(request.getProcessKey(), request.getTenantId(), request.getId())) {
                return AjaxResult.error("流程标识已存在");
            }
            
            // 转换请求对象为实体对象
            ProcessTemplate processTemplate = new ProcessTemplate();
            processTemplate.setId(request.getId());
            processTemplate.setProcessKey(request.getProcessKey());
            processTemplate.setProcessName(request.getProcessName());
            processTemplate.setDescription(request.getDescription());
            processTemplate.setBpmnXml(request.getBpmnXml());
            processTemplate.setStatus(request.getStatus());
            processTemplate.setVersion(request.getVersion());
            processTemplate.setTenantId(request.getTenantId());
            processTemplate.setCategory(request.getCategory());
            processTemplate.setFormKey(request.getFormKey());
            
            int result = processTemplateService.update(processTemplate);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除流程模板
     * @param id 流程模板ID
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('process:template:delete')")
    @LogOperation(module = "流程管理", operationType = "删除", content = "删除流程模板")
    @DeleteMapping("/{id}")
    public AjaxResult<Integer> deleteById(@PathVariable Long id) {
        try {
            int result = processTemplateService.deleteById(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除流程模板
     * @param ids 流程模板ID列表
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('process:template:delete')")
    @LogOperation(module = "流程管理", operationType = "删除", content = "批量删除流程模板")
    @DeleteMapping("/batch")
    public AjaxResult<Integer> deleteByIds(@RequestBody Long[] ids) {
        try {
            int result = processTemplateService.deleteByIds(ids);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 发布流程模板
     * @param id 流程模板ID
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('process:template:publish')")
    @LogOperation(module = "流程管理", operationType = "发布", content = "发布流程模板")
    @PostMapping("/publish/{id}")
    public AjaxResult<Integer> publish(@PathVariable Long id) {
        try {
            int result = processTemplateService.publishProcessTemplate(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("发布失败: " + e.getMessage());
        }
    }

    /**
     * 复制流程模板
     * @param params 包含模板ID和新模板名称的参数
     * @return 新模板ID
     */
    @PreAuthorize("hasAuthority('process:template:copy')")
    @LogOperation(module = "流程管理", operationType = "复制", content = "复制流程模板")
    @PostMapping("/copy")
    public AjaxResult<Long> copy(@RequestBody Map<String, Object> params) {
        try {
            Long id = Long.valueOf(params.get("id").toString());
            String newName = params.get("newName").toString();
            Long newTemplateId = processTemplateService.copyProcessTemplate(id, newName);
            return AjaxResult.success(newTemplateId);
        } catch (Exception e) {
            return AjaxResult.error("复制失败: " + e.getMessage());
        }
    }
}
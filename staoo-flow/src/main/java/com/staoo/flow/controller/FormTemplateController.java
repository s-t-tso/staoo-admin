package com.staoo.flow.controller;

import com.staoo.common.annotation.LogOperation;
import com.staoo.common.domain.AjaxResult;
import com.staoo.flow.domain.FormTemplate;
import com.staoo.flow.mapstruct.FormTemplateConverter;
import com.staoo.flow.pojo.request.FormTemplateRequest;
import com.staoo.flow.pojo.response.FormTemplateResponse;
import com.staoo.flow.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表单模板控制器
 * 提供表单模板相关的REST API接口
 */
@RestController
@RequestMapping("/api/form-template")
public class FormTemplateController {

    @Autowired
    private FormTemplateService formTemplateService;
    
    @Autowired
    private FormTemplateConverter formTemplateConverter;

    /**
     * 根据ID查询表单模板
     * @param id 表单模板ID
     * @return 表单模板信息
     */
    @PreAuthorize("hasAuthority('form:template:view')")
    @GetMapping("/{id}")
    public AjaxResult<FormTemplateResponse> getById(@PathVariable Long id) {
        try {
            FormTemplate formTemplate = formTemplateService.getById(id);
            if (formTemplate == null) {
                return AjaxResult.error("表单模板不存在");
            }
            FormTemplateResponse response = formTemplateConverter.toResponse(formTemplate);
            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据表单标识查询表单模板
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单模板信息
     */
    @PreAuthorize("hasAuthority('form:template:view')")
    @GetMapping("/by-key")
    public AjaxResult<FormTemplateResponse> getByFormKey(@RequestParam String formKey, @RequestParam Long tenantId) {
        try {
            FormTemplate formTemplate = formTemplateService.getByFormKey(formKey, tenantId);
            if (formTemplate == null) {
                return AjaxResult.error("表单模板不存在");
            }
            FormTemplateResponse response = formTemplateConverter.toResponse(formTemplate);
            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单模板列表
     * @param request 查询条件
     * @return 表单模板列表
     */
    @PreAuthorize("hasAuthority('form:template:list')")
    @GetMapping("/list")
    public AjaxResult<List<FormTemplateResponse>> getList(FormTemplateRequest request) {
        try {
            FormTemplate formTemplate = formTemplateConverter.toEntity(request);
            List<FormTemplate> list = formTemplateService.getList(formTemplate);
            List<FormTemplateResponse> responseList = formTemplateConverter.toResponseList(list);
            return AjaxResult.success(responseList);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增表单模板
     * @param request 表单模板请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:template:add')")
    @LogOperation(module = "表单管理", operationType = "新增", content = "新增表单模板")
    @PostMapping
    public AjaxResult<Integer> add(@Validated @RequestBody FormTemplateRequest request) {
        try {
            FormTemplate formTemplate = formTemplateConverter.toEntity(request);
            // 验证表单标识唯一性
            if (!formTemplateService.checkFormKeyUnique(formTemplate.getFormKey(), formTemplate.getTenantId(), null)) {
                return AjaxResult.error("表单标识已存在");
            }
            int result = formTemplateService.save(formTemplate);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新表单模板
     * @param request 表单模板请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:template:edit')")
    @LogOperation(module = "表单管理", operationType = "更新", content = "更新表单模板")
    @PutMapping
    public AjaxResult<Integer> update(@Validated @RequestBody FormTemplateRequest request) {
        try {
            FormTemplate formTemplate = formTemplateConverter.toEntity(request);
            // 验证表单标识唯一性
            if (!formTemplateService.checkFormKeyUnique(formTemplate.getFormKey(), formTemplate.getTenantId(), formTemplate.getId())) {
                return AjaxResult.error("表单标识已存在");
            }
            int result = formTemplateService.update(formTemplate);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除表单模板
     * @param id 表单模板ID
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:template:delete')")
    @LogOperation(module = "表单管理", operationType = "删除", content = "删除表单模板")
    @DeleteMapping("/{id}")
    public AjaxResult<Integer> deleteById(@PathVariable Long id) {
        try {
            int result = formTemplateService.deleteById(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除表单模板
     * @param ids 表单模板ID列表
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:template:delete')")
    @LogOperation(module = "表单管理", operationType = "删除", content = "批量删除表单模板")
    @DeleteMapping("/batch")
    public AjaxResult<Integer> deleteByIds(@RequestBody Long[] ids) {
        try {
            int result = formTemplateService.deleteByIds(ids);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 发布表单模板
     * @param request 表单模板请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:template:publish')")
    @LogOperation(module = "表单管理", operationType = "发布", content = "发布表单模板")
    @PostMapping("/publish")
    public AjaxResult<Integer> publish(@Validated @RequestBody FormTemplateRequest request) {
        try {
            int result = formTemplateService.publishFormTemplate(request.getId());
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("发布失败: " + e.getMessage());
        }
    }

    /**
     * 复制表单模板
     * @param request 复制请求信息
     * @return 新模板ID
     */
    @PreAuthorize("hasAuthority('form:template:copy')")
    @LogOperation(module = "表单管理", operationType = "复制", content = "复制表单模板")
    @PostMapping("/copy")
    public AjaxResult<Long> copy(@Validated @RequestBody FormTemplateRequest request) {
        try {
            // 验证表单标识唯一性
            if (!formTemplateService.checkFormKeyUnique(request.getFormKey(), request.getTenantId(), null)) {
                return AjaxResult.error("表单标识已存在");
            }
            Long newTemplateId = formTemplateService.copyFormTemplate(request.getId(), request.getFormName());
            return AjaxResult.success(newTemplateId);
        } catch (Exception e) {
            return AjaxResult.error("复制失败: " + e.getMessage());
        }
    }
}
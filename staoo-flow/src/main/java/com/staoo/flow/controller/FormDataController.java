package com.staoo.flow.controller; 

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.staoo.flow.domain.FormData;
import com.staoo.flow.mapstruct.FormDataConverter;
import com.staoo.flow.pojo.request.FormDataRequest;
import com.staoo.flow.pojo.response.FormDataResponse;
import com.staoo.flow.service.FormDataService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 表单数据控制器
 * 提供表单数据相关的REST API接口
 */
@RestController
@RequestMapping("/api/form-data")
public class FormDataController {

    @Autowired
    private FormDataService formDataService;
    
    @Autowired
    private FormDataConverter formDataConverter;

    /**
     * 根据ID查询表单数据
     * @param id 表单数据ID
     * @return 表单数据信息
     */
    @PreAuthorize("hasAuthority('form:data:view')")
    @GetMapping("/{id}")
    public AjaxResult<FormDataResponse> getById(@PathVariable Long id) {
        try {
            FormData formData = formDataService.getById(id);
            if (formData == null) {
                return AjaxResult.error("表单数据不存在");
            }
            FormDataResponse response = formDataConverter.toResponse(formData);
            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据业务键和表单标识查询表单数据
     * @param businessKey 业务键
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @return 表单数据信息
     */
    @PreAuthorize("hasAuthority('form:data:view')")
    @GetMapping("/by-business-key")
    public AjaxResult<FormDataResponse> getByBusinessKey(@RequestParam String businessKey, 
                                                     @RequestParam String formKey, 
                                                     @RequestParam Long tenantId) {
        try {
            FormData formData = formDataService.getByBusinessKey(businessKey, formKey, tenantId);
            if (formData == null) {
                return AjaxResult.error("表单数据不存在");
            }
            FormDataResponse response = formDataConverter.toResponse(formData);
            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单数据列表
     * @param formData 查询条件
     * @return 表单数据列表
     */
    @PreAuthorize("hasAuthority('form:data:list')")
    @GetMapping("/list")
    public AjaxResult<List<FormDataResponse>> getList(FormData formData) {
        try {
            List<FormData> list = formDataService.getList(formData);
            List<FormDataResponse> responses = formDataConverter.toResponseList(list);
            return AjaxResult.success(responses);
        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增表单数据
     * @param request 表单数据请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:data:add')")
    @LogOperation(module = "表单管理", operationType = "新增", content = "新增表单数据")
    @PostMapping
    public AjaxResult<Integer> add(@Validated @RequestBody FormDataRequest request) {
        try {
            FormData formData = formDataConverter.toEntity(request);
            int result = formDataService.save(formData);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新表单数据
     * @param request 表单数据请求信息
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:data:edit')")
    @LogOperation(module = "表单管理", operationType = "更新", content = "更新表单数据")
    @PutMapping
    public AjaxResult<Integer> update(@Validated @RequestBody FormDataRequest request) {
        try {
            FormData formData = formDataConverter.toEntity(request);
            int result = formDataService.update(formData);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除表单数据
     * @param id 表单数据ID
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:data:delete')")
    @LogOperation(module = "表单管理", operationType = "删除", content = "删除表单数据")
    @DeleteMapping("/{id}")
    public AjaxResult<Integer> deleteById(@PathVariable Long id) {
        try {
            int result = formDataService.deleteById(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除表单数据
     * @param ids 表单数据ID列表
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:data:delete')")
    @LogOperation(module = "表单管理", operationType = "删除", content = "批量删除表单数据")
    @DeleteMapping("/batch")
    public AjaxResult<Integer> deleteByIds(@RequestBody Long[] ids) {
        try {
            int result = formDataService.deleteByIds(ids);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新表单数据状态
     * @param id 表单数据ID
     * @param status 状态
     * @return 操作结果
     */
    @PreAuthorize("hasAuthority('form:data:edit')")
    @LogOperation(module = "表单管理", operationType = "更新", content = "更新表单数据状态")
    @PutMapping("/status/{id}")
    public AjaxResult<Integer> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        try {
            String status = statusMap.get("status");
            if (status == null || status.isEmpty()) {
                return AjaxResult.error("状态不能为空");
            }
            int result = formDataService.updateStatus(id, status);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("更新状态失败: " + e.getMessage());
        }
    }

    /**
     * 导出表单数据
     * @param formKey 表单标识
     * @param tenantId 租户ID
     * @param response HTTP响应
     */
    @PreAuthorize("hasAuthority('form:data:export')")
    @LogOperation(module = "表单管理", operationType = "导出", content = "导出表单数据")
    @GetMapping("/export")
    public void exportFormData(@RequestParam String formKey, @RequestParam Long tenantId, HttpServletResponse response) {
        try {
            // 导出数据
            byte[] data = formDataService.exportFormData(formKey, tenantId);
            
            // 设置响应头
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(formKey + "_data.json", StandardCharsets.UTF_8));
            response.setContentLength(data.length);
            
            // 输出数据
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\": 500, \"message\": \"导出失败: " + e.getMessage() + "\"}");
            } catch (IOException ex) {
                // 忽略写入异常
            }
        }
    }
}
<template>
  <div class="form-template-edit">
    <div class="header">
      <el-button @click="handleBack" type="default">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>{{ formTemplateId ? '编辑表单模板' : '新增表单模板' }}</h1>
      <div class="header-actions">
        <el-button @click="handlePreview" type="default">预览</el-button>
        <el-button @click="handleSave" type="primary">保存</el-button>
      </div>
    </div>
    
    <el-steps :active="activeStep" class="steps" align-center>
      <el-step title="基础设置" />
      <el-step title="表单设计" />
      <el-step title="流程设置" />
      <el-step title="高级设置" />
    </el-steps>
    
    <div class="content">
      <!-- 基础设置 -->
      <div v-if="activeStep === 0">
        <el-form ref="basicForm" :model="formTemplateForm" :rules="basicRules" label-width="100px">
          <el-form-item label="表单名称" prop="formName">
            <el-input v-model="formTemplateForm.formName" placeholder="请输入表单名称" />
          </el-form-item>
          <el-form-item label="表单标识" prop="formKey">
            <el-input v-model="formTemplateForm.formKey" placeholder="请输入表单标识，用于程序识别" :disabled="!!formTemplateId" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="formTemplateForm.description" type="textarea" placeholder="请输入表单描述" />
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 表单设计 -->
      <div v-if="activeStep === 1">
        <div class="design-tabs">
          <el-tabs v-model="designTab" type="border-card">
            <el-tab-pane label="表单设计">
              <div class="form-design-section">
                <!-- 左侧控件面板 -->
                <div class="toolbox">
                  <div class="toolbox-section">
                    <h3>布局控件</h3>
                    <div class="toolbox-items">
                      <el-button size="small" type="default" @click="addControl('column')">分栏</el-button>
                    </div>
                  </div>
                  
                  <div class="toolbox-section">
                    <h3>基础控件</h3>
                    <div class="toolbox-items">
                      <el-button size="small" type="default" @click="addControl('singleInput')">单行输入框</el-button>
                      <el-button size="small" type="default" @click="addControl('multiInput')">多行输入框</el-button>
                      <el-button size="small" type="default" @click="addControl('number')">数字输入框</el-button>
                      <el-button size="small" type="default" @click="addControl('radio')">单选框</el-button>
                      <el-button size="small" type="default" @click="addControl('checkbox')">多选框</el-button>
                      <el-button size="small" type="default" @click="addControl('date')">日期</el-button>
                      <el-button size="small" type="default" @click="addControl('dateRange')">日期区间</el-button>
                      <el-button size="small" type="default" @click="addControl('idCard')">身份证</el-button>
                      <el-button size="small" type="default" @click="addControl('phone')">电话</el-button>
                    </div>
                  </div>
                  
                  <div class="toolbox-section">
                    <h3>增强控件</h3>
                    <div class="toolbox-items">
                      <el-button size="small" type="default" @click="addControl('classification')">级联/分类</el-button>
                      <el-button size="small" type="default" @click="addControl('image')">图片</el-button>
                      <el-button size="small" type="default" @click="addControl('table')">明细/表格</el-button>
                      <el-button size="small" type="default" @click="addControl('amount')">金额</el-button>
                      <el-button size="small" type="default" @click="addControl('attachment')">附件</el-button>
                      <el-button size="small" type="default" @click="addControl('signature')">手写签名</el-button>
                      <el-button size="small" type="default" @click="addControl('externalContact')">外部联系人</el-button>
                      <el-button size="small" type="default" @click="addControl('contact')">联系人</el-button>
                      <el-button size="small" type="default" @click="addControl('project')">工程项目</el-button>
                      <el-button size="small" type="default" @click="addControl('department')">部门</el-button>
                      <el-button size="small" type="default" @click="addControl('industryRecordDept')">行业通讯录部门</el-button>
                      <el-button size="small" type="default" @click="addControl('location')">地点</el-button>
                      <el-button size="small" type="default" @click="addControl('calculation')">计算公式</el-button>
                      <el-button size="small" type="default" @click="addControl('linkedApproval')">关联审批单</el-button>
                      <el-button size="small" type="default" @click="addControl('region')">省市区</el-button>
                      <el-button size="small" type="default" @click="addControl('rating')">评分</el-button>
                    </div>
                  </div>
                </div>
                
                <!-- 右侧表单预览区域 -->
                <div class="form-preview">
                  <div class="preview-header">
                    <h3>表单预览</h3>
                    <div class="preview-actions">
                      <el-button size="small" @click="clearForm">清空表单</el-button>
                      <el-button size="small" type="danger" @click="clearSelected">清空选中</el-button>
                    </div>
                  </div>
                  
                  <div class="preview-container" ref="previewContainer">
                    <div v-for="(control, index) in formControls" :key="control.id" class="form-control-item" :class="{ 'selected': selectedControlId === control.id }" @click.stop="selectControl(control.id)">
                      <el-form-item :label="control.label" :prop="control.fieldName" :required="control.required">
                        <component :is="getControlComponent(control.type)" :control="control" v-model="control.value" />
                      </el-form-item>
                      
                      <!-- 控件操作工具栏 -->
                      <div v-if="selectedControlId === control.id" class="control-actions">
                        <el-button size="small" @click.stop="moveUp(index)">上移</el-button>
                        <el-button size="small" @click.stop="moveDown(index)">下移</el-button>
                        <el-button size="small" type="danger" @click.stop="removeControl(index)">删除</el-button>
                        <el-button size="small" @click.stop="editControlProperties(index)">属性</el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="表单属性">
              <div v-if="selectedControlId">
                <el-form ref="controlForm" :model="selectedControl" :rules="controlRules" label-width="100px">
                  <el-form-item label="字段标签" prop="label">
                    <el-input v-model="selectedControl.label" />
                  </el-form-item>
                  <el-form-item label="字段名称" prop="fieldName">
                    <el-input v-model="selectedControl.fieldName" />
                  </el-form-item>
                  <el-form-item label="提示文字" prop="placeholder">
                    <el-input v-model="selectedControl.placeholder" />
                  </el-form-item>
                  <el-form-item label="必填项">
                    <el-switch v-model="selectedControl.required" />
                  </el-form-item>
                  <el-form-item label="默认值" prop="defaultValue">
                    <el-input v-model="selectedControl.defaultValue" />
                  </el-form-item>
                  <el-form-item label="显示条件" prop="showCondition">
                    <el-input v-model="selectedControl.showCondition" type="textarea" />
                    <div class="form-text">使用JavaScript表达式，返回true/false</div>
                  </el-form-item>
                  
                  <!-- 根据控件类型显示不同的属性 -->
                  <div v-if="selectedControl.type === 'radio' || selectedControl.type === 'checkbox'">
                    <el-form-item label="选项列表">
                      <div v-for="(option, optIndex) in selectedControl.options" :key="optIndex" class="option-item">
                        <el-input v-model="option.label" placeholder="选项文本" style="width: 150px; margin-right: 10px" />
                        <el-input v-model="option.value" placeholder="选项值" style="width: 150px; margin-right: 10px" />
                        <el-button size="small" type="danger" @click="removeOption(optIndex)">删除</el-button>
                      </div>
                      <el-button size="small" @click="addOption">添加选项</el-button>
                    </el-form-item>
                  </div>
                </el-form>
              </div>
              <div v-else class="no-selection">
                请在表单设计区选择一个控件以编辑其属性
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      
      <!-- 流程设置 -->
      <div v-if="activeStep === 2">
        <el-form ref="flowForm" :model="flowSettings" :rules="flowRules" label-width="100px">
          <el-form-item label="关联流程" prop="processKey">
            <el-select v-model="flowSettings.processKey" placeholder="请选择关联的流程模板">
              <el-option value="">无关联流程</el-option>
              <el-option v-for="process in processTemplateList" :key="process.processKey" :label="process.processName" :value="process.processKey" />
            </el-select>
          </el-form-item>
          <el-form-item label="提交校验" prop="submitValidation">
            <el-switch v-model="flowSettings.submitValidation" />
          </el-form-item>
          <el-form-item label="提交成功提示" prop="successMessage">
            <el-input v-model="flowSettings.successMessage" placeholder="表单提交成功后的提示信息" />
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 高级设置 -->
      <div v-if="activeStep === 3">
        <el-form ref="advancedForm" :model="advancedSettings" :rules="advancedRules" label-width="100px">
          <el-form-item label="版本号" prop="version">
            <el-input v-model.number="advancedSettings.version" placeholder="请输入版本号" type="number" />
          </el-form-item>
          <el-form-item label="是否启用" prop="enabled">
            <el-switch v-model="advancedSettings.enabled" />
          </el-form-item>
          <el-form-item label="优先级" prop="priority">
            <el-slider v-model="advancedSettings.priority" :min="1" :max="10" show-input />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="advancedSettings.remark" type="textarea" placeholder="请输入备注信息" />
          </el-form-item>
        </el-form>
      </div>
    </div>
    
    <div class="footer">
      <el-button v-if="activeStep > 0" @click="prevStep" type="default">上一步</el-button>
      <el-button v-if="activeStep < 3" @click="nextStep" type="primary">下一步</el-button>
      <el-button v-if="activeStep === 3" @click="handleSave" type="primary">保存表单模板</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { getFormTemplateById, saveFormTemplate, updateFormTemplate } from '../../../services/flow/formTemplateService'
import { getProcessTemplateList } from '../../../services/flow/processTemplateService'

const router = useRouter()
const route = useRoute()

// 获取路由参数中的ID
const formTemplateId = computed(() => {
  return route.query.id ? Number(route.query.id) : null
})

// 当前步骤
const activeStep = ref(0)

// 设计标签页
const designTab = ref('0')

// 表单模板表单
const formTemplateForm = reactive({
  formName: '',
  formKey: '',
  description: '',
  formConfig: '',
  status: 'DRAFT',
  version: 1,
  tenantId: 1 // 这里需要根据实际情况设置租户ID
})

// 基础设置表单规则
const basicRules = {
  formName: [{ required: true, message: '请输入表单名称', trigger: 'blur' }],
  formKey: [{ required: true, message: '请输入表单标识', trigger: 'blur' }]
}

// 流程设置
const flowSettings = reactive({
  processKey: '',
  submitValidation: true,
  successMessage: '表单提交成功！'
})

// 流程设置表单规则
const flowRules = {
  processKey: []
}

// 高级设置
const advancedSettings = reactive({
  version: 1,
  enabled: true,
  priority: 5,
  remark: ''
})

// 高级设置表单规则
const advancedRules = {
  version: [{ required: true, message: '请输入版本号', trigger: 'blur' }, { type: 'number', min: 1, message: '版本号必须大于0', trigger: 'blur' }]
}

// 流程模板列表
const processTemplateList = ref<any[]>([])

// 表单控件列表
const formControls = ref<any[]>([])

// 选中的控件ID
const selectedControlId = ref<number | null>(null)

// 选中的控件
const selectedControl = ref<any>({})

// 控件属性表单规则
const controlRules = {
  label: [{ required: true, message: '请输入字段标签', trigger: 'blur' }],
  fieldName: [{ required: true, message: '请输入字段名称', trigger: 'blur' }],
  placeholder: []
}

// 加载表单模板详情
const loadFormTemplateDetail = async () => {
  if (formTemplateId.value) {
    try {
      const response = await getFormTemplateById(formTemplateId.value)
      if (response.code === 200 && response.data) {
        // 填充表单数据
        Object.assign(formTemplateForm, response.data)
        
        // 解析表单配置JSON
        if (response.data.formConfig) {
          try {
            formControls.value = JSON.parse(response.data.formConfig)
          } catch (error) {
            console.error('解析表单配置失败：', error)
            ElMessage.error('解析表单配置失败')
          }
        }
      } else {
        ElMessage.error('获取表单模板详情失败：' + (response.msg || '未知错误'))
      }
    } catch (error) {
      ElMessage.error('获取表单模板详情失败')
      console.error('获取表单模板详情异常：', error)
    }
  }
}

// 加载流程模板列表
const loadProcessTemplateList = async () => {
  try {
    const response = await getProcessTemplateList({ tenantId: 1 }) // 这里需要根据实际情况设置租户ID
    if (response.code === 200 && response.data) {
      processTemplateList.value = response.data
    } else {
      ElMessage.error('获取流程模板列表失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取流程模板列表失败')
    console.error('获取流程模板列表异常：', error)
  }
}

// 添加控件
const addControl = (type: string) => {
  const controlTypes: Record<string, { name: string; fieldName: string }> = {
    // 布局控件
    column: { name: '分栏', fieldName: `column_${Date.now()}` },
    // 基础控件
    singleInput: { name: '单行输入框', fieldName: `input_${Date.now()}` },
    multiInput: { name: '多行输入框', fieldName: `textarea_${Date.now()}` },
    number: { name: '数字输入框', fieldName: `number_${Date.now()}` },
    radio: { name: '单选框', fieldName: `radio_${Date.now()}` },
    checkbox: { name: '多选框', fieldName: `checkbox_${Date.now()}` },
    date: { name: '日期', fieldName: `date_${Date.now()}` },
    dateRange: { name: '日期区间', fieldName: `date_range_${Date.now()}` },
    idCard: { name: '身份证', fieldName: `id_card_${Date.now()}` },
    phone: { name: '电话', fieldName: `phone_${Date.now()}` },
    // 增强控件
    classification: { name: '级联/分类', fieldName: `classification_${Date.now()}` },
    image: { name: '图片', fieldName: `image_${Date.now()}` },
    table: { name: '明细/表格', fieldName: `table_${Date.now()}` },
    amount: { name: '金额', fieldName: `amount_${Date.now()}` },
    attachment: { name: '附件', fieldName: `attachment_${Date.now()}` },
    signature: { name: '手写签名', fieldName: `signature_${Date.now()}` },
    externalContact: { name: '外部联系人', fieldName: `external_contact_${Date.now()}` },
    contact: { name: '联系人', fieldName: `contact_${Date.now()}` },
    project: { name: '工程项目', fieldName: `project_${Date.now()}` },
    department: { name: '部门', fieldName: `department_${Date.now()}` },
    industryRecordDept: { name: '行业通讯录部门', fieldName: `industry_dept_${Date.now()}` },
    location: { name: '地点', fieldName: `location_${Date.now()}` },
    calculation: { name: '计算公式', fieldName: `calculation_${Date.now()}` },
    linkedApproval: { name: '关联审批单', fieldName: `linked_approval_${Date.now()}` },
    region: { name: '省市区', fieldName: `region_${Date.now()}` },
    rating: { name: '评分', fieldName: `rating_${Date.now()}` }
  }
  
  const controlInfo = controlTypes[type]
  const newControl = {
    id: Date.now(),
    type: type,
    label: controlInfo.name,
    fieldName: controlInfo.fieldName,
    placeholder: `请输入${controlInfo.name}`,
    value: '',
    required: false,
    disabled: false,
    defaultValue: '',
    showCondition: '',
    options: (type === 'radio' || type === 'checkbox') ? [{ label: '选项1', value: '1' }, { label: '选项2', value: '2' }] : []
  }
  
  formControls.value.push(newControl)
  // 选中新添加的控件
  selectControl(newControl.id)
}

// 获取控件组件
const getControlComponent = (_type: string) => {
  // 这里应该返回对应的控件组件
  // 为了简化示例，返回一个通用的组件名称
  return 'el-input'
}

// 选择控件
const selectControl = (id: number) => {
  selectedControlId.value = id
  const control = formControls.value.find(c => c.id === id)
  if (control) {
    selectedControl.value = { ...control }
    designTab.value = '1' // 切换到属性标签页
  }
}

// 移动控件上移
const moveUp = (index: number) => {
  if (index > 0) {
    const temp = formControls.value[index]
    formControls.value[index] = formControls.value[index - 1]
    formControls.value[index - 1] = temp
  } else {
    ElMessage.warning('已经是第一个控件了')
  }
}

// 移动控件下移
const moveDown = (index: number) => {
  if (index < formControls.value.length - 1) {
    const temp = formControls.value[index]
    formControls.value[index] = formControls.value[index + 1]
    formControls.value[index + 1] = temp
  } else {
    ElMessage.warning('已经是最后一个控件了')
  }
}

// 删除控件
const removeControl = (index: number) => {
  formControls.value.splice(index, 1)
  selectedControlId.value = null
  selectedControl.value = {}
}

// 编辑控件属性
const editControlProperties = (_index: number) => {
  // 保存属性更改到控件列表
  const control = formControls.value.find(c => c.id === selectedControlId.value)
  if (control) {
    Object.assign(control, selectedControl.value)
    ElMessage.success('控件属性已更新')
  }
}

// 清空表单
const clearForm = () => {
  ElMessageBox.confirm('确定要清空表单吗？清空后将无法恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'error'
  }).then(() => {
    formControls.value = []
    selectedControlId.value = null
    selectedControl.value = {}
    ElMessage.success('表单已清空')
  })
}

// 清空选中
const clearSelected = () => {
  selectedControlId.value = null
  selectedControl.value = {}
  designTab.value = '0' // 切换回设计标签页
}

// 添加选项
const addOption = () => {
  if (!selectedControl.value.options) {
    selectedControl.value.options = []
  }
  selectedControl.value.options.push({ label: `选项${selectedControl.value.options.length + 1}`, value: `${selectedControl.value.options.length + 1}` })
}

// 删除选项
const removeOption = (index: number) => {
  if (selectedControl.value.options && selectedControl.value.options.length > 1) {
    selectedControl.value.options.splice(index, 1)
  } else {
    ElMessage.warning('至少保留一个选项')
  }
}

// 上一步
const prevStep = () => {
  activeStep.value--
}

// 下一步
const nextStep = () => {
  // 验证当前步骤的表单
  if (activeStep.value === 0) {
    const basicForm = (window as any).basicForm
    if (basicForm) {
      basicForm.validate((valid: boolean) => {
        if (valid) {
          activeStep.value++
        }
      })
    } else {
      activeStep.value++
    }
  } else if (activeStep.value === 1) {
    // 保存表单设计
    // 将formControls转换为表单配置JSON并保存
    formTemplateForm.formConfig = JSON.stringify(formControls.value)
    ElMessage.success('表单设计保存成功')
    activeStep.value++
  } else if (activeStep.value === 2) {
    // 保存流程设置
    ElMessage.success('流程设置保存成功')
    activeStep.value++
  }
}

// 保存表单模板
const handleSave = async () => {
  try {
    // 合并高级设置到表单模板表单
    formTemplateForm.version = advancedSettings.version
    
    // 保存表单设计配置
    formTemplateForm.formConfig = JSON.stringify(formControls.value)
    
    let response
    if (formTemplateId.value) {
      // 更新表单模板
      response = await updateFormTemplate({ id: formTemplateId.value, ...formTemplateForm })
    } else {
      // 新增表单模板
      response = await saveFormTemplate(formTemplateForm)
    }
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      router.push('/flow/form-template/list')
    } else {
      ElMessage.error('保存失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存表单模板异常：', error)
  }
}

// 预览
const handlePreview = () => {
  // 这里可以实现表单预览功能
  ElMessage.info('表单预览功能待实现')
}

// 返回
const handleBack = () => {
  if (hasUnsavedChanges()) {
    ElMessageBox.confirm('有未保存的更改，确定要离开吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.push('/flow/form-template/list')
    })
  } else {
    router.push('/flow/form-template/list')
  }
}

// 检查是否有未保存的更改
const hasUnsavedChanges = () => {
  // 这里可以实现检查是否有未保存更改的逻辑
  return false
}

// 初始化加载
onMounted(() => {
  loadProcessTemplateList()
  if (formTemplateId.value) {
    loadFormTemplateDetail()
  }
})
</script>

<style scoped>
.form-template-edit {
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin: 0;
  margin-left: 20px;
  font-size: 20px;
  font-weight: 500;
  flex: 1;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.steps {
  margin-bottom: 30px;
}

.content {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-design-section {
  display: flex;
  gap: 20px;
  min-height: 600px;
}

.toolbox {
  width: 200px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  max-height: 600px;
  overflow-y: auto;
}

.toolbox-section {
  margin-bottom: 20px;
}

.toolbox-section h3 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 500;
  color: #606266;
}

.toolbox-items {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-preview {
  flex: 1;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.preview-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.preview-actions {
  display: flex;
  gap: 10px;
}

.preview-container {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  max-height: 550px;
  overflow-y: auto;
}

.form-control-item {
  position: relative;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 10px;
  cursor: pointer;
}

.form-control-item:hover {
  border-color: #c0c4cc;
}

.form-control-item.selected {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.control-actions {
  position: absolute;
  right: 10px;
  top: 10px;
  display: flex;
  gap: 5px;
}

.no-selection {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #909399;
  font-size: 14px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

.form-text {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}

.option-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.footer {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
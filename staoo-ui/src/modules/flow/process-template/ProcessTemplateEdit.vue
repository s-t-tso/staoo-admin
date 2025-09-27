<template>
  <div class="process-template-edit">
    <div class="header">
      <el-button @click="handleBack" type="default">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>{{ processTemplateId ? '编辑流程模板' : '新增流程模板' }}</h1>
    </div>
    
    <el-steps :active="activeStep" class="steps" align-center>
      <el-step title="基础设置" />
      <el-step title="表单设计" />
      <el-step title="流程设计" />
      <el-step title="高级设置" />
    </el-steps>
    
    <div class="content">
      <!-- 基础设置 -->
      <div v-if="activeStep === 0">
        <el-form ref="basicForm" :model="processTemplateForm" :rules="basicRules" label-width="100px">
          <el-form-item label="流程名称" prop="processName">
            <el-input v-model="processTemplateForm.processName" placeholder="请输入流程名称" />
          </el-form-item>
          <el-form-item label="流程标识" prop="processKey">
            <el-input v-model="processTemplateForm.processKey" placeholder="请输入流程标识，用于程序识别" :disabled="!!processTemplateId" />
          </el-form-item>
          <el-form-item label="流程分类" prop="category">
            <el-input v-model="processTemplateForm.category" placeholder="请输入流程分类" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="processTemplateForm.description" type="textarea" placeholder="请输入流程描述" />
          </el-form-item>
          <el-form-item label="关联表单" prop="formKey">
            <el-select v-model="processTemplateForm.formKey" placeholder="请选择关联的表单模板">
              <el-option value="">无关联表单</el-option>
              <el-option v-for="form in formTemplateList" :key="form.formKey" :label="form.formName" :value="form.formKey" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 表单设计 -->
      <div v-if="activeStep === 1">
        <div class="form-design-section">
          <div class="toolbox">
            <h3>控件</h3>
            <div class="control-group">
              <h4>基础控件</h4>
              <div class="control-items">
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
            <div class="control-group">
              <h4>增强控件</h4>
              <div class="control-items">
                <el-button size="small" type="default" @click="addControl('classification')">级联/分类</el-button>
                <el-button size="small" type="default" @click="addControl('image')">图片</el-button>
                <el-button size="small" type="default" @click="addControl('table')">明细/表格</el-button>
                <el-button size="small" type="default" @click="addControl('amount')">金额</el-button>
                <el-button size="small" type="default" @click="addControl('attachment')">附件</el-button>
                <el-button size="small" type="default" @click="addControl('signature')">手写签名</el-button>
                <el-button size="small" type="default" @click="addControl('calculation')">计算公式</el-button>
                <!-- 特殊控件 -->
                <el-button size="small" type="primary" @click="addControl('userSelect')">联系人选择</el-button>
                <el-button size="small" type="primary" @click="addControl('deptSelect')">部门选择</el-button>
                <el-button size="small" type="primary" @click="addControl('roleSelect')">角色选择</el-button>
              </div>
            </div>
          </div>
          
          <div class="form-preview">
            <h3>表单预览</h3>
            <div class="preview-container">
              <div v-for="(control, index) in formControls" :key="control.id" class="form-control-item">
                <el-form-item :label="control.label" :prop="control.fieldName">
                  <!-- 根据控件类型渲染不同的输入组件 -->
                  <component :is="getControlComponent(control.type)" :control="control" v-model="control.value" />
                </el-form-item>
                <div class="control-actions">
                  <el-button size="small" @click="moveUp(index)">上移</el-button>
                  <el-button size="small" @click="moveDown(index)">下移</el-button>
                  <el-button size="small" type="danger" @click="removeControl(index)">删除</el-button>
                  <el-button size="small" @click="editControl(index)">编辑</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 流程设计 -->
      <div v-if="activeStep === 2">
        <div class="process-design-section">
          <h3>流程设计器</h3>
          <!-- 集成Vue Flow流程设计器 -->
          <div class="flow-design-container">
            <div class="flow-tools">
              <el-button type="primary" @click="saveFlowDesign">保存流程设计</el-button>
              <el-button @click="loadSampleFlow">加载示例流程</el-button>
              <div class="flow-node-palette">
                <h4>节点类型</h4>
                <div class="node-items">
                  <el-button size="small" @click="addStartNode">开始节点</el-button>
                  <el-button size="small" @click="addTaskNode">审批节点</el-button>
                  <el-button size="small" @click="addEndNode">结束节点</el-button>
                </div>
              </div>
            </div>
            <div class="flow-canvas">
              <VueFlow
                v-if="showFlowDesigner"
                v-model="elements"
                @node-select="onNodeSelect"
                @connection-added="onConnectionAdded"
              >
                <!-- 自定义节点组件 -->
                <template #node-start="props">
                  <StartNode :data="props.data" :selected="props.selected" />
                </template>
                <template #node-task="props">
                  <TaskNode :data="props.data" :selected="props.selected" />
                </template>
                <template #node-end="props">
                  <EndNode :data="props.data" :selected="props.selected" />
                </template>
              </VueFlow>
              <div v-else class="flow-tips">流程设计器将在这里渲染...</div>
            </div>
            <!-- 节点属性面板 -->
            <div v-if="selectedNode" class="node-properties-panel">
              <h4>{{ selectedNode.type === 'start' ? '开始节点' : selectedNode.type === 'task' ? '审批节点' : '结束节点' }} 属性</h4>
              <el-form :model="selectedNode.data" label-width="100px">
                <el-form-item v-if="selectedNode.type === 'task'" label="节点名称">
                  <el-input v-model="selectedNode.data.label" placeholder="请输入节点名称" />
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task'" label="审核人员类型">
                  <el-radio-group v-model="selectedNode.data.approverType">
                    <el-radio label="fixed">固定人员</el-radio>
                    <el-radio label="deptManager">部门主管</el-radio>
                    <el-radio label="deptRole">部门角色</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task' && selectedNode.data.approverType === 'fixed'" label="审核人员">
                  <el-select v-model="selectedNode.data.approvers" multiple placeholder="请选择审核人员">
                    <el-option v-for="user in userList" :key="user.id" :label="user.username" :value="user.id" />
                  </el-select>
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task' && selectedNode.data.approverType === 'deptManager'" label="部门字段">
                  <el-select v-model="selectedNode.data.deptField" placeholder="请选择表单中的部门字段">
                    <el-option v-for="control in formControls" :key="control.fieldName" :label="control.label" :value="control.fieldName" />
                  </el-select>
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task' && selectedNode.data.approverType === 'deptRole'" label="部门字段">
                  <el-select v-model="selectedNode.data.deptField" placeholder="请选择表单中的部门字段">
                    <el-option v-for="control in formControls" :key="control.fieldName" :label="control.label" :value="control.fieldName" />
                  </el-select>
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task' && selectedNode.data.approverType === 'deptRole'" label="角色">
                  <el-select v-model="selectedNode.data.roleId" placeholder="请选择角色">
                    <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id" />
                  </el-select>
                </el-form-item>
                <el-form-item v-if="selectedNode.type === 'task'" label="审批方式">
                  <el-radio-group v-model="selectedNode.data.approvalType">
                    <el-radio label="and">会签</el-radio>
                    <el-radio label="or">或签</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
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
      <el-button v-if="activeStep === 3" @click="saveProcessTemplate" type="primary">保存流程模板</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { VueFlow } from '@vue-flow/core'
import type { Node, Edge } from '@vue-flow/core'
import { getProcessTemplateById, saveProcessTemplate as saveProcessTemplateApi, updateProcessTemplate } from '../../../services/flow/processTemplateService'
import { getFormTemplateList } from '../../../services/flow/formTemplateService'
import roleService from '../../../services/system/role'
import userService from '../../../services/system/user'

// 导入Vue Flow样式
import '@vue-flow/core/dist/style.css'

// 自定义节点组件
const StartNode = {
  props: {
    data: {
      type: Object,
      required: true
    },
    selected: {
      type: Boolean,
      default: false
    }
  },
  template: `
    <div 
      :class="['node', 'start-node', { selected: selected }]"
      :style="{
        width: '100px',
        height: '60px',
        backgroundColor: '#409EFF',
        color: 'white',
        borderRadius: '6px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        cursor: 'move',
        border: selected ? '2px solid #67C23A' : 'none'
      }"
    >
      开始
    </div>
  `
}

const TaskNode = {
  props: {
    data: {
      type: Object,
      required: true
    },
    selected: {
      type: Boolean,
      default: false
    }
  },
  template: `
    <div 
      :class="['node', 'task-node', { selected: selected }]"
      :style="{
        width: '120px',
        padding: '10px',
        backgroundColor: '#67C23A',
        color: 'white',
        borderRadius: '6px',
        cursor: 'move',
        border: selected ? '2px solid #409EFF' : 'none'
      }"
    >
      <div style="fontSize: '14px', fontWeight: 'bold', marginBottom: '5px'">{{ data.label || '审批节点' }}</div>
      <div style="fontSize: '12px'">
        {{ data.approverType === 'fixed' ? '固定人员' : '' }}
        {{ data.approverType === 'deptManager' ? '部门主管' : '' }}
        {{ data.approverType === 'deptRole' ? '部门角色' : '' }}
      </div>
    </div>
  `
}

const EndNode = {
  props: {
    data: {
      type: Object,
      required: true
    },
    selected: {
      type: Boolean,
      default: false
    }
  },
  template: `
    <div 
      :class="['node', 'end-node', { selected: selected }]"
      :style="{
        width: '100px',
        height: '60px',
        backgroundColor: '#F56C6C',
        color: 'white',
        borderRadius: '6px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        cursor: 'move',
        border: selected ? '2px solid #409EFF' : 'none'
      }"
    >
      结束
    </div>
  `
}

const router = useRouter()
const route = useRoute()

// 获取路由参数中的ID
const processTemplateId = computed(() => {
  return route.query.id ? Number(route.query.id) : null
})

// 当前步骤
const activeStep = ref(0)

// 流程模板表单
const processTemplateForm = reactive({
  processName: '',
  processKey: '',
  description: '',
  category: '',
  formKey: '',
  bpmnXml: '',
  status: 'DRAFT',
  version: 1,
  tenantId: 1 // 这里需要根据实际情况设置租户ID
})

// 基础设置表单规则
const basicRules = {
  processName: [{ required: true, message: '请输入流程名称', trigger: 'blur' }],
  processKey: [{ required: true, message: '请输入流程标识', trigger: 'blur' }],
  category: [{ required: true, message: '请输入流程分类', trigger: 'blur' }]
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

// 表单模板列表
const formTemplateList = ref<any[]>([])

// 表单控件列表
const formControls = ref<any[]>([])

// Vue Flow 相关
const elements = ref<(Node | Edge)[]>([])
const selectedNode = ref<Node | null>(null)
const showFlowDesigner = ref(true)

// 用户列表和角色列表，用于审核人员选择
const userList = ref<any[]>([])
const roleList = ref<any[]>([])

// 加载流程模板详情
const loadProcessTemplateDetail = async () => {
  if (processTemplateId.value) {
    try {
      const response = await getProcessTemplateById(processTemplateId.value)
      if (response.code === 200 && response.data) {
        // 填充表单数据
        Object.assign(processTemplateForm, response.data)
        
        // 如果有bpmnXml，可以在这里解析并加载到流程设计器
        if (response.data.bpmnXml) {
          try {
            const flowData = JSON.parse(response.data.bpmnXml)
            elements.value = flowData.nodes.concat(flowData.edges)
          } catch (error) {
            console.error('解析bpmnXml失败：', error)
          }
        }
      } else {
        ElMessage.error('获取流程模板详情失败：' + (response.msg || '未知错误'))
      }
    } catch (error) {
      ElMessage.error('获取流程模板详情失败')
      console.error('获取流程模板详情异常：', error)
    }
  }
}

// 加载表单模板列表
const loadFormTemplateList = async () => {
  try {
    const response = await getFormTemplateList({ tenantId: 1 }) // 这里需要根据实际情况设置租户ID
    if (response.code === 200 && response.data) {
      formTemplateList.value = response.data
    } else {
      ElMessage.error('获取表单模板列表失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取表单模板列表失败')
    console.error('获取表单模板列表异常：', error)
  }
}

// 添加控件
const addControl = (type: string) => {
  const controlTypes = {
    singleInput: { name: '单行输入框', fieldName: `input_${Date.now()}` },
    multiInput: { name: '多行输入框', fieldName: `textarea_${Date.now()}` },
    number: { name: '数字输入框', fieldName: `number_${Date.now()}` },
    radio: { name: '单选框', fieldName: `radio_${Date.now()}` },
    checkbox: { name: '多选框', fieldName: `checkbox_${Date.now()}` },
    date: { name: '日期', fieldName: `date_${Date.now()}` },
    dateRange: { name: '日期区间', fieldName: `date_range_${Date.now()}` },
    idCard: { name: '身份证', fieldName: `id_card_${Date.now()}` },
    phone: { name: '电话', fieldName: `phone_${Date.now()}` },
    classification: { name: '级联/分类', fieldName: `classification_${Date.now()}` },
    image: { name: '图片', fieldName: `image_${Date.now()}` },
    table: { name: '明细/表格', fieldName: `table_${Date.now()}` },
    amount: { name: '金额', fieldName: `amount_${Date.now()}` },
    attachment: { name: '附件', fieldName: `attachment_${Date.now()}` },
    signature: { name: '手写签名', fieldName: `signature_${Date.now()}` },
    calculation: { name: '计算公式', fieldName: `calculation_${Date.now()}` },
    userSelect: { name: '联系人选择', fieldName: `user_select_${Date.now()}` },
    deptSelect: { name: '部门选择', fieldName: `dept_select_${Date.now()}` },
    roleSelect: { name: '角色选择', fieldName: `role_select_${Date.now()}` }
  }
  
  const controlInfo = controlTypes[type as keyof typeof controlTypes]
  const newControl = {
    id: Date.now(),
    type: type,
    label: controlInfo.name,
    fieldName: controlInfo.fieldName,
    placeholder: `请选择${controlInfo.name}`,
    value: type === 'checkbox' || type === 'userSelect' || type === 'deptSelect' || type === 'roleSelect' ? [] : '',
    required: false,
    disabled: false,
    options: type === 'radio' || type === 'checkbox' ? [{ label: '选项1', value: '1' }, { label: '选项2', value: '2' }] : [],
    multiple: type === 'checkbox' || type === 'userSelect' || type === 'deptSelect' || type === 'roleSelect'
  }
  
  formControls.value.push(newControl)
}

// 获取控件组件
const getControlComponent = (type: string) => {
  // 根据控件类型返回对应的Element Plus组件
  switch (type) {
    case 'singleInput':
    case 'multiInput':
    case 'number':
    case 'idCard':
    case 'phone':
    case 'amount':
      return 'el-input'
    case 'radio':
      return 'el-radio-group'
    case 'checkbox':
      return 'el-checkbox-group'
    case 'date':
    case 'dateRange':
      return 'el-date-picker'
    case 'classification':
      return 'el-cascader'
    case 'image':
    case 'attachment':
      return 'el-upload'
    case 'table':
      return 'el-table'
    case 'signature':
      return 'el-input'
    case 'calculation':
      return 'el-input'
    case 'userSelect':
    case 'deptSelect':
    case 'roleSelect':
      return 'el-select'
    default:
      return 'el-input'
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
}

// 编辑控件
const editControl = (_index: number) => {
  // 这里可以打开控件编辑弹窗
  ElMessage.info('编辑控件功能待实现')
}

// 节点选择事件处理
const onNodeSelect = (e: any) => {
  if (e.nodes.length > 0) {
    selectedNode.value = elements.value.find(node => node.id === e.nodes[0]) as Node
  } else {
    selectedNode.value = null
  }
}

// 连接添加事件处理
const onConnectionAdded = (_e: any) => {
  // 处理连接添加逻辑
  ElMessage.success('连接添加成功')
}

// 添加开始节点
const addStartNode = () => {
  const startNode: Node = {
    id: `start-${Date.now()}`,
    type: 'start',
    position: { x: 100, y: 200 },
    data: {
      label: '开始'
    }
  }
  elements.value.push(startNode)
}

// 添加审批节点
const addTaskNode = () => {
  const taskNode: Node = {
    id: `task-${Date.now()}`,
    type: 'task',
    position: { x: 300, y: 200 },
    data: {
      label: '审批节点',
      approverType: 'fixed', // fixed: 固定人员, deptManager: 部门主管, deptRole: 部门角色
      approvers: [],
      deptField: '',
      roleId: '',
      approvalType: 'or' // and: 会签, or: 或签
    }
  }
  elements.value.push(taskNode)
}

// 添加结束节点
const addEndNode = () => {
  const endNode: Node = {
    id: `end-${Date.now()}`,
    type: 'end',
    position: { x: 500, y: 200 },
    data: {
      label: '结束'
    }
  }
  elements.value.push(endNode)
}

// 保存流程设计
const saveFlowDesign = () => {
  // 将流程设计转换为BPMN XML（简化版本）
  const flowData = {
    nodes: elements.value.filter(el => 'type' in el),
    edges: elements.value.filter(el => 'source' in el)
  }
  
  // 将流程数据保存为JSON字符串
  processTemplateForm.bpmnXml = JSON.stringify(flowData)
  ElMessage.success('流程设计保存成功')
}

// 加载示例流程
const loadSampleFlow = () => {
  elements.value = [
    {
      id: 'start-1',
      type: 'start',
      position: { x: 100, y: 200 },
      data: { label: '开始' }
    },
    {
      id: 'task-1',
      type: 'task',
      position: { x: 300, y: 200 },
      data: {
        label: '部门经理审批',
        approverType: 'deptManager',
        deptField: 'dept',
        approvalType: 'or'
      }
    },
    {
      id: 'task-2',
      type: 'task',
      position: { x: 500, y: 200 },
      data: {
        label: '总经理审批',
        approverType: 'fixed',
        approvers: [1, 2], // 假设1和2是用户ID
        approvalType: 'or'
      }
    },
    {
      id: 'end-1',
      type: 'end',
      position: { x: 700, y: 200 },
      data: { label: '结束' }
    },
    {
      id: 'edge-1',
      source: 'start-1',
      target: 'task-1'
    },
    {
      id: 'edge-2',
      source: 'task-1',
      target: 'task-2'
    },
    {
      id: 'edge-3',
      source: 'task-2',
      target: 'end-1'
    }
  ]
  ElMessage.success('示例流程加载成功')
}

// 加载用户列表
const loadUserList = async () => {
  try {
    const response = await userService.getUserList({ page: 1, pageSize: 100 })
    if (response.code === 200 && response.data && response.data.records) {
      userList.value = response.data.records
    }
  } catch (error) {
    console.error('加载用户列表失败：', error)
  }
}

// 加载角色列表
const loadRoleList = async () => {
  try {
    const response = await roleService.getRoleList({ page: 1, pageSize: 100 })
    if (response.code === 200 && response.data && response.data.records) {
      roleList.value = response.data.records
    }
  } catch (error) {
    console.error('加载角色列表失败：', error)
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
    ElMessage.success('表单设计保存成功')
    activeStep.value++
  } else if (activeStep.value === 2) {
    // 保存流程设计
    saveFlowDesign()
    activeStep.value++
  }
}

// 保存流程模板
const saveProcessTemplate = async () => {
  try {
    // 合并高级设置到流程模板表单
    processTemplateForm.version = advancedSettings.version
    
    let response
    if (processTemplateId.value) {
      // 更新流程模板
      response = await updateProcessTemplate({ id: processTemplateId.value, ...processTemplateForm })
    } else {
      // 新增流程模板
      response = await saveProcessTemplateApi(processTemplateForm)
    }
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      router.push('/flow/process-template/list')
    } else {
      ElMessage.error('保存失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存流程模板异常：', error)
  }
}

// 返回
const handleBack = () => {
  if (hasUnsavedChanges()) {
    ElMessageBox.confirm('有未保存的更改，确定要离开吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.push('/flow/process-template/list')
    })
  } else {
    router.push('/flow/process-template/list')
  }
}

// 检查是否有未保存的更改
const hasUnsavedChanges = () => {
  // 这里可以实现检查是否有未保存更改的逻辑
  return false
}

// 初始化加载
onMounted(() => {
  loadFormTemplateList()
  loadUserList()
  loadRoleList()
  if (processTemplateId.value) {
    loadProcessTemplateDetail()
  }
})
</script>

<style scoped>
.process-template-edit {
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
}

.toolbox {
  width: 200px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  max-height: 600px;
  overflow-y: auto;
}

.toolbox h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 500;
}

.control-group {
  margin-bottom: 20px;
}

.control-group h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 500;
  color: #606266;
}

.control-items {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-preview {
  flex: 1;
}

.form-preview h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 500;
}

.preview-container {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  max-height: 600px;
  overflow-y: auto;
}

.form-control-item {
  position: relative;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 10px;
}

.form-control-item:hover {
  border-color: #c0c4cc;
}

.control-actions {
  position: absolute;
  right: 10px;
  top: 10px;
  display: flex;
  gap: 5px;
}

.process-design-section {
  min-height: 600px;
}

.process-design-section h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 500;
}

.flow-design-container {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.flow-tools {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.flow-node-palette {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}

.flow-node-palette h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  font-weight: 500;
  color: #606266;
}

.node-items {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.flow-canvas {
  height: 400px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 15px;
  background-color: #fafafa;
}

.flow-tips {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.node-properties-panel {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
}

.node-properties-panel h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 500;
}

.footer {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* Vue Flow 自定义样式 */
:deep(.vue-flow__connection) {
  stroke-width: 2;
}

:deep(.vue-flow__connection-path) {
  stroke: #909399;
}

:deep(.vue-flow__handle) {
  width: 10px;
  height: 10px;
  background-color: #409EFF;
}
</style>
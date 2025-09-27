<template>
  <el-dialog
    v-model="dialogVisible"
    title="部门信息"
    width="600px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      class="form-container"
    >
      <el-form-item label="上级部门" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="departmentTreeOptions"
          placeholder="请选择上级部门"
          :props="{ label: 'departmentName', value: 'id', children: 'children' }"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="部门名称" prop="departmentName">
        <el-input v-model="formData.departmentName" placeholder="请输入部门名称" />
      </el-form-item>
      
      <el-form-item label="部门负责人" prop="leader">
        <el-input v-model="formData.leader" placeholder="请输入部门负责人" />
      </el-form-item>
      
      <el-form-item label="部门电话" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入部门电话" />
      </el-form-item>
      
      <el-form-item label="部门邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入部门邮箱" />
      </el-form-item>
      
      <el-form-item label="排序" prop="orderNum">
        <el-input-number v-model="formData.orderNum" :min="0" :step="1" placeholder="请输入排序号" />
      </el-form-item>
      
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import departmentService from '@/services/system/department'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  departmentData: {
    type: Object,
    default: null
  },
  parentDepartmentId: {
    type: String,
    default: '0'
  }
})

// Emits
const emit = defineEmits(['close', 'success'])

// Data
const dialogVisible = ref(false)
const formRef = ref<any>(null)
const formData = reactive<any>({
  id: '',
  parentId: '0',
  departmentName: '',
  leader: '',
  phone: '',
  email: '',
  orderNum: 0,
  status: 1
})
const departmentTreeOptions = ref<any[]>([])

// Form rules
const formRules = reactive({
  departmentName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 1, max: 50, message: '部门名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入排序号', trigger: 'blur' },
    { type: 'number', min: 0, message: '排序号不能小于0', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^\d{10,20}$/, message: '请输入正确的电话号码格式', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
})

// Watch dialog visible
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal
  if (newVal) {
    initForm()
  }
})

// Init form data
const initForm = async () => {
  // Reset form
  Object.keys(formData).forEach(key => {
    if (['orderNum', 'status'].includes(key)) {
      formData[key] = 1
    } else if (key === 'parentId') {
      formData[key] = props.parentDepartmentId || '0'
    } else {
      formData[key] = ''
    }
  })
  
  // Load department tree options (excluding current department if editing)
  await loadDepartmentTreeOptions()
  
  // If editing existing department
  if (props.departmentData) {
    Object.assign(formData, props.departmentData)
  }
  
  // Reset form validation
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// Load department tree options
const loadDepartmentTreeOptions = async () => {
  try {
    const response = await departmentService.getDepartmentTree()
    let departmentTree = response.data.data || []
    
    // If editing, remove current department from options to prevent self-reference
    if (formData.id) {
      departmentTree = filterDepartmentTree(departmentTree, formData.id)
    }
    
    departmentTreeOptions.value = departmentTree
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  }
}

// Filter department tree to exclude specific department and its children
const filterDepartmentTree = (tree: any[], excludeId: string): any[] => {
  return tree.filter(node => {
    if (node.id === excludeId) {
      return false
    }
    if (node.children && node.children.length > 0) {
      node.children = filterDepartmentTree(node.children, excludeId)
    }
    return true
  })
}

// Handle submit
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // Check if department name is duplicate
    const hasDuplicate = await checkDepartmentNameDuplicate()
    if (hasDuplicate) {
      ElMessage.error('同级部门下名称已存在')
      return
    }
    
    // Submit data
    const submitData = { ...formData }
    
    if (formData.id) {
      // Update department
      await departmentService.updateDepartment(formData.id, submitData)
      ElMessage.success('部门更新成功')
    } else {
      // Add department
      await departmentService.addDepartment(submitData)
      ElMessage.success('部门添加成功')
    }
    
    emit('success')
    handleClose()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

// Check if department name is duplicate
const checkDepartmentNameDuplicate = async (): Promise<boolean> => {
  try {
    const response = await departmentService.checkDepartmentNameDuplicate(
      formData.departmentName,
      formData.parentId,
      formData.id
    )
    return response.data.data || false
  } catch (error) {
    return false
  }
}

// Handle close
const handleClose = () => {
  emit('close')
}

// Initialize
onMounted(() => {
  if (props.visible) {
    initForm()
  }
})
</script>

<style scoped>
.form-container {
  padding: 10px 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
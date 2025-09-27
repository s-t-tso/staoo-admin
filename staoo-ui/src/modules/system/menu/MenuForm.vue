<template>
  <el-dialog
    v-model="dialogVisible"
    title="菜单信息"
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
      <el-form-item label="上级菜单" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="menuTreeOptions"
          placeholder="请选择上级菜单"
          :props="{ label: 'menuName', value: 'id', children: 'children' }"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="formData.menuName" placeholder="请输入菜单名称" />
      </el-form-item>
      
      <el-form-item label="菜单类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择菜单类型" style="width: 100%">
          <el-option label="菜单" value="menu" />
          <el-option label="目录" value="catalog" />
          <el-option label="按钮" value="button" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="路由路径" prop="path" v-if="formData.type !== 'button'">
        <el-input v-model="formData.path" placeholder="请输入路由路径" />
      </el-form-item>
      
      <el-form-item label="组件路径" prop="component" v-if="formData.type !== 'button'">
        <el-input v-model="formData.component" placeholder="请输入组件路径" />
      </el-form-item>
      
      <el-form-item label="菜单图标" prop="icon" v-if="formData.type !== 'button'">
        <el-input v-model="formData.icon" placeholder="请输入图标名称" />
      </el-form-item>
      
      <el-form-item label="权限标识" prop="perms">
        <el-input v-model="formData.perms" placeholder="请输入权限标识" />
      </el-form-item>
      
      <el-form-item label="排序" prop="orderNum">
        <el-input-number v-model="formData.orderNum" :min="0" :step="1" placeholder="请输入排序号" />
      </el-form-item>
      
      <el-form-item label="是否显示" prop="visible" v-if="formData.type !== 'button'">
        <el-radio-group v-model="formData.visible">
          <el-radio :label="1">显示</el-radio>
          <el-radio :label="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="是否缓存" prop="cache" v-if="formData.type === 'menu'">
        <el-radio-group v-model="formData.cache">
          <el-radio :label="1">缓存</el-radio>
          <el-radio :label="0">不缓存</el-radio>
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
import menuService from '@/services/system/menu'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  menuData: {
    type: Object,
    default: null
  },
  parentMenuId: {
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
  menuName: '',
  type: 'menu',
  path: '',
  component: '',
  icon: '',
  perms: '',
  orderNum: 0,
  visible: 1,
  cache: 0
})
const menuTreeOptions = ref<any[]>([])

// Form rules
const formRules = reactive({
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' },
    { min: 1, max: 50, message: '菜单名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  path: [
    { required: true, message: '请输入路由路径', trigger: 'blur', vIf: () => formData.type !== 'button' },
    { min: 1, max: 200, message: '路由路径长度在 1 到 200 个字符', trigger: 'blur', vIf: () => formData.type !== 'button' }
  ],
  component: [
    { required: true, message: '请输入组件路径', trigger: 'blur', vIf: () => formData.type === 'menu' },
    { min: 1, max: 255, message: '组件路径长度在 1 到 255 个字符', trigger: 'blur', vIf: () => formData.type !== 'button' }
  ],
  orderNum: [
    { required: true, message: '请输入排序号', trigger: 'blur' },
    { type: 'number', min: 0, message: '排序号不能小于0', trigger: 'blur' }
  ],
  perms: [
    { min: 0, max: 100, message: '权限标识长度不能超过 100 个字符', trigger: 'blur' }
  ]
})

// Watch dialog visible
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal
  if (newVal) {
    initForm()
  }
})

// Watch menu type to show/hide fields
watch(() => formData.type, () => {
  // Clear validation when type changes
  if (formRef.value) {
    formRef.value.clearValidate(['path', 'component', 'icon', 'visible', 'cache'])
  }
})

// Init form data
const initForm = async () => {
  // Reset form
  Object.keys(formData).forEach(key => {
    if (['orderNum', 'visible'].includes(key)) {
      formData[key] = 1
    } else if (key === 'cache') {
      formData[key] = 0
    } else if (key === 'parentId') {
      formData[key] = props.parentMenuId || '0'
    } else {
      formData[key] = ''
    }
  })
  
  // Load menu tree options (excluding current menu if editing)
  await loadMenuTreeOptions()
  
  // If editing existing menu
  if (props.menuData) {
    Object.assign(formData, props.menuData)
  }
  
  // Reset form validation
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// Load menu tree options
const loadMenuTreeOptions = async () => {
  try {
    const response = await menuService.getMenuTree()
    let menuTree = response.data.data || []
    
    // If editing, remove current menu from options to prevent self-reference
    if (formData.id) {
      menuTree = filterMenuTree(menuTree, formData.id)
    }
    
    menuTreeOptions.value = menuTree
  } catch (error) {
    ElMessage.error('加载菜单列表失败')
  }
}

// Filter menu tree to exclude specific menu and its children
const filterMenuTree = (tree: any[], excludeId: string): any[] => {
  return tree.filter(node => {
    if (node.id === excludeId) {
      return false
    }
    if (node.children && node.children.length > 0) {
      node.children = filterMenuTree(node.children, excludeId)
    }
    return true
  })
}

// Handle submit
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // Check if menu name is duplicate
    const hasDuplicate = await checkMenuNameDuplicate()
    if (hasDuplicate) {
      ElMessage.error('同级菜单下名称已存在')
      return
    }
    
    // Submit data
    const submitData = { ...formData }
    
    if (formData.id) {
      // Update menu
      await menuService.updateMenu(formData.id, submitData)
      ElMessage.success('菜单更新成功')
    } else {
      // Add menu
      await menuService.addMenu(submitData)
      ElMessage.success('菜单添加成功')
    }
    
    emit('success')
    handleClose()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

// Check if menu name is duplicate
const checkMenuNameDuplicate = async (): Promise<boolean> => {
  try {
    const response = await menuService.checkMenuNameDuplicate(
      formData.menuName,
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
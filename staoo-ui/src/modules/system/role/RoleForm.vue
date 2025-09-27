<template>
  <el-dialog
    v-model="dialogVisible"
    title="角色信息"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      class="form-container"
    >
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="formData.roleCode" placeholder="请输入角色编码" />
      </el-form-item>
      
      <el-form-item label="描述" prop="description">
        <el-input v-model="formData.description" type="textarea" placeholder="请输入角色描述" />
      </el-form-item>
      
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="菜单权限" v-if="formData.id">
        <div class="menu-permission-container">
          <el-tree
            ref="menuTreeRef"
            :data="menuTree"
            :props="{ label: 'menuName', children: 'children' }"
            show-checkbox
            node-key="id"
            :check-strictly="false"
            v-model:checkedKeys="formData.menuIds"
          />
        </div>
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
import roleService from '@/services/system/role'
import menuService from '@/services/system/menu'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  roleData: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['close', 'success'])

// Data
const dialogVisible = ref(false)
const formRef = ref<any>(null)
const menuTreeRef = ref<any>(null)
const formData = reactive<any>({
  id: '',
  roleName: '',
  roleCode: '',
  description: '',
  status: 1,
  menuIds: [] as string[]
})
const menuTree = ref<any[]>([])

// Form rules
const formRules = reactive({
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '角色编码只能包含字母、数字和下划线', trigger: 'blur' },
    { min: 2, max: 30, message: '角色编码长度在 2 到 30 个字符', trigger: 'blur' }
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
    if (key === 'status') {
      formData[key] = 1
    } else if (key === 'menuIds') {
      formData[key] = []
    } else {
      formData[key] = ''
    }
  })
  
  // If editing existing role
  if (props.roleData) {
    Object.assign(formData, props.roleData)
    // Convert menuIds to array if necessary
    if (typeof formData.menuIds === 'string') {
      formData.menuIds = formData.menuIds.split(',').filter((id: string) => id)
    }
    
    // Load menu tree and role permissions
    await loadMenuTree()
  }
  
  // Reset form validation
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// Load menu tree
const loadMenuTree = async () => {
  try {
    const response = await menuService.getMenuTree()
    menuTree.value = response.data.data || []
    
    // If editing existing role, load its menu permissions
    if (formData.id) {
      try {
        const permResponse = await roleService.getRoleMenus(formData.id)
        formData.menuIds = permResponse.data.data || []
      } catch (error) {
        ElMessage.error('加载角色权限失败')
      }
    }
  } catch (error) {
    ElMessage.error('加载菜单列表失败')
  }
}

// Handle submit
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // Submit data
    const submitData = { ...formData }
    
    if (formData.id) {
      // Update role
      await roleService.updateRole(formData.id, submitData)
      
      // Update role menus if needed
      if (formData.menuIds && formData.menuIds.length > 0) {
        await roleService.configureRoleMenus(formData.id, formData.menuIds)
      }
      
      ElMessage.success('角色更新成功')
    } else {
      // Add role
      await roleService.addRole(submitData)
      ElMessage.success('角色添加成功')
    }
    
    emit('success')
    handleClose()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
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

.menu-permission-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
}
</style>
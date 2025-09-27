<template>
  <el-dialog
    v-model="dialogVisible"
    title="用户信息"
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
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" />
      </el-form-item>
      
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="formData.nickname" placeholder="请输入昵称" />
      </el-form-item>
      
      <el-form-item label="密码" v-if="!formData.id">
        <el-input v-model="formData.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      
      <el-form-item label="确认密码" v-if="!formData.id">
        <el-input v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
      </el-form-item>
      
      <el-form-item label="部门" prop="departmentId">
        <el-tree-select
          v-model="formData.departmentId"
          :data="departmentTree"
          placeholder="请选择部门"
          :props="{ label: 'departmentName', value: 'id', children: 'children' }"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="角色" prop="roleIds">
        <el-select
          v-model="formData.roleIds"
          multiple
          placeholder="请选择角色"
          style="width: 100%"
        >
          <el-option
            v-for="role in roleList"
            :key="role.id"
            :label="role.roleName"
            :value="role.id"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>
      
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号" />
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
import userService from '@/services/system/user'
import departmentService from '@/services/system/department'
import roleService from '@/services/system/role'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  userData: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['close', 'success'])

// Data
const dialogVisible = ref(false)
const formRef = ref<any>(null)
const formData = reactive<any>({
  id: '',
  username: '',
  nickname: '',
  password: '',
  departmentId: '',
  roleIds: [] as string[],
  email: '',
  phone: '',
  status: 1
})
const confirmPassword = ref('')
const departmentTree = ref<any[]>([])
const roleList = ref<any[]>([])

// Form rules
const formRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  departmentId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  roleIds: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
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
    } else if (key === 'roleIds') {
      formData[key] = []
    } else {
      formData[key] = ''
    }
  })
  confirmPassword.value = ''
  
  // Load departments and roles
  await Promise.all([loadDepartments(), loadRoles()])
  
  // If editing existing user
  if (props.userData) {
    Object.assign(formData, props.userData)
    // Convert roleIds to array if necessary
    if (typeof formData.roleIds === 'string') {
      formData.roleIds = formData.roleIds.split(',').filter((id: string) => id)
    }
  }
  
  // Reset form validation
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// Load departments
const loadDepartments = async () => {
  try {
    const response = await departmentService.getDepartmentTree()
    departmentTree.value = response.data.data || []
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  }
}

// Load roles
const loadRoles = async () => {
  try {
    const response = await roleService.getRoleList({ pageSize: 1000 })
    roleList.value = response.data.data?.list || []
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  }
}

// Handle submit
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // Check password confirmation
    if (!formData.id && formData.password !== confirmPassword.value) {
      ElMessage.error('两次输入的密码不一致')
      return
    }
    
    // Submit data
    const submitData = { ...formData }
    
    if (formData.id) {
      // Update user
      await userService.updateUser(formData.id, submitData)
      ElMessage.success('用户更新成功')
    } else {
      // Add user
      await userService.addUser(submitData)
      ElMessage.success('用户添加成功')
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
</style>
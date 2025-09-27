<template>
  <div class="user-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <div class="header-actions">
            <el-upload
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :action="importUrl"
              class="upload-demo"
            >
              <el-button type="primary" plain icon="Upload" style="margin-right: 10px;">导入用户</el-button>
            </el-upload>
            <el-button type="primary" plain @click="handleExport">导出用户</el-button>
            <el-button type="primary" icon="Plus" @click="handleAdd">新增用户</el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-input v-model="searchParams.username" placeholder="用户名" style="width: 200px; margin-right: 10px;"></el-input>
        <el-input v-model="searchParams.nickname" placeholder="昵称" style="width: 200px; margin-right: 10px;"></el-input>
        <el-select v-model="searchParams.roleId" placeholder="角色" style="width: 200px; margin-right: 10px;">
          <el-option label="全部" value=""></el-option>
          <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id"></el-option>
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      
      <!-- 数据列表 -->
      <el-table v-loading="loading" :data="userList" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
        <el-table-column prop="departmentName" label="部门" width="150"></el-table-column>
        <el-table-column prop="roleNames" label="角色" width="180"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleResetPassword(scope.row)">重置密码</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>
    
    <!-- 用户表单对话框 -->
    <UserForm
      :visible="userFormVisible"
      :user-data="currentUserData"
      @close="handleFormClose"
      @success="handleFormSuccess"
    />
    
    <!-- 重置密码对话框 -->
    <el-dialog
      v-model="resetPasswordVisible"
      title="重置密码"
      width="400px"
      :before-close="handleResetPasswordClose"
    >
      <el-form ref="resetPasswordFormRef" :model="resetPasswordForm" :rules="resetPasswordRules" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="resetPasswordForm.newPassword" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPasswordForm.confirmPassword" type="password" placeholder="请再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleResetPasswordClose">取消</el-button>
          <el-button type="primary" @click="handleResetPasswordSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import UserForm from './UserForm.vue'
import userService from '@/services/system/user'
import roleService from '@/services/system/role'

// 搜索参数
const searchParams = reactive({
  username: '',
  nickname: '',
  roleId: ''
})

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 数据列表
const userList = ref<any[]>([])
const roleList = ref<any[]>([])
const loading = ref(false)
const userFormVisible = ref(false)
const resetPasswordVisible = ref(false)
const currentUserData = ref<any>(null)
const resetPasswordFormRef = ref<any>(null)
const resetPasswordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})
const resetPasswordRules = reactive({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    {
      validator: (_rule: any, value: string, callback: any) => {
        if (value !== resetPasswordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})
const importUrl = '/api/system/user/import'

// 初始化加载数据
const initData = async () => {
  await Promise.all([fetchUserList(), fetchRoleList()])
}

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchParams,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const response = await userService.getUserList(params)
    userList.value = response.data.data?.list || []
    pagination.total = response.data.data?.total || 0
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    // 模拟数据，避免页面空白
    userList.value = [
      {
        id: '1',
        username: 'admin',
        nickname: '管理员',
        departmentName: '技术部',
        roleNames: '超级管理员',
        status: 1,
        createTime: '2023-01-01 10:00:00'
      },
      {
        id: '2',
        username: 'user1',
        nickname: '用户一',
        departmentName: '市场部',
        roleNames: '普通用户',
        status: 1,
        createTime: '2023-01-02 10:00:00'
      }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

// 获取角色列表
const fetchRoleList = async () => {
  try {
    const response = await roleService.getRoleList({ pageSize: 1000 })
    roleList.value = response.data.data?.list || []
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchUserList()
}

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key as keyof typeof searchParams] = ''
  })
  pagination.pageNum = 1
  fetchUserList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchUserList()
}

// 分页当前页变化
const handleCurrentChange = (current: number) => {
  pagination.pageNum = current
  fetchUserList()
}

// 新增用户
const handleAdd = () => {
  currentUserData.value = null
  userFormVisible.value = true
}

// 编辑用户
const handleEdit = (row: any) => {
  currentUserData.value = { ...row }
  userFormVisible.value = true
}

// 处理表单关闭
const handleFormClose = () => {
  userFormVisible.value = false
  currentUserData.value = null
}

// 处理表单提交成功
const handleFormSuccess = () => {
  fetchUserList()
}

// 切换用户状态
const handleStatusChange = async (row: any) => {
  try {
    await userService.changeUserStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = !row.status // 回滚状态
    ElMessage.error('状态更新失败')
  }
}

// 删除用户
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户「${row.nickname}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await userService.deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUserList()
  } catch (error) {
    // 用户取消删除或操作失败
  }
}

// 重置密码
const handleResetPassword = (row: any) => {
  currentUserData.value = { ...row }
  resetPasswordForm.newPassword = ''
  resetPasswordForm.confirmPassword = ''
  resetPasswordVisible.value = true
}

// 重置密码对话框关闭
const handleResetPasswordClose = () => {
  resetPasswordVisible.value = false
  if (resetPasswordFormRef.value) {
    resetPasswordFormRef.value.clearValidate()
  }
}

// 重置密码提交
const handleResetPasswordSubmit = async () => {
  if (!resetPasswordFormRef.value || !currentUserData.value) return
  
  try {
    await resetPasswordFormRef.value.validate()
    await userService.resetPassword(currentUserData.value.id, resetPasswordForm.newPassword)
    ElMessage.success('密码重置成功')
    handleResetPasswordClose()
  } catch (error) {
    ElMessage.error('密码重置失败')
  }
}

// 导入用户前校验
const handleBeforeUpload = (file: File) => {
  const isXlsx = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.name.endsWith('.xlsx')
  if (!isXlsx) {
    ElMessage.error('只能上传 .xlsx 格式的文件')
    return false
  }
  return true
}

// 导入用户成功
const handleUploadSuccess = () => {
  ElMessage.success('用户导入成功')
  fetchUserList()
}

// 导入用户失败
const handleUploadError = () => {
  ElMessage.error('用户导入失败')
}

// 导出用户
const handleExport = async () => {
  try {
    const response = await userService.exportUsers(searchParams)
    const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `用户列表_${new Date().toLocaleDateString()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    ElMessage.error('用户导出失败')
  }
}

// 初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.user-list {
  width: 100%;
  height: 100%;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 5px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
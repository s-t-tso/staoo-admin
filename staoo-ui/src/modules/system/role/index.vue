<template>
  <div class="role-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增角色</el-button>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-input v-model="searchParams.roleName" placeholder="角色名称" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      
      <!-- 数据列表 -->
      <el-table v-loading="loading" :data="roleList" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码" width="150"></el-table-column>
        <el-table-column prop="description" label="描述" width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleConfigureMenu(scope.row)">菜单配置</el-button>
            <el-button type="primary" link size="small" @click="handleConfigureDataPermission(scope.row)">数据权限</el-button>
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
    
    <!-- 角色表单对话框 -->
    <RoleForm
      :visible="roleFormVisible"
      :role-data="currentRoleData"
      @close="handleFormClose"
      @success="handleFormSuccess"
    />
    
    <!-- 菜单权限配置对话框 -->
    <el-dialog
      v-model="menuConfigVisible"
      title="菜单权限配置"
      width="500px"
      :before-close="handleMenuConfigClose"
    >
      <div class="menu-permission-container">
        <el-tree
          ref="menuTreeRef"
          :data="menuTree"
          :props="{ label: 'menuName', children: 'children' }"
          show-checkbox
          node-key="id"
          :check-strictly="false"
          v-model:checkedKeys="checkedMenuIds"
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleMenuConfigClose">取消</el-button>
          <el-button type="primary" @click="handleMenuConfigSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 数据权限配置对话框 -->
    <el-dialog
      v-model="dataPermissionVisible"
      title="数据权限配置"
      width="600px"
      :before-close="handleDataPermissionClose"
    >
      <el-form ref="dataPermissionFormRef" :model="dataPermissionForm" :rules="dataPermissionRules" label-width="100px">
        <el-form-item label="数据权限类型" prop="dataType">
          <el-radio-group v-model="dataPermissionForm.dataType" @change="handleDataTypeChange">
            <el-radio :label="1">全部数据权限</el-radio>
            <el-radio :label="2">自定义数据权限</el-radio>
            <el-radio :label="3">部门数据权限</el-radio>
            <el-radio :label="4">本人数据权限</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="部门权限" prop="departmentIds" v-if="dataPermissionForm.dataType === 2">
          <el-tree-select
            v-model="dataPermissionForm.departmentIds"
            :data="departmentTree"
            placeholder="请选择部门"
            multiple
            :props="{ label: 'departmentName', value: 'id', children: 'children' }"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDataPermissionClose">取消</el-button>
          <el-button type="primary" @click="handleDataPermissionSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import RoleForm from './RoleForm.vue'
import roleService from '@/services/system/role'
import menuService from '@/services/system/menu'
import departmentService from '@/services/system/department'

// 搜索参数
const searchParams = reactive({
  roleName: ''
})

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 数据列表
const roleList = ref<any[]>([])
const loading = ref(false)
const roleFormVisible = ref(false)
const menuConfigVisible = ref(false)
const dataPermissionVisible = ref(false)
const currentRoleData = ref<any>(null)
const menuTree = ref<any[]>([])
const checkedMenuIds = ref<string[]>([])
const menuTreeRef = ref<any>(null)
const departmentTree = ref<any[]>([])
const dataPermissionFormRef = ref<any>(null)
const dataPermissionForm = reactive({
  dataType: 1,
  departmentIds: [] as string[]
})
const dataPermissionRules = reactive({
  dataType: [
    { required: true, message: '请选择数据权限类型', trigger: 'change' }
  ],
  departmentIds: [
    {
      required: true,
      message: '请选择部门权限',
      trigger: 'change',
      vIf: () => dataPermissionForm.dataType === 2
    }
  ]
})

// 初始化加载数据
const initData = async () => {
  await fetchRoleList()
}

// 获取角色列表
  const fetchRoleList = async () => {
    loading.value = true
    try {
      const params = {
        ...searchParams,
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize
      }
      const response = await roleService.getRoleList(params)
      roleList.value = response.data.data?.list || []
      pagination.total = response.data.data?.total || 0
    } catch (error) {
      ElMessage.error('获取角色列表失败')
      roleList.value = []
      pagination.total = 0
    } finally {
      loading.value = false
    }
  }

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchRoleList()
}

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key as keyof typeof searchParams] = ''
  })
  pagination.pageNum = 1
  fetchRoleList()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchRoleList()
}

// 分页当前页变化
const handleCurrentChange = (current: number) => {
  pagination.pageNum = current
  fetchRoleList()
}

// 新增角色
const handleAdd = () => {
  currentRoleData.value = null
  roleFormVisible.value = true
}

// 编辑角色
const handleEdit = (row: any) => {
  currentRoleData.value = { ...row }
  roleFormVisible.value = true
}

// 处理表单关闭
const handleFormClose = () => {
  roleFormVisible.value = false
  currentRoleData.value = null
}

// 处理表单提交成功
const handleFormSuccess = () => {
  fetchRoleList()
}

// 切换角色状态
const handleStatusChange = async (row: any) => {
  try {
    await roleService.updateRole(row.id, { status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = !row.status // 回滚状态
    ElMessage.error('状态更新失败')
  }
}

// 删除角色
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除角色「${row.roleName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await roleService.deleteRole(row.id)
    ElMessage.success('删除成功')
    fetchRoleList()
  } catch (error) {
    // 用户取消删除或操作失败
  }
}

// 配置菜单权限
const handleConfigureMenu = async (row: any) => {
  currentRoleData.value = { ...row }
  
  try {
    // 加载菜单树和角色已有权限
    const [menuResponse, permResponse] = await Promise.all([
      menuService.getMenuTree(),
      roleService.getRoleMenus(row.id)
    ])
    
    menuTree.value = menuResponse.data.data || []
    checkedMenuIds.value = permResponse.data.data || []
    menuConfigVisible.value = true
  } catch (error) {
    ElMessage.error('加载菜单权限失败')
  }
}

// 关闭菜单配置对话框
const handleMenuConfigClose = () => {
  menuConfigVisible.value = false
  checkedMenuIds.value = []
}

// 提交菜单配置
const handleMenuConfigSubmit = async () => {
  if (!currentRoleData.value) return
  
  try {
    await roleService.configureRoleMenus(currentRoleData.value.id, checkedMenuIds.value)
    ElMessage.success('菜单权限配置成功')
    handleMenuConfigClose()
  } catch (error) {
    ElMessage.error('菜单权限配置失败')
  }
}

// 配置数据权限
const handleConfigureDataPermission = async (row: any) => {
  currentRoleData.value = { ...row }
  
  try {
    // 加载部门树和角色已有数据权限
    const [deptResponse, permResponse] = await Promise.all([
      departmentService.getDepartmentTree(),
      roleService.getRoleDataPermission(row.id)
    ])
    
    departmentTree.value = deptResponse.data.data || []
    
    if (permResponse.data.data) {
      Object.assign(dataPermissionForm, permResponse.data.data)
      // 确保departmentIds是数组
      if (typeof dataPermissionForm.departmentIds === 'string') {
        dataPermissionForm.departmentIds = (dataPermissionForm.departmentIds as string).split(',').filter((id: string) => id)
      }
    } else {
      // 默认值
      dataPermissionForm.dataType = 1
      dataPermissionForm.departmentIds = []
    }
    
    dataPermissionVisible.value = true
  } catch (error) {
    ElMessage.error('加载数据权限失败')
  }
}

// 关闭数据权限配置对话框
const handleDataPermissionClose = () => {
  dataPermissionVisible.value = false
  if (dataPermissionFormRef.value) {
    dataPermissionFormRef.value.clearValidate()
  }
}

// 提交数据权限配置
const handleDataPermissionSubmit = async () => {
  if (!dataPermissionFormRef.value || !currentRoleData.value) return
  
  try {
    await dataPermissionFormRef.value.validate()
    await roleService.configureRoleDataPermission(currentRoleData.value.id, dataPermissionForm)
    ElMessage.success('数据权限配置成功')
    handleDataPermissionClose()
  } catch (error) {
    ElMessage.error('数据权限配置失败')
  }
}

// 处理数据权限类型变化
const handleDataTypeChange = () => {
  if (dataPermissionForm.dataType !== 2) {
    dataPermissionForm.departmentIds = []
  }
}

// 初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.role-list {
  width: 100%;
  height: 100%;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
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

.menu-permission-container {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
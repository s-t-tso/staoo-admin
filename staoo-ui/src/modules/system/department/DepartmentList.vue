<template>
  <div class="department-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增部门</el-button>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-input v-model="searchParams.departmentName" placeholder="部门名称" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      
      <!-- 数据列表 - 树形表格 -->
      <el-table
        v-loading="loading"
        :data="departmentTree"
        style="width: 100%"
        :row-key="(row: any) => row.id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column type="selection" width="55" :selectable="selectable" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="departmentName" label="部门名称" width="180" />
        <el-table-column prop="leaderName" label="负责人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.status" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleAddSubDept(row)">添加子部门</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 部门表单对话框 -->
    <DepartmentForm
      :visible="deptFormVisible"
      :dept-data="currentDeptData"
      :parent-dept-id="currentParentDeptId"
      @close="handleFormClose"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import DepartmentForm from './DepartmentForm.vue'
import departmentService from '@/services/system/department'

// 搜索参数
const searchParams = reactive({
  departmentName: ''
})

// 数据列表
const departmentTree = ref<any[]>([])
const loading = ref(false)
const deptFormVisible = ref(false)
const currentDeptData = ref<any>(null)
const currentParentDeptId = ref<string>('')

// 初始化加载数据
const initData = async () => {
  await fetchDepartmentList()
}

// 获取部门列表
const fetchDepartmentList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchParams
    }
    const response = await departmentService.getDepartmentList(params)
    departmentTree.value = response.data.data || []
  } catch (error) {
    ElMessage.error('获取部门列表失败')
    departmentTree.value = []
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  fetchDepartmentList()
}

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key as keyof typeof searchParams] = ''
  })
  fetchDepartmentList()
}

// 新增部门
const handleAdd = () => {
  currentDeptData.value = null
  currentParentDeptId.value = ''
  deptFormVisible.value = true
}

// 添加子部门
const handleAddSubDept = (row: any) => {
  currentDeptData.value = null
  currentParentDeptId.value = row.id
  deptFormVisible.value = true
}

// 编辑部门
const handleEdit = (row: any) => {
  currentDeptData.value = { ...row }
  currentParentDeptId.value = row.parentId || ''
  deptFormVisible.value = true
}

// 处理表单关闭
const handleFormClose = () => {
  deptFormVisible.value = false
  currentDeptData.value = null
  currentParentDeptId.value = ''
}

// 处理表单提交成功
const handleFormSuccess = () => {
  fetchDepartmentList()
}

// 切换部门状态
const handleStatusChange = async (row: any) => {
  try {
    await departmentService.updateDepartment(row.id, { status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = !row.status // 回滚状态
    ElMessage.error('状态更新失败')
  }
}

// 删除部门
const handleDelete = async (row: any) => {
  try {
    // 检查是否有子部门
    if (row.children && row.children.length > 0) {
      ElMessage.warning('请先删除子部门')
      return
    }
    
    // 检查是否有用户
    const hasUsers = await checkDepartmentHasUsers(row.id)
    if (hasUsers) {
      ElMessage.warning('部门下存在用户，无法删除')
      return
    }
    
    await ElMessageBox.confirm(`确定要删除部门「${row.departmentName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await departmentService.deleteDepartment(row.id)
    ElMessage.success('删除成功')
    fetchDepartmentList()
  } catch (error) {
    // 用户取消删除或操作失败
  }
}

// 检查部门是否有用户
const checkDepartmentHasUsers = async (deptId: string): Promise<boolean> => {
  try {
    const response = await departmentService.getDepartmentUsers(deptId)
    return response.data.data && response.data.data.length > 0
  } catch (error) {
    // 接口调用失败，默认返回false
    return false
  }
}

// 选择器 - 排除禁用项
const selectable = (row: any) => {
  return row.status === 1
}

// 初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.department-list {
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
</style>
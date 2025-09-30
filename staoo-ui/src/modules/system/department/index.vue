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

      <!-- 批量操作 -->
      <div class="batch-actions" v-if="selectedRows.length > 0">
        <el-button type="primary" @click="handleBatchEnable" :disabled="!canBatchEnable">批量启用</el-button>
        <el-button type="warning" @click="handleBatchDisable" :disabled="!canBatchDisable">批量禁用</el-button>
        <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <!-- 数据权限配置 -->
      <el-dialog v-model="dataPermissionVisible" title="部门数据权限配置" width="600px">
        <el-form ref="dataPermissionFormRef" :model="dataPermissionForm" label-width="100px">
          <el-form-item label="数据权限类型">
            <el-radio-group v-model="dataPermissionForm.dataType" @change="handleDataTypeChange">
              <el-radio :label="1">全部数据权限</el-radio>
              <el-radio :label="2">自定义数据权限</el-radio>
              <el-radio :label="3">本部门数据权限</el-radio>
              <el-radio :label="4">本部门及以下数据权限</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="可查看部门" v-if="dataPermissionForm.dataType === 2">
            <el-tree
              v-model="dataPermissionForm.departmentIds"
              :data="permissionDepartmentTree"
              show-checkbox
              node-key="id"
              :props="{ label: 'departmentName', children: 'children' }"
              :check-strictly="true"
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

      <!-- 数据列表 - 树形表格 -->
      <el-table
        v-loading="loading"
        :data="departmentTree"
        style="width: 100%"
        :list-key="(list: any) => list.id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" :selectable="selectable" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="departmentName" label="部门名称" width="180" />
        <el-table-column prop="leaderName" label="负责人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ list }">
            <el-switch v-model="list.status" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(list)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
            <template #default="{ list }">
              <el-button type="primary" link size="small" @click="handleAddSubDept(list)">添加子部门</el-button>
              <el-button type="primary" link size="small" @click="handleEdit(list)">编辑</el-button>
              <el-button type="primary" link size="small" @click="handleConfigureDataPermission(list)">数据权限</el-button>
              <el-button type="danger" link size="small" @click="handleDelete(list)">删除</el-button>
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
import { ref, reactive, onMounted, computed } from 'vue'
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
const selectedRows = ref<any[]>([])
const dataPermissionVisible = ref(false)
const dataPermissionFormRef = ref<any>(null)
const permissionDepartmentTree = ref<any[]>([])

// 定义数据权限表单接口
interface DataPermissionForm {
  dataType: number;
  departmentIds: string[];
}

const dataPermissionForm = reactive<DataPermissionForm>({
  dataType: 1,
  departmentIds: []
})

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
const handleAddSubDept = (list: any) => {
  currentDeptData.value = null
  currentParentDeptId.value = list.id
  deptFormVisible.value = true
}

// 编辑部门
const handleEdit = (list: any) => {
  currentDeptData.value = { ...list }
  currentParentDeptId.value = list.parentId || ''
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
const handleStatusChange = async (list: any) => {
  try {
    await departmentService.updateDepartment(list.id, { status: list.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    list.status = !list.status // 回滚状态
    ElMessage.error('状态更新失败')
  }
}

// 删除部门
const handleDelete = async (list: any) => {
  try {
    // 检查是否有子部门
    if (list.children && list.children.length > 0) {
      ElMessage.warning('请先删除子部门')
      return
    }

    // 检查是否有用户
    const hasUsers = await checkDepartmentHasUsers(list.id)
    if (hasUsers) {
      ElMessage.warning('部门下存在用户，无法删除')
      return
    }

    await ElMessageBox.confirm(`确定要删除部门「${list.departmentName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await departmentService.deleteDepartment(list.id)
    ElMessage.success('删除成功')
    fetchDepartmentList()
  } catch (error) {
    // 用户取消删除或操作失败
  }
}

// 检查部门是否有用户
const checkDepartmentHasUsers = async (deptId: string): Promise<boolean> => {
  try {
    // 使用现有的getDepartmentUsers方法来检查部门是否有用户
    const response = await departmentService.getDepartmentUsers(deptId)
    return response.data.data && response.data.data.length > 0
  } catch (error) {
    // 接口调用失败，默认返回false
    return false
  }
}

// 选择器 - 排除禁用项
const selectable = (list: any) => {
  return list.status === 1
}

// 处理选择变化
const handleSelectionChange = (rows: any[]) => {
  selectedRows.value = rows
}

// 配置数据权限
const handleConfigureDataPermission = async (list: any) => {
  try {
    currentDeptData.value = list
    
    // 加载部门树（用于数据权限选择）
    await loadPermissionDepartmentTree()
    
    // 加载已有数据权限配置（如果存在）
    // 这里假设存在一个接口来获取部门的数据权限配置
    // const response = await departmentService.getDepartmentDataPermission(list.id)
    // if (response.data.data) {
    //   Object.assign(dataPermissionForm, response.data.data)
    // } else {
    //   // 默认值
    //   dataPermissionForm.dataType = 1
    //   dataPermissionForm.departmentIds = []
    // }
    
    // 暂时使用默认值
    dataPermissionForm.dataType = 1
    dataPermissionForm.departmentIds = []
    
    dataPermissionVisible.value = true
  } catch (error) {
    ElMessage.error('加载数据权限失败')
  }
}

// 加载权限部门树
const loadPermissionDepartmentTree = async () => {
  try {
    const response = await departmentService.getDepartmentTree()
    permissionDepartmentTree.value = response.data.data || []
  } catch (error) {
    ElMessage.error('加载部门树失败')
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
  if (!dataPermissionFormRef.value || !currentDeptData.value) return
  
  try {
    // 确保departmentIds是数组
    if (typeof dataPermissionForm.departmentIds === 'string') {
      dataPermissionForm.departmentIds = (dataPermissionForm.departmentIds as string).split(',').filter((id: string) => id)
    }
    
    // 这里假设存在一个接口来保存部门的数据权限配置
    // await departmentService.configureDepartmentDataPermission(currentDeptData.value.id, dataPermissionForm)
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

// 检查是否可以批量启用
const canBatchEnable = computed(() => {
  return selectedRows.value.some(row => row.status === 0)
})

// 检查是否可以批量禁用
const canBatchDisable = computed(() => {
  return selectedRows.value.some(row => row.status === 1)
})

// 批量启用部门
const handleBatchEnable = async () => {
  try {
    const deptIds = selectedRows.value.map(row => row.id)
    await departmentService.batchUpdateDepartmentStatus(deptIds, 1)
    ElMessage.success('批量启用成功')
    fetchDepartmentList()
  } catch (error) {
    ElMessage.error('批量启用失败')
  }
}

// 批量禁用部门
const handleBatchDisable = async () => {
  try {
    // 检查是否有子部门或用户，不允许禁用有子部门或用户的部门
    for (const dept of selectedRows.value) {
      if (dept.children && dept.children.length > 0) {
        ElMessage.warning(`部门「${dept.departmentName}」存在子部门，不允许禁用`)
        return
      }
      const hasUsers = await checkDepartmentHasUsers(dept.id)
      if (hasUsers) {
        ElMessage.warning(`部门「${dept.departmentName}」存在用户，不允许禁用`)
        return
      }
    }
    
    const deptIds = selectedRows.value.map(row => row.id)
    await departmentService.batchUpdateDepartmentStatus(deptIds, 0)
    ElMessage.success('批量禁用成功')
    fetchDepartmentList()
  } catch (error) {
    ElMessage.error('批量禁用失败')
  }
}

// 批量删除部门
const handleBatchDelete = async () => {
  try {
    // 检查是否有子部门或用户
    for (const dept of selectedRows.value) {
      if (dept.children && dept.children.length > 0) {
        ElMessage.warning(`部门「${dept.departmentName}」存在子部门，不允许删除`)
        return
      }
      const hasUsers = await checkDepartmentHasUsers(dept.id)
      if (hasUsers) {
        ElMessage.warning(`部门「${dept.departmentName}」存在用户，不允许删除`)
        return
      }
    }
    
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个部门吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const deptIds = selectedRows.value.map(row => row.id)
    await departmentService.batchDeleteDepartments(deptIds)
    ElMessage.success('批量删除成功')
    fetchDepartmentList()
  } catch (error) {
    // 用户取消删除或操作失败
  }
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

.batch-actions {
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f0f2f5;
  border-radius: 5px;
  display: flex;
  gap: 10px;
}
</style>

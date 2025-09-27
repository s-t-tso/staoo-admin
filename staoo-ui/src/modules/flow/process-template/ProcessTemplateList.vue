<template>
  <div class="process-template-list">
    <div class="header">
      <h1>流程模板管理</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增流程模板
      </el-button>
    </div>
    
    <div class="search-bar">
      <el-input
        v-model="searchForm.processName"
        placeholder="请输入流程名称"
        style="width: 200px"
        clearable
      />
      <el-input
        v-model="searchForm.processKey"
        placeholder="请输入流程标识"
        style="width: 200px; margin-left: 10px"
        clearable
      />
      <el-select
        v-model="searchForm.status"
        placeholder="请选择状态"
        style="width: 120px; margin-left: 10px"
      >
        <el-option label="全部" value="" />
        <el-option label="草稿" value="DRAFT" />
        <el-option label="已发布" value="PUBLISHED" />
      </el-select>
      <el-button type="primary" @click="handleSearch" style="margin-left: 10px">
        <el-icon><Search /></el-icon>
        搜索
      </el-button>
      <el-button @click="handleReset" style="margin-left: 10px">重置</el-button>
    </div>
    
    <el-table v-loading="loading" :data="processTemplateList" style="width: 100%">
      <el-table-column prop="id" label="模板ID" width="80" />
      <el-table-column prop="processName" label="流程名称" width="180" />
      <el-table-column prop="processKey" label="流程标识" width="180" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" link size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button v-if="scope.row.status === 'DRAFT'" type="primary" link size="small" @click="handlePublish(scope.row.id)">发布</el-button>
          <el-button type="primary" link size="small" @click="handleCopy(scope.row)">复制</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { getProcessTemplateList, publishProcessTemplate, deleteProcessTemplate, copyProcessTemplate } from '../../../services/flow/processTemplateService'

const router = useRouter()

// 加载状态
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  processName: '',
  processKey: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 流程模板列表
const processTemplateList = ref<any[]>([])

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case 'DRAFT':
      return 'warning'
    case 'PUBLISHED':
      return 'success'
    default:
      return 'info'
  }
}

// 获取状态标签文本
const getStatusLabel = (status: string) => {
  switch (status) {
    case 'DRAFT':
      return '草稿'
    case 'PUBLISHED':
      return '已发布'
    default:
      return status
  }
}

// 加载流程模板列表
const loadProcessTemplateList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      tenantId: 1 // 这里需要根据实际情况设置租户ID
    }
    const response = await getProcessTemplateList(params)
    if (response.code === 200 && response.data) {
      processTemplateList.value = response.data.list
      pagination.total = response.data.total
    } else {
      ElMessage.error('获取流程模板列表失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    ElMessage.error('获取流程模板列表失败')
    console.error('获取流程模板列表异常：', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadProcessTemplateList()
}

// 重置
const handleReset = () => {
  searchForm.processName = ''
  searchForm.processKey = ''
  searchForm.status = ''
  pagination.currentPage = 1
  loadProcessTemplateList()
}

// 分页大小改变
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  loadProcessTemplateList()
}

// 当前页改变
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current
  loadProcessTemplateList()
}

// 新建
const handleCreate = () => {
  router.push('/flow/process-template/edit')
}

// 编辑
const handleEdit = (row: any) => {
  router.push(`/flow/process-template/edit?id=${row.id}`)
}

// 发布
const handlePublish = async (id: number) => {
  try {
    ElMessageBox.confirm('确定要发布该流程模板吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      const response = await publishProcessTemplate(id)
      if (response.code === 200) {
        ElMessage.success('发布成功')
        loadProcessTemplateList()
      } else {
        ElMessage.error('发布失败：' + (response.msg || '未知错误'))
      }
    })
  } catch (error) {
    console.error('发布流程模板异常：', error)
  }
}

// 复制
const handleCopy = async (row: any) => {
  try {
    const newName = await ElMessageBox.prompt(`请输入新流程名称（原名称：${row.processName}）`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /^[^\s]+$/, // 不允许空格
      inputErrorMessage: '流程名称不能为空或包含空格'
    })
    
    if (newName.value) {
      const response = await copyProcessTemplate({ id: row.id, newName: newName.value })
      if (response.code === 200) {
        ElMessage.success('复制成功')
        loadProcessTemplateList()
      } else {
        ElMessage.error('复制失败：' + (response.msg || '未知错误'))
      }
    }
  } catch (error) {
    console.error('复制流程模板异常：', error)
  }
}

// 删除
const handleDelete = async (id: number) => {
  try {
    ElMessageBox.confirm('确定要删除该流程模板吗？删除后将无法恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    }).then(async () => {
      const response = await deleteProcessTemplate(id)
      if (response.code === 200) {
        ElMessage.success('删除成功')
        loadProcessTemplateList()
      } else {
        ElMessage.error('删除失败：' + (response.msg || '未知错误'))
      }
    })
  } catch (error) {
    console.error('删除流程模板异常：', error)
  }
}

// 初始化加载
onMounted(() => {
  loadProcessTemplateList()
})
</script>

<style scoped>
.process-template-list {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
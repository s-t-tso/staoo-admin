<template>
  <div class="menu-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-input v-model="searchParams.menuName" placeholder="菜单名称" style="width: 300px; margin-right: 10px;"></el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      
      <!-- 数据列表 - 树形表格 -->
      <el-table
        v-loading="loading"
        :data="menuTree"
        style="width: 100%"
        :row-key="(row: any) => row.id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column type="selection" width="55" :selectable="selectable" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="menuName" label="菜单名称" width="180">
          <template #default="{ row }">
            <div style="display: flex; align-items: center;">
              <el-icon v-if="row.icon && row.menuType !== 2" style="margin-right: 5px;">
                <component :is="row.icon" />
              </el-icon>
              {{ row.menuName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.menuType === 0" type="primary">目录</el-tag>
            <el-tag v-else-if="row.menuType === 1" type="success">菜单</el-tag>
            <el-tag v-else type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" width="200" />
        <el-table-column prop="component" label="组件" width="200" />
        <el-table-column prop="perms" label="权限标识" width="200" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.status" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleAddSubMenu(row)">添加子菜单</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 菜单表单对话框 -->
    <MenuForm
      :visible="menuFormVisible"
      :menu-data="currentMenuData"
      :menu-type="currentMenuType"
      :parent-menu-id="currentParentMenuId"
      @close="handleFormClose"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MenuForm from './MenuForm.vue'
import menuService from '@/services/system/menu'

// 搜索参数
const searchParams = reactive({
  menuName: ''
})

// 数据列表
const menuTree = ref<any[]>([])
const loading = ref(false)
const menuFormVisible = ref(false)
const currentMenuData = ref<any>(null)
const currentMenuType = ref<number>(0)
const currentParentMenuId = ref<string>('')

// 初始化加载数据
const initData = async () => {
  await fetchMenuList()
}

// 获取菜单列表
const fetchMenuList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchParams
    }
    const response = await menuService.getMenuList(params)
    menuTree.value = response.data.data || []
  } catch (error) {
    ElMessage.error('获取菜单列表失败')
    menuTree.value = []
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  fetchMenuList()
}

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key as keyof typeof searchParams] = ''
  })
  fetchMenuList()
}

// 新增菜单
const handleAdd = () => {
  currentMenuData.value = null
  currentMenuType.value = 0
  currentParentMenuId.value = ''
  menuFormVisible.value = true
}

// 添加子菜单
const handleAddSubMenu = (row: any) => {
  currentMenuData.value = null
  currentMenuType.value = row.menuType === 0 ? 1 : 2
  currentParentMenuId.value = row.id
  menuFormVisible.value = true
}

// 编辑菜单
const handleEdit = (row: any) => {
  currentMenuData.value = { ...row }
  currentMenuType.value = row.menuType
  currentParentMenuId.value = row.parentId || ''
  menuFormVisible.value = true
}

// 处理表单关闭
const handleFormClose = () => {
  menuFormVisible.value = false
  currentMenuData.value = null
  currentMenuType.value = 0
  currentParentMenuId.value = ''
}

// 处理表单提交成功
const handleFormSuccess = () => {
  fetchMenuList()
}

// 切换菜单状态
const handleStatusChange = async (row: any) => {
  try {
    await menuService.updateMenu(row.id, { status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = !row.status // 回滚状态
    ElMessage.error('状态更新失败')
  }
}

// 删除菜单
const handleDelete = async (row: any) => {
  try {
    // 检查是否有子菜单
    if (row.children && row.children.length > 0) {
      ElMessage.warning('请先删除子菜单')
      return
    }
    
    await ElMessageBox.confirm(`确定要删除菜单「${row.menuName}」吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await menuService.deleteMenu(row.id)
    ElMessage.success('删除成功')
    fetchMenuList()
  } catch (error) {
    // 用户取消删除或操作失败
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
.menu-list {
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
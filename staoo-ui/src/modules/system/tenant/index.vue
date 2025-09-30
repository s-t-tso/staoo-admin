<template>
  <div class="system-tenant-list">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-input v-model="searchForm.tenantName" placeholder="租户名称" clearable style="width: 200px;" />
      <el-input v-model="searchForm.tenantKey" placeholder="租户标识" clearable style="width: 200px; margin-left: 10px;" />
      <el-select v-model="searchForm.status" placeholder="状态" style="width: 120px; margin-left: 10px;">
        <el-option label="全部" value="" />
        <el-option label="正常" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
      <el-button type="primary" @click="getTenantList" style="margin-left: 10px;">
        <el-icon><Search /></el-icon>
        搜索
      </el-button>
      <el-button @click="resetSearch" style="margin-left: 10px;">
        重置
      </el-button>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-area">
      <el-button type="primary" @click="showAddTenantDialog">
        <el-icon><Plus /></el-icon>
        新增租户
      </el-button>
      <el-button type="danger" @click="batchDeleteTenants" :disabled="selectedRowKeys.length === 0">
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tenantList"
      style="width: 100%"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="租户ID" width="80" />
      <el-table-column prop="tenantName" label="租户名称" width="180" />
      <el-table-column prop="tenantKey" label="租户标识" width="180" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row.id, scope.row.status)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="showEditTenantDialog(scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            link
            @click="deleteTenant(scope.row.id)"
          >
            删除
          </el-button>
          <el-button
            type="primary"
            link
            @click="showUserAssignmentDialog(scope.row.id, scope.row.tenantName)"
          >
            分配用户
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 租户表单对话框 -->
    <tenant-form
      ref="tenantForm"
      :visible="tenantFormVisible"
      :tenant-id="currentTenantId"
      @close="handleTenantFormClose"
      @success="handleTenantFormSuccess"
    />

    <!-- 用户分配对话框 -->
    <el-dialog
      v-model="userAssignmentVisible"
      title="分配用户 - {{ currentTenantName }}"
      width="600px"
    >
      <el-row :gutter="20">
        <el-col :span="11">
          <div class="user-selection-section">
            <el-input
              v-model="unassignedUserSearch"
              placeholder="搜索用户"
              clearable
              @input="filterUnassignedUsers"
              style="margin-bottom: 10px;"
            />
            <el-select
              v-model="selectedUnassignedUsers"
              multiple
              filterable
              remote
              reserve-keyword
              placeholder="请选择用户"
              :remote-method="loadUnassignedUsers"
              :loading="loadingUnassignedUsers"
              :popper-append-to-body="false"
              value-key="id"
              style="width: 100%; height: 300px;"
            >
              <el-option
                v-for="user in filteredUnassignedUsers"
                :key="user.id"
                :label="user.username"
                :value="user"
              />
            </el-select>
          </div>
        </el-col>
        <el-col :span="2" class="action-buttons">
          <div style="display: flex; flex-direction: column; gap: 10px; margin-top: 100px;">
            <el-button type="primary" @click="addSelectedUsers" :disabled="selectedUnassignedUsers.length === 0">
              <el-icon><Right /></el-icon>
            </el-button>
            <el-button type="primary" @click="removeSelectedUsers" :disabled="selectedAssignedUsers.length === 0">
                <el-icon><ArrowLeft /></el-icon>
              </el-button>
          </div>
        </el-col>
        <el-col :span="11">
          <div class="user-assigned-section">
            <el-input
              v-model="assignedUserSearch"
              placeholder="搜索已分配用户"
              clearable
              @input="filterAssignedUsers"
              style="margin-bottom: 10px;"
            />
            <el-select
              v-model="selectedAssignedUsers"
              multiple
              filterable
              remote
              reserve-keyword
              placeholder="已分配用户"
              :remote-method="loadAssignedUsers"
              :loading="loadingAssignedUsers"
              :popper-append-to-body="false"
              value-key="id"
              style="width: 100%; height: 300px;"
            >
              <el-option
                v-for="user in filteredAssignedUsers"
                :key="user.id"
                :label="user.username"
                :value="user"
              />
            </el-select>
          </div>
        </el-col>
      </el-row>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userAssignmentVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUserAssignment">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Search, Plus, Delete, Right, ArrowLeft } from '@element-plus/icons-vue';
import TenantForm from './TenantForm.vue';
import tenantService from '@/services/system/tenant';

// 搜索表单
const searchForm = ref({
  tenantName: '',
  tenantKey: '',
  status: ''
});

// 租户列表数据
const tenantList = ref<any[]>([]);
const loading = ref(false);

// 分页数据
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
});

// 选中的行
const selectedRowKeys = ref<string[]>([]);

// 租户表单对话框
const tenantFormVisible = ref(false);
const currentTenantId = ref('');
const tenantForm = ref<InstanceType<typeof TenantForm>>();

// 用户分配对话框
const userAssignmentVisible = ref(false);
const currentTenantName = ref('');
const unassignedUserSearch = ref('');
const assignedUserSearch = ref('');
const selectedUnassignedUsers = ref<any[]>([]);
const selectedAssignedUsers = ref<any[]>([]);
const unassignedUsers = ref<any[]>([]);
const assignedUsers = ref<any[]>([]);
const loadingUnassignedUsers = ref(false);
const loadingAssignedUsers = ref(false);

// 过滤后的未分配用户
const filteredUnassignedUsers = computed(() => {
  if (!unassignedUserSearch.value) {
    return unassignedUsers.value;
  }
  return unassignedUsers.value.filter(user => 
    user.username.toLowerCase().includes(unassignedUserSearch.value.toLowerCase())
  );
});

// 过滤后的已分配用户
const filteredAssignedUsers = computed(() => {
  if (!assignedUserSearch.value) {
    return assignedUsers.value;
  }
  return assignedUsers.value.filter(user => 
    user.username.toLowerCase().includes(assignedUserSearch.value.toLowerCase())
  );
});

// 获取租户列表
const getTenantList = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchForm.value,
      pageNum: pagination.value.pageNum,
      pageSize: pagination.value.pageSize
    };
    const response = await tenantService.getTenantList(params);
    if (response.code === 200) {
      tenantList.value = response.data.records || [];
      pagination.value.total = response.data.total || 0;
    } else {
      ElMessage.error(response.message || '获取租户列表失败');
    }
  } catch (error) {
    console.error('获取租户列表失败:', error);
    ElMessage.error('获取租户列表失败');
  } finally {
    loading.value = false;
  }
};

// 处理选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRowKeys.value = selection.map(row => row.id);
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    tenantName: '',
    tenantKey: '',
    status: ''
  };
  pagination.value.pageNum = 1;
  getTenantList();
};

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size;
  pagination.value.pageNum = 1;
  getTenantList();
};

// 处理分页当前页变化
const handleCurrentChange = (current: number) => {
  pagination.value.pageNum = current;
  getTenantList();
};

// 显示新增租户对话框
const showAddTenantDialog = () => {
  currentTenantId.value = '';
  tenantFormVisible.value = true;
};

// 显示编辑租户对话框
const showEditTenantDialog = (id: string) => {
  currentTenantId.value = id;
  tenantFormVisible.value = true;
};

// 处理租户表单关闭
const handleTenantFormClose = () => {
  tenantFormVisible.value = false;
  currentTenantId.value = '';
};

// 处理租户表单提交成功
const handleTenantFormSuccess = () => {
  tenantFormVisible.value = false;
  getTenantList();
};

// 删除租户
const deleteTenant = async (id: string) => {
  try {
    const response = await tenantService.deleteTenant(id);
    if (response.code === 200) {
      ElMessage.success('删除成功');
      getTenantList();
    } else {
      ElMessage.error(response.message || '删除失败');
    }
  } catch (error) {
    console.error('删除租户失败:', error);
    ElMessage.error('删除租户失败');
  }
};

// 批量删除租户
const batchDeleteTenants = async () => {
  try {
    const response = await tenantService.batchDeleteTenants(selectedRowKeys.value);
    if (response.code === 200) {
      ElMessage.success('批量删除成功');
      getTenantList();
      selectedRowKeys.value = [];
    } else {
      ElMessage.error(response.message || '批量删除失败');
    }
  } catch (error) {
    console.error('批量删除租户失败:', error);
    ElMessage.error('批量删除租户失败');
  }
};

// 处理租户状态变更
const handleStatusChange = async (id: string, status: number) => {
  try {
    const response = await tenantService.changeTenantStatus(id, status);
    if (response.code !== 200) {
      ElMessage.error(response.message || '状态变更失败');
      getTenantList(); // 重新获取数据以恢复正确状态
    } else {
      ElMessage.success('状态变更成功');
    }
  } catch (error) {
    console.error('状态变更失败:', error);
    ElMessage.error('状态变更失败');
    getTenantList(); // 重新获取数据以恢复正确状态
  }
};

// 显示用户分配对话框
const showUserAssignmentDialog = async (tenantId: string, tenantName: string) => {
  currentTenantId.value = tenantId;
  currentTenantName.value = tenantName;
  
  // 加载已分配用户
  await loadAssignedUsers('');
  
  // 加载未分配用户
  await loadUnassignedUsers('');
  
  userAssignmentVisible.value = true;
};

// 加载未分配用户
const loadUnassignedUsers = async (_query: string) => {
  loadingUnassignedUsers.value = true;
  try {
    // 实际项目中应该调用API获取未分配用户
    // 这里仅作为示例
    unassignedUsers.value = [];
  } catch (error) {
    console.error('加载未分配用户失败:', error);
  } finally {
    loadingUnassignedUsers.value = false;
  }
};

// 加载已分配用户
const loadAssignedUsers = async (_query: string) => {
  loadingAssignedUsers.value = true;
  try {
    const response = await tenantService.getTenantUsers(currentTenantId.value);
    if (response.code === 200) {
      assignedUsers.value = response.data || [];
    } else {
      ElMessage.error(response.message || '加载已分配用户失败');
    }
  } catch (error) {
    console.error('加载已分配用户失败:', error);
  } finally {
    loadingAssignedUsers.value = false;
  }
};

// 添加选中用户
const addSelectedUsers = () => {
  selectedUnassignedUsers.value.forEach(user => {
    if (!assignedUsers.value.some(u => u.id === user.id)) {
      assignedUsers.value.push(user);
    }
  });
  selectedUnassignedUsers.value = [];
};

// 移除选中用户
const removeSelectedUsers = () => {
  selectedAssignedUsers.value.forEach(user => {
    const index = assignedUsers.value.findIndex(u => u.id === user.id);
    if (index > -1) {
      assignedUsers.value.splice(index, 1);
    }
  });
  selectedAssignedUsers.value = [];
};

// 保存用户分配
const saveUserAssignment = async () => {
  try {
    const userIds = assignedUsers.value.map(user => user.id);
    const response = await tenantService.assignTenantUsers(currentTenantId.value, userIds);
    if (response.code === 200) {
      ElMessage.success('用户分配成功');
      userAssignmentVisible.value = false;
    } else {
      ElMessage.error(response.message || '用户分配失败');
    }
  } catch (error) {
    console.error('保存用户分配失败:', error);
    ElMessage.error('用户分配失败');
  }
};

// 过滤未分配用户
const filterUnassignedUsers = () => {
  // 此函数用于触发计算属性更新
};

// 过滤已分配用户
const filterAssignedUsers = () => {
  // 此函数用于触发计算属性更新
};

// 组件挂载时获取租户列表
onMounted(() => {
  getTenantList();
});
</script>

<style scoped>
.system-tenant-list {
  padding: 20px;
  background-color: #fff;
  min-height: 100%;
}

.search-area {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.action-area {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.user-selection-section,
.user-assigned-section {
  display: flex;
  flex-direction: column;
}

.action-buttons {
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
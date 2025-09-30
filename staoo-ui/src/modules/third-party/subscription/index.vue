<template>
  <div class="data-subscription-list">
    <div class="page-header">
      <h2>数据订阅管理</h2>
      <el-button type="primary" @click="handleAddSubscription">添加订阅</el-button>
    </div>

    <div class="search-filter">
      <el-select
        v-model="searchForm.appId"
        placeholder="第三方应用"
        style="width: 200px; margin-right: 10px;"
        filterable
        clearable
      >
        <el-option v-for="app in activeApps" :key="app.id" :label="app.appName" :value="app.id" />
      </el-select>
      <el-select
        v-model="searchForm.dataType"
        placeholder="数据类型"
        style="width: 150px; margin-right: 10px;"
        filterable
        clearable
      >
        <el-option v-for="type in dataTypes" :key="type.code" :label="type.name" :value="type.code" />
      </el-select>
      <el-select
        v-model="searchForm.status"
        placeholder="状态"
        style="width: 120px; margin-right: 10px;"
      >
        <el-option label="全部" :value="" />
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="subscriptionList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="appName" label="第三方应用" width="180" />
      <el-table-column prop="dataType" label="数据类型" width="150">
        <template #default="scope">
          {{ getDataTypeName(scope.row.dataType) }}
        </template>
      </el-table-column>
      <el-table-column prop="frequency" label="订阅频率" width="120">
        <template #default="scope">
          {{ getFrequencyName(scope.row.frequency) }}
        </template>
      </el-table-column>
      <el-table-column prop="filterConditions" label="过滤条件" min-width="200">
        <template #default="scope">
          <el-button 
            type="text" 
            size="small" 
            @click="handleShowFilter(scope.row)"
            v-if="scope.row.filterConditions && Object.keys(scope.row.filterConditions).length > 0"
          >
            查看条件
          </el-button>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-switch 
            v-model="scope.row.status" 
            active-value="1" 
            inactive-value="0" 
            @change="handleToggleStatus(scope.row.id, scope.row.status)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEditSubscription(scope.row)"
            style="margin-right: 5px;"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDeleteSubscription(scope.row.id)"
          >
            删除
          </el-button>
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

    <!-- 添加/编辑订阅弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="subscriptionFormRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="第三方应用" prop="appId">
          <el-select v-model="formData.appId" placeholder="请选择第三方应用">
            <el-option v-for="app in activeApps" :key="app.id" :label="app.appName" :value="app.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataType">
          <el-select v-model="formData.dataType" placeholder="请选择数据类型">
            <el-option v-for="type in dataTypes" :key="type.code" :label="type.name" :value="type.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="订阅频率" prop="frequency">
          <el-select v-model="formData.frequency" placeholder="请选择订阅频率">
            <el-option label="实时" value="realtime" />
            <el-option label="每日" value="daily" />
            <el-option label="每周" value="weekly" />
            <el-option label="每月" value="monthly" />
          </el-select>
        </el-form-item>
        <el-form-item label="过滤条件" prop="filterConditions">
          <el-button type="primary" @click="handleConfigFilter">配置条件</el-button>
          <div v-if="formData.filterConditions && Object.keys(formData.filterConditions).length > 0" class="filter-preview">
            <span class="filter-label">已配置条件：</span>
            <span class="filter-summary">查看详情</span>
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="formData.status" active-value="1" inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 过滤条件配置弹窗 -->
    <el-dialog v-model="filterConfigVisible" title="配置过滤条件" width="600px">
      <div class="filter-config-container">
        <p>过滤条件配置界面将根据不同的数据类型提供相应的配置项。</p>
        <p v-if="selectedDataType">当前选择的数据类型：{{ getDataTypeName(selectedDataType) }}</p>
        <!-- 这里可以根据不同的数据类型显示不同的过滤条件配置界面 -->
        <el-button type="primary" @click="handleConfirmFilter">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看过滤条件弹窗 -->
    <el-dialog v-model="showFilterVisible" title="过滤条件" width="600px">
      <div class="filter-detail-container">
        <pre v-if="currentFilterConditions">{{ JSON.stringify(currentFilterConditions, null, 2) }}</pre>
        <p v-else>无过滤条件</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { DataSubscription, ThirdPartyApp } from '../../../types';
import dataSubscriptionService from '../../../services/third-party/dataSubscriptionService';
import thirdPartyAppService from '../../../services/third-party/thirdPartyAppService';

// 搜索表单
const searchForm = reactive({
  appId: '',
  dataType: '',
  status: ''
});

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 订阅列表
const subscriptionList = ref<DataSubscription[]>([]);

// 可用的第三方应用列表
const activeApps = ref<ThirdPartyApp[]>([]);

// 可用的数据类型列表
const dataTypes = ref<Array<{ code: string; name: string; description: string }>>([]);

// 弹窗相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加订阅');
const subscriptionFormRef = ref<FormInstance>();
const formData = reactive<Partial<DataSubscription>>({
  appId: undefined,
  dataType: '',
  frequency: 'daily',
  filterConditions: undefined,
  status: 1
});
const formRules = reactive<FormRules>({
  appId: [{ required: true, message: '请选择第三方应用', trigger: 'change' }],
  dataType: [{ required: true, message: '请选择数据类型', trigger: 'change' }],
  frequency: [{ required: true, message: '请选择订阅频率', trigger: 'change' }]
});

// 过滤条件配置相关
const filterConfigVisible = ref(false);
const showFilterVisible = ref(false);
const currentFilterConditions = ref<Record<string, any>>({});
const selectedDataType = computed(() => formData.dataType);

// 加载订阅列表
const loadSubscriptionList = async () => {
  try {
    const response = await getDataSubscriptionList({
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      appId: searchForm.appId ? Number(searchForm.appId) : undefined,
      dataType: searchForm.dataType,
      status: searchForm.status ? Number(searchForm.status) : undefined
    });
    if (response.code === 200) {
      subscriptionList.value = response.data.list;
      pagination.total = response.data.total;
    } else {
      ElMessage.error(response.message || '获取订阅列表失败');
    }
  } catch (error) {
    console.error('获取订阅列表失败:', error);
    ElMessage.error('获取订阅列表失败');
  }
};

// 加载可用的第三方应用
const loadActiveApps = async () => {
  try {
    const response = await thirdPartyAppService.getActiveThirdPartyApps();
    if (response.code === 200) {
      activeApps.value = response.data;
    } else {
      ElMessage.error(response.message || '获取第三方应用列表失败');
    }
  } catch (error) {
    console.error('获取第三方应用列表失败:', error);
    ElMessage.error('获取第三方应用列表失败');
  }
};

// 加载可用的数据类型
const loadDataTypes = async () => {
  try {
    const response = await getAvailableDataTypes();
    if (response.code === 200) {
      dataTypes.value = response.data;
    } else {
      ElMessage.error(response.message || '获取数据类型列表失败');
      // 使用模拟数据
      dataTypes.value = [
        { code: 'user_data', name: '用户数据', description: '系统用户相关数据' },
        { code: 'order_data', name: '订单数据', description: '业务订单相关数据' },
        { code: 'log_data', name: '日志数据', description: '系统日志相关数据' }
      ];
    }
  } catch (error) {
    console.error('获取数据类型列表失败:', error);
    ElMessage.error('获取数据类型列表失败');
    // 使用模拟数据
    dataTypes.value = [
      { code: 'user_data', name: '用户数据', description: '系统用户相关数据' },
      { code: 'order_data', name: '订单数据', description: '业务订单相关数据' },
      { code: 'log_data', name: '日志数据', description: '系统日志相关数据' }
    ];
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  loadSubscriptionList();
};

// 重置
const handleReset = () => {
  searchForm.appId = '';
  searchForm.dataType = '';
  searchForm.status = '';
  pagination.currentPage = 1;
  loadSubscriptionList();
};

// 分页大小变更
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  loadSubscriptionList();
};

// 当前页码变更
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  loadSubscriptionList();
};

// 添加订阅
const handleAddSubscription = () => {
  dialogTitle.value = '添加订阅';
  Object.assign(formData, {
    appId: undefined,
    dataType: '',
    frequency: 'daily',
    filterConditions: undefined,
    status: 1
  });
  dialogVisible.value = true;
};

// 编辑订阅
const handleEditSubscription = (subscription: DataSubscription) => {
  dialogTitle.value = '编辑订阅';
  Object.assign(formData, {
    id: subscription.id,
    appId: subscription.appId,
    dataType: subscription.dataType,
    frequency: subscription.frequency,
    filterConditions: subscription.filterConditions,
    status: subscription.status
  });
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  if (!subscriptionFormRef.value) return;
  try {
    await subscriptionFormRef.value.validate();
    
    if (formData.id) {
      // 更新订阅
      const response = await updateDataSubscription(formData as DataSubscription);
      if (response.code === 200) {
        ElMessage.success('更新订阅成功');
        dialogVisible.value = false;
        loadSubscriptionList();
      } else {
        ElMessage.error(response.message || '更新订阅失败');
      }
    } else {
      // 创建订阅
      const response = await createDataSubscription(formData as Omit<DataSubscription, 'id' | 'createTime' | 'updateTime' | 'appName'>);
      if (response.code === 200) {
        ElMessage.success('创建订阅成功');
        dialogVisible.value = false;
        loadSubscriptionList();
      } else {
        ElMessage.error(response.message || '创建订阅失败');
      }
    }
  } catch (error) {
    console.error('提交表单失败:', error);
    ElMessage.error('操作失败');
  }
};

// 删除订阅
const handleDeleteSubscription = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除此订阅吗？删除后将无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    const response = await deleteDataSubscription(id);
    if (response.code === 200) {
      ElMessage.success('删除订阅成功');
      loadSubscriptionList();
    } else {
      ElMessage.error(response.message || '删除订阅失败');
    }
  } catch (error) {
    // 用户取消删除
    if (error === 'cancel') return;
    console.error('删除订阅失败:', error);
    ElMessage.error('删除订阅失败');
  }
};

// 切换订阅状态
const handleToggleStatus = async (id: number, status: number) => {
  try {
    const response = await toggleDataSubscriptionStatus(id, status);
    if (response.code !== 200) {
      ElMessage.error(response.message || '操作失败');
      // 恢复原来的状态
      loadSubscriptionList();
    }
  } catch (error) {
    console.error('切换状态失败:', error);
    ElMessage.error('操作失败');
    // 恢复原来的状态
    loadSubscriptionList();
  }
};

// 配置过滤条件
const handleConfigFilter = () => {
  filterConfigVisible.value = true;
};

// 确认过滤条件配置
const handleConfirmFilter = () => {
  // 这里简化处理，实际项目中应该根据配置界面的值更新formData.filterConditions
  if (!formData.filterConditions) {
    formData.filterConditions = {};
  }
  // 示例数据
  formData.filterConditions.example = '配置的过滤条件';
  ElMessage.success('过滤条件配置成功');
  filterConfigVisible.value = false;
};

// 查看过滤条件
const handleShowFilter = (subscription: DataSubscription) => {
  currentFilterConditions.value = subscription.filterConditions || {};
  showFilterVisible.value = true;
};

// 获取数据类型名称
const getDataTypeName = (code: string): string => {
  const type = dataTypes.value.find(t => t.code === code);
  return type ? type.name : code;
};

// 获取订阅频率名称
const getFrequencyName = (frequency: string): string => {
  const frequencyMap: Record<string, string> = {
    'realtime': '实时',
    'daily': '每日',
    'weekly': '每周',
    'monthly': '每月'
  };
  return frequencyMap[frequency] || frequency;
};

// 初始化加载
onMounted(() => {
  loadSubscriptionList();
  loadActiveApps();
  loadDataTypes();
});
</script>

<style scoped>
.data-subscription-list {
  padding: 20px;
  background: #fff;
  min-height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-filter {
  display: flex;
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.filter-preview {
  margin-top: 10px;
  padding: 5px 10px;
  background: #f5f7fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.filter-label {
  margin-right: 5px;
  color: #666;
}

.filter-summary {
  color: #409eff;
  cursor: pointer;
}

.filter-config-container,
.filter-detail-container {
  padding: 10px 0;
}

.filter-detail-container pre {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  overflow-x: auto;
  white-space: pre-wrap;
}
</style>
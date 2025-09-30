<template>
  <div class="third-party-app-list">
    <div class="page-header">
      <h2>第三方应用管理</h2>
      <el-button type="primary" @click="handleAddApp">添加应用</el-button>
    </div>

    <div class="search-filter">
      <el-input
        v-model="searchForm.appName"
        placeholder="应用名称"
        style="width: 200px; margin-right: 10px;"
      />
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

    <el-table :data="appList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="appName" label="应用名称" width="180" />
      <el-table-column prop="appKey" label="App Key" width="200">
        <template #default="scope">
          <span class="app-key">{{ scope.row.appKey }}</span>
          <el-button 
            type="text" 
            size="small" 
            @click="handleCopy(scope.row.appKey)"
            v-if="scope.row.appKey"
          >
            复制
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="appSecret" label="App Secret" width="200">
        <template #default="scope">
          <span class="app-secret">
            {{ scope.row.appSecret ? '●●●●●●●●●●●●' : '-' }}
          </span>
          <el-button 
            type="text" 
            size="small" 
            @click="handleShowSecret(scope.row)"
            v-if="scope.row.appSecret"
          >
            查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" width="200" />
      <el-table-column prop="callbackUrl" label="回调地址" width="250" />
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
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEditApp(scope.row)"
            style="margin-right: 5px;"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDeleteApp(scope.row.id)"
            style="margin-right: 5px;"
          >
            删除
          </el-button>
          <el-button 
            size="small" 
            @click="handleGenerateCredentials(scope.row.id)"
          >
            生成凭证
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

    <!-- 添加/编辑应用弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="appFormRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="formData.appName" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" placeholder="请输入应用描述" type="textarea" />
        </el-form-item>
        <el-form-item label="回调地址" prop="callbackUrl">
          <el-input v-model="formData.callbackUrl" placeholder="请输入回调地址" />
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

    <!-- 查看App Secret弹窗 -->
    <el-dialog v-model="showSecretVisible" title="App Secret" width="400px">
      <div class="secret-container">
        <p class="secret-text">{{ currentAppSecret }}</p>
        <el-button type="primary" @click="handleCopySecret">复制</el-button>
      </div>
    </el-dialog>

    <!-- 生成新凭证确认弹窗 -->
    <el-dialog v-model="generateCredentialsVisible" title="生成新凭证" width="400px">
      <p>生成新凭证将导致旧凭证失效，是否继续？</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="generateCredentialsVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmGenerateCredentials">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { ThirdPartyApp } from '../../../types';
import thirdPartyAppService from '../../../services/third-party/thirdPartyAppService';

// 搜索表单
const searchForm = reactive({
  appName: '',
  status: ''
});

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 应用列表
const appList = ref<ThirdPartyApp[]>([]);

// 弹窗相关
const dialogVisible = ref(false);
const dialogTitle = ref('添加应用');
const appFormRef = ref<FormInstance>();
const formData = reactive<Partial<ThirdPartyApp>>({
  appName: '',
  description: '',
  callbackUrl: '',
  status: 1
});
const formRules = reactive<FormRules>({
  appName: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
  callbackUrl: [{ required: true, message: '请输入回调地址', trigger: 'blur' }]
});

// 查看App Secret相关
const showSecretVisible = ref(false);
const currentAppSecret = ref('');

// 生成新凭证相关
const generateCredentialsVisible = ref(false);
const currentAppId = ref(0);

// 加载应用列表
const loadAppList = async () => {
  try {
    const response = await getThirdPartyAppList({
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      appName: searchForm.appName,
      status: searchForm.status ? Number(searchForm.status) : undefined
    });
    if (response.code === 200) {
      appList.value = response.data.list;
      pagination.total = response.data.total;
    } else {
      ElMessage.error(response.message || '获取应用列表失败');
    }
  } catch (error) {
    console.error('获取应用列表失败:', error);
    ElMessage.error('获取应用列表失败');
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  loadAppList();
};

// 重置
const handleReset = () => {
  searchForm.appName = '';
  searchForm.status = '';
  pagination.currentPage = 1;
  loadAppList();
};

// 分页大小变更
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  loadAppList();
};

// 当前页码变更
const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  loadAppList();
};

// 添加应用
const handleAddApp = () => {
  dialogTitle.value = '添加应用';
  Object.assign(formData, {
    appName: '',
    description: '',
    callbackUrl: '',
    status: 1
  });
  dialogVisible.value = true;
};

// 编辑应用
const handleEditApp = (app: ThirdPartyApp) => {
  dialogTitle.value = '编辑应用';
  Object.assign(formData, {
    id: app.id,
    appName: app.appName,
    description: app.description,
    callbackUrl: app.callbackUrl,
    status: app.status
  });
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  if (!appFormRef.value) return;
  try {
    await appFormRef.value.validate();
    
    if (formData.id) {
      // 更新应用
      const response = await updateThirdPartyApp(formData as ThirdPartyApp);
      if (response.code === 200) {
        ElMessage.success('更新应用成功');
        dialogVisible.value = false;
        loadAppList();
      } else {
        ElMessage.error(response.message || '更新应用失败');
      }
    } else {
      // 创建应用
      const response = await createThirdPartyApp(formData as Omit<ThirdPartyApp, 'id' | 'createTime' | 'updateTime'>);
      if (response.code === 200) {
        ElMessage.success('创建应用成功');
        dialogVisible.value = false;
        loadAppList();
      } else {
        ElMessage.error(response.message || '创建应用失败');
      }
    }
  } catch (error) {
    console.error('提交表单失败:', error);
    ElMessage.error('操作失败');
  }
};

// 删除应用
const handleDeleteApp = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除此应用吗？删除后将无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    const response = await deleteThirdPartyApp(id);
    if (response.code === 200) {
      ElMessage.success('删除应用成功');
      loadAppList();
    } else {
      ElMessage.error(response.message || '删除应用失败');
    }
  } catch (error) {
    // 用户取消删除
    if (error === 'cancel') return;
    console.error('删除应用失败:', error);
    ElMessage.error('删除应用失败');
  }
};

// 切换应用状态
const handleToggleStatus = async (id: number, status: number) => {
  try {
    const response = await toggleThirdPartyAppStatus(id, status);
    if (response.code !== 200) {
      ElMessage.error(response.message || '操作失败');
      // 恢复原来的状态
      loadAppList();
    }
  } catch (error) {
    console.error('切换状态失败:', error);
    ElMessage.error('操作失败');
    // 恢复原来的状态
    loadAppList();
  }
};

// 显示App Secret
const handleShowSecret = (app: ThirdPartyApp) => {
  currentAppSecret.value = app.appSecret || '';
  showSecretVisible.value = true;
};

// 复制App Key
const handleCopy = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    ElMessage.success('复制成功');
  } catch (error) {
    console.error('复制失败:', error);
    ElMessage.error('复制失败');
  }
};

// 复制App Secret
const handleCopySecret = async () => {
  try {
    await navigator.clipboard.writeText(currentAppSecret.value);
    ElMessage.success('复制成功');
  } catch (error) {
    console.error('复制失败:', error);
    ElMessage.error('复制失败');
  }
};

// 生成新凭证
const handleGenerateCredentials = (id: number) => {
  currentAppId.value = id;
  generateCredentialsVisible.value = true;
};

// 确认生成新凭证
const confirmGenerateCredentials = async () => {
  try {
    const response = await thirdPartyAppService.generateAppCredentials(currentAppId.value);
    if (response.code === 200) {
      ElMessage.success('生成新凭证成功');
      generateCredentialsVisible.value = false;
      loadAppList();
    } else {
      ElMessage.error(response.message || '生成新凭证失败');
    }
  } catch (error) {
    console.error('生成新凭证失败:', error);
    ElMessage.error('生成新凭证失败');
  }
};

// 初始化加载
onMounted(() => {
  loadAppList();
});
</script>

<style scoped>
.third-party-app-list {
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

.app-key,
.app-secret {
  font-family: monospace;
  color: #666;
}

.secret-container {
  text-align: center;
  padding: 20px 0;
}

.secret-text {
  font-family: monospace;
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
  word-break: break-all;
}
</style>
<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="600px"
    @close="handleClose"
    @open="handleOpen"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      style="max-width: 500px;"
    >
      <el-form-item label="租户名称" prop="tenantName">
        <el-input
          v-model="formData.tenantName"
          placeholder="请输入租户名称"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="租户标识" prop="tenantKey">
        <el-input
          v-model="formData.tenantKey"
          placeholder="请输入租户标识"
          maxlength="30"
          show-word-limit
        />
        <div class="el-form-item__help">租户唯一标识，建议使用英文和数字</div>
      </el-form-item>
      
      <el-form-item label="租户描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          placeholder="请输入租户描述"
          :rows="3"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="创建人" v-if="formData.createBy">
        <el-input v-model="formData.createBy" disabled />
      </el-form-item>
      
      <el-form-item label="创建时间" v-if="formData.createTime">
        <el-input v-model="formData.createTime" disabled />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue';
import { ElMessage, ElForm, type FormRules } from 'element-plus';
import tenantService from '@/services/system/tenant';

// Props
interface Props {
  visible: boolean;
  tenantId: string;
}

const props = defineProps<Props>();

// Emits
const emit = defineEmits<{
  close: [];
  success: [];
}>();

// 表单引用
const formRef = ref<InstanceType<typeof ElForm>>();

// 对话框可见状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => {
    if (!value) {
      emit('close');
    }
  }
});

// 对话框标题
const dialogTitle = computed(() => {
  return props.tenantId ? '编辑租户' : '新增租户';
});

// 表单数据
const formData = ref({
  id: '',
  tenantName: '',
  tenantKey: '',
  description: '',
  status: 1,
  createBy: '',
  createTime: ''
});

// 表单验证规则
const formRules: FormRules = {
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' },
    { min: 2, max: 50, message: '租户名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  tenantKey: [
    { required: true, message: '请输入租户标识', trigger: 'blur' },
    { min: 2, max: 30, message: '租户标识长度在 2 到 30 个字符', trigger: 'blur' },
    {
      pattern: /^[a-zA-Z0-9_]+$/, 
      message: '租户标识只能包含字母、数字和下划线', 
      trigger: 'blur'
    },
    {
      validator: (_rule: any, value: string, callback: any) => {
        if (!value) {
          callback();
          return;
        }
        
        tenantService.checkTenantNameDuplicate(value, props.tenantId)
          .then(response => {
            if (response.code === 200 && response.data) {
              callback('租户标识已存在');
            } else {
              callback();
            }
          })
          .catch(error => {
            console.error('检查租户标识重复失败:', error);
            callback(); // 出错时默认通过验证，避免影响用户体验
          });
      },
      trigger: 'blur'
    }
  ]
};

// 监听租户ID变化，加载租户详情
watch(() => props.tenantId, (newId) => {
  if (newId && props.visible) {
    loadTenantDetail(newId);
  }
});

// 监听对话框可见状态变化
watch(() => props.visible, (newVisible) => {
  if (newVisible) {
    handleOpen();
  }
});

// 加载租户详情
const loadTenantDetail = async (id: string) => {
  try {
    const response = await tenantService.getTenantDetail(id);
    if (response.code === 200 && response.data) {
      formData.value = { ...response.data };
    } else {
      ElMessage.error(response.message || '加载租户详情失败');
    }
  } catch (error) {
    console.error('加载租户详情失败:', error);
    ElMessage.error('加载租户详情失败');
  }
};

// 重置表单
const resetForm = () => {
  formData.value = {
    id: '',
    tenantName: '',
    tenantKey: '',
    description: '',
    status: 1,
    createBy: '',
    createTime: ''
  };
  
  if (formRef.value) {
    formRef.value.resetFields();
  }
};

// 处理对话框打开
const handleOpen = () => {
  if (props.tenantId) {
    loadTenantDetail(props.tenantId);
  } else {
    resetForm();
  }
};

// 处理对话框关闭
const handleClose = () => {
  emit('close');
};

// 处理取消
const handleCancel = () => {
  dialogVisible.value = false;
};

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    
    // 准备提交数据
    const submitData = {
      ...formData.value,
      id: props.tenantId || undefined
    };
    
    // 调用API提交数据
    let response;
    if (props.tenantId) {
      response = await tenantService.updateTenant(props.tenantId, submitData);
    } else {
      response = await tenantService.addTenant(submitData);
    }
    
    // 处理响应
    if (response.code === 200) {
      ElMessage.success(props.tenantId ? '编辑成功' : '新增成功');
      emit('success');
      dialogVisible.value = false;
    } else {
      ElMessage.error(response.message || (props.tenantId ? '编辑失败' : '新增失败'));
    }
  } catch (error) {
    console.error('表单验证或提交失败:', error);
    // 表单验证失败会自动显示错误信息，这里主要处理其他类型的错误
  }
};
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
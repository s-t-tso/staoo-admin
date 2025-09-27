<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <div class="login-title">Staoo Admin</div>
      <el-form
        ref="loginFormRef"
        v-model="loginForm"
        :rules="loginRules"
        class="login-form"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            :validate-event="false"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            :validate-event="false"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <el-link type="primary" class="forgot-password" @click="handleForgotPassword">忘记密码?</el-link>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store'
import { ElMessage } from 'element-plus'
import { login } from '../../services/authService'
import type { LoginRequest } from '../../types'

// 表单引用
const loginFormRef = ref<InstanceType<typeof import('element-plus')['ElForm']>>()

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 登录规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 加载状态
const loading = ref(false)

// Store
const userStore = useUserStore()
const router = useRouter()

// 处理登录
const handleLogin = async () => {
  // 验证表单
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    // 调用登录服务
    const loginRequest: LoginRequest = {
      username: loginForm.username,
      password: loginForm.password
    }

    const data = await login(loginRequest)

    // 保存用户信息和token
    userStore.setToken(data.accessToken)
    userStore.setUserInfo(data.userInfo)

    // 记住我
    if (loginForm.rememberMe) {
      localStorage.setItem('rememberedUsername', loginForm.username)
    } else {
      localStorage.removeItem('rememberedUsername')
    }

    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理忘记密码
const handleForgotPassword = () => {
  ElMessage('忘记密码功能暂未实现')
}

// 初始化函数
const init = () => {
  // 读取记住的用户名
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    loginForm.rememberMe = true
  }
}

// 组件挂载时初始化
onMounted(() => {
  init()
})
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-form-wrapper {
  width: 400px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 40px;
}

.login-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 30px;
}

.login-form {
  width: 100%;
}

.forgot-password {
  float: right;
}

.login-button {
  width: 100%;
}
</style>

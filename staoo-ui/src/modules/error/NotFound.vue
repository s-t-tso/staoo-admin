<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-message">抱歉，您访问的页面不存在</div>
      <div class="error-description">
        您可能输入了错误的地址，或者该页面已被删除或移动
      </div>
      <el-button type="primary" @click="goBack" class="back-button">
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  // 如果有历史记录，则返回上一页，否则返回首页
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/dashboard')
  }
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.not-found-content {
  text-align: center;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 20px;
  line-height: 1;
}

.error-message {
  font-size: 24px;
  color: #303133;
  margin-bottom: 12px;
}

.error-description {
  font-size: 14px;
  color: #606266;
  margin-bottom: 30px;
  line-height: 1.5;
}

.back-button {
  padding: 10px 30px;
  font-size: 16px;
}
</style>
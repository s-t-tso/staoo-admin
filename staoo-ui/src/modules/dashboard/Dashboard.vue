<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h1>欢迎使用 Staoo Admin</h1>
      <p>这是一个基于 Vue 3 + TypeScript + Vite 的企业级后台管理系统</p>
    </div>

    <div class="dashboard-stats">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">1,284</div>
          <div class="stat-label">总用户数</div>
        </div>
        <div class="stat-icon">
          <el-icon><User /></el-icon>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">86</div>
          <div class="stat-label">今日新增</div>
        </div>
        <div class="stat-icon">
          <el-icon><Plus /></el-icon>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">99.9%</div>
          <div class="stat-label">系统可用性</div>
        </div>
        <div class="stat-icon">
          <el-icon><Check /></el-icon>
        </div>
      </el-card>

      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-number">¥28,456</div>
          <div class="stat-label">今日收入</div>
        </div>
        <div class="stat-icon">
          <el-icon><Money /></el-icon>
        </div>
      </el-card>
    </div>

    <div class="dashboard-charts">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>用户增长趋势</span>
          </div>
        </template>
        <div ref="userGrowthChart" class="chart-container"></div>
      </el-card>

      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>系统访问统计</span>
          </div>
        </template>
        <div ref="systemAccessChart" class="chart-container"></div>
      </el-card>
    </div>

    <div class="dashboard-recent">
      <el-card class="recent-card">
        <template #header>
          <div class="card-header">
            <span>最近活动</span>
          </div>
        </template>
        <el-table :data="recentActivities" style="width: 100%">
          <el-table-column prop="time" label="时间" width="180" />
          <el-table-column prop="user" label="用户" width="120" />
          <el-table-column prop="action" label="操作" />
          
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { User, Plus, Check, Money } from '@element-plus/icons-vue'

// 图表容器引用
const userGrowthChart = ref<HTMLElement>()
const systemAccessChart = ref<HTMLElement>()

// 最近活动数据
const recentActivities = [
  {
    time: '2024-10-01 09:30:25',
    user: 'admin',
    action: '登录系统',
    status: 'success'
  },
  {
    time: '2024-10-01 08:45:12',
    user: 'user123',
    action: '更新用户信息',
    status: 'success'
  },
  {
    time: '2024-10-01 07:15:47',
    user: 'testuser',
    action: '创建新角色',
    status: 'success'
  },
  {
    time: '2024-09-30 18:20:33',
    user: 'admin',
    action: '系统备份',
    status: 'success'
  },
  {
    time: '2024-09-30 16:55:18',
    user: 'user456',
    action: '删除用户',
    status: 'failure'
  }
]

// 初始化页面
onMounted(() => {
  // 这里应该初始化图表
  // 由于没有引入具体的图表库，暂时只做占位
  console.log('Dashboard mounted')
})
</script>

<style scoped>
.dashboard-container {
  width: 100%;
  height: 100%;
}

.dashboard-header {
  margin-bottom: 24px;
}

.dashboard-header h1 {
  font-size: 28px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #303133;
}

.dashboard-header p {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.dashboard-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #f0f9ff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1890ff;
}

.dashboard-charts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.chart-card {
  height: 300px;
  display: flex;
  flex-direction: column;
}

.chart-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dashboard-recent {
  margin-bottom: 24px;
}

.recent-card {
  min-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

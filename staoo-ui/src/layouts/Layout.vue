<template>
  <div class="staoo-layout">
    <!-- 侧边栏 -->
    <aside
      class="staoo-sidebar"
      :class="{ 'collapsed': systemStore.sidebarCollapsed }"
    >
      <div class="sidebar-header">
        <div class="logo">Staoo Admin</div>
        <el-button
          class="collapse-btn"
          icon="el-icon-menu-fold"
          @click="toggleSidebar"
          size="small"
          circle
        />
      </div>

      <nav class="sidebar-menu">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          router
          :collapse-transition="false"
          :collapse="systemStore.sidebarCollapsed"
        >
          <template v-for="route in routes" :key="route.name">
            <!-- 有子路由的菜单 -->
            <el-sub-menu v-if="route.children && route.children.length > 0" :index="route.name">
              <template #title>
                <el-icon>
                  <component :is="route.meta?.icon || 'Menu'" />
                </el-icon>
                <span>{{ route.meta?.title }}</span>
              </template>

              <template v-for="child in route.children" :key="child.name">
                <el-menu-item v-if="child.meta?.hidden !== true" :index="child.path">
                  <el-icon v-if="child.meta?.icon"><component :is="child.meta.icon" /></el-icon>
                  <span>{{ child.meta?.title }}</span>
                </el-menu-item>
              </template>
            </el-sub-menu>

            <!-- 无子路由的菜单 -->
            <el-menu-item v-else :index="route.path">
              <el-icon>
                <component :is="route.meta?.icon || 'Menu'" />
              </el-icon>
              <span>{{ route.meta?.title }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </nav>
    </aside>

    <!-- 主内容区域 -->
    <div class="staoo-main">
      <!-- 顶部导航栏 -->
      <header class="staoo-header">
        <div class="header-left">
          <el-button
            class="toggle-btn"
            icon="el-icon-menu"
            @click="toggleSidebar"
            size="small"
            circle
          />

          <!-- 面包屑导航 -->
          <el-breadcrumb v-if="systemStore.breadcrumbList.length > 0" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item v-for="(item, index) in systemStore.breadcrumbList" :key="index">
              <router-link v-if="index < systemStore.breadcrumbList.length - 1" :to="item.path">
                {{ item.name }}
              </router-link>
              <span v-else>{{ item.name }}</span>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 搜索框 -->
          <el-input
            v-model="searchValue"
            placeholder="搜索"
            prefix-icon="el-icon-search"
            size="small"
            class="search-input"
          />

          <!-- 通知 -->
          <el-dropdown size="small">
            <span class="el-dropdown-link">
              <el-badge is-dot>
                <el-icon><Bell /></el-icon>
              </el-badge>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>通知1</el-dropdown-item>
                <el-dropdown-item>通知2</el-dropdown-item>
                <el-dropdown-item>通知3</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <!-- 用户信息 -->
          <el-dropdown size="small">
            <span class="el-dropdown-link user-info">
              <el-avatar :size="32">
                {{ userStore.userInfo.nickname || 'User' }}
              </el-avatar>
              <span v-if="!systemStore.sidebarCollapsed" class="user-name">
                {{ userStore.userInfo.nickname || '未登录' }}
              </span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 内容区域 -->
      <main class="staoo-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore, useSystemStore } from '../store'
import { Bell } from '@element-plus/icons-vue'

// 安全拼接路径的辅助函数已移除，因为当前未使用

// Store
const userStore = useUserStore()
const systemStore = useSystemStore()
const router = useRouter()

// 搜索值
const searchValue = ref('')

// 计算当前激活的菜单
const activeMenu = computed(() => {
  const route = router.currentRoute.value
  return route.name as string || ''
})

// 使用从后端获取的动态路由数据
const routes = computed(() => {
  // 返回系统Store中存储的可访问路由
  return systemStore.accessedRoutes || []
})

// 切换侧边栏
const toggleSidebar = () => {
  systemStore.toggleSidebar()
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

// 初始化
onMounted(async () => {
  // 加载token
  userStore.loadToken()
  
  // 如果用户已登录且没有加载过菜单数据，则加载动态路由
  if (userStore.isLoggedIn && systemStore.accessedRoutes.length === 0) {
    try {
      await systemStore.loadMenuData()
    } catch (error) {
      console.error('加载菜单数据失败:', error)
    }
  }

  // 更新面包屑
  updateBreadcrumb()
})

// 更新面包屑
const updateBreadcrumb = () => {
  const route = router.currentRoute.value
  const matched = route.matched
  const breadcrumbList = matched.map((item: any) => ({
    path: item.path,
    name: item.meta?.title as string || item.name as string
  }))
  systemStore.setBreadcrumbList(breadcrumbList)
}
</script>

<style scoped>
.staoo-layout {
  display: flex;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 侧边栏样式 */
.staoo-sidebar {
  width: 240px;
  background-color: #fff;
  border-right: 1px solid #ebeef5;
  transition: width 0.3s ease;
  overflow: hidden;
}

.staoo-sidebar.collapsed {
  width: 54px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.logo {
  font-size: 18px;
  font-weight: bold;
  color: #1890ff;
}

.sidebar-menu {
  padding: 16px 0;
}

/* 主内容区域样式 */
.staoo-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 顶部导航栏样式 */
.staoo-header {
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-input {
  width: 200px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-size: 14px;
}

/* 内容区域样式 */
.staoo-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>

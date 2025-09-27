import { createApp } from 'vue'
// 导入全局样式
import './assets/styles/main.scss'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import pinia from './store'

const app = createApp(App)

// 使用Element Plus
app.use(ElementPlus)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用路由
app.use(router)

// 使用状态管理
app.use(pinia)

// 在应用挂载前加载用户信息
import { useUserStore } from './store'
const userStore = useUserStore()
userStore.loadToken()

app.mount('#app')

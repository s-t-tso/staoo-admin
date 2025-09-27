# Staoo Admin 前端项目

## 项目介绍
Staoo Admin 是一个基于 Vue 3 + TypeScript + Vite 的企业级后台管理系统前端工程。

## 技术栈
- **框架**: Vue 3
- **语言**: TypeScript
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **图标**: Element Plus Icons
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP请求**: Axios
- **工作流配置**: Vue Flow

## 项目结构
```
staoo-ui
├── public/           # 静态资源
├── src/              # 源代码
│   ├── assets/       # 资源文件（图片、样式等）
│   ├── components/   # 公共组件
│   ├── layouts/      # 布局组件
│   ├── modules/      # 业务模块
│   ├── router/       # 路由配置
│   ├── store/        # 状态管理
│   ├── utils/        # 工具函数
│   ├── services/     # API服务
│   ├── types/        # TypeScript类型定义
│   ├── App.vue       # 根组件
│   ├── main.ts       # 入口文件
│   └── vite-env.d.ts # Vite环境类型定义
├── .gitignore        # Git忽略配置
├── package.json      # 项目依赖
├── tsconfig.json     # TypeScript配置
├── vite.config.ts    # Vite配置
└── README.md         # 项目说明
```

## 快速开始

### 安装依赖
```bash
npm install
```

### 开发环境运行
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产版本
```bash
npm run preview
```

## 功能模块
- **系统管理**：用户管理、角色管理、部门管理、菜单管理
- **工作流模块**：流程设计器、流程实例管理
- **表单模块**：表单设计器、表单数据管理
- **API网关模块**：请求路由、负载均衡、认证授权
- **公共组件模块**：工具类、常量定义、统一响应

## 开发规范
- **组件开发**：优先使用Composition API和`<script setup>`语法糖
- **命名规范**：文件使用小驼峰，组件使用大驼峰，变量使用小驼峰，常量使用全大写下划线
- **TypeScript规范**：所有变量、函数、组件Props等必须有明确的类型定义
- **代码风格**：遵循前端规则文档中的相关规范

## 注意事项
- 开发前请确保已安装Node.js环境（推荐16.x及以上版本）
- 项目使用了Vue 3的最新特性，请熟悉相关文档
- 如需添加新的依赖，请先查看项目根目录下的前端规则文档
- 提交代码前请确保通过了代码检查和测试

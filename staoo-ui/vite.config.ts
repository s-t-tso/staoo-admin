import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  
  // 路径别名配置
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  
  // 开发服务器配置
  server: {
    port: 3000,
    open: true,
    proxy: {
      // 配置API代理，解决跨域问题
      '/api': {
        target: 'http://localhost:8080', // 后端API地址
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
  
  // 构建优化配置
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    minify: 'terser',
    chunkSizeWarningLimit: 1500,
    rollupOptions: {
      output: {
        chunkFileNames: 'assets/js/[name]-[hash].js',
        entryFileNames: 'assets/js/[name]-[hash].js',
        assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
        manualChunks(id) {
          // 将第三方库分离成单独的chunk
          if (id.includes('node_modules')) {
            if (id.includes('element-plus')) {
              return 'element-plus'
            }
            if (id.includes('axios')) {
              return 'axios'
            }
            if (id.includes('@vue')) {
              return 'vue'
            }
            if (id.includes('vue-flow')) {
              return 'vue-flow'
            }
            return 'vendor'
          }
        },
      },
    },
  },
  
  // CSS配置
  css: {
    preprocessorOptions: {
      scss: {
        // 全局SCSS变量和混合器
        additionalData: `
          @import "@/assets/styles/variables.scss";
          @import "@/assets/styles/mixins.scss";
        `,
      },
    },
  },
})

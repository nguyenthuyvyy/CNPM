import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: {}, // ⚡ fix lỗi "global is not defined" cho sockjs-client
  },
})

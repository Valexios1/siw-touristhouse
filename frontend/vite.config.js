import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  define: {
    'process.env.NODE_ENV': JSON.stringify('production'),
  },
  build: {
    outDir: '../src/main/resources/static/js',
    emptyOutDir: false,
    lib: {
      entry: 'src/main.jsx',
      name: 'CamereWidget',
      formats: ['iife'],
      fileName: () => 'camere-widget.js',
    },
  },
});

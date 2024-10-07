import {fileURLToPath, URL} from 'node:url'

import {defineConfig, UserConfigExport} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig(({command, mode, isSsrBuild, isPreview}) => {
    let config: UserConfigExport;
    if (mode === 'dev') {
        config = {
            plugins: [
                vue(),
                vueDevTools()
            ],
            server: {
                host: '127.0.0.1',
                port: 8080
            }
        };
    } else {
        config = {
            plugins: [
                vue(),
            ]
        };
    }
    if (!config.resolve) {
        config.resolve = {};
    }
    config.resolve.alias = {
        '@todo-lists': fileURLToPath(new URL('./src/todo-lists', import.meta.url)),
        '@': fileURLToPath(new URL('./src', import.meta.url))
    };
    return config;
});

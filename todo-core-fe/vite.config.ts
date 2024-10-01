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
            base: '/vue-ui',
            server: {
                host: '127.0.0.1',
                port: 8080,
                //cors: true,
                // origin: '127.0.0.1:8080',
                // proxy: {
                //     '^/api/.*': {
                //         target: 'http://localhost:8080',
                //         changeOrigin: true,
                //         secure: false
                //     }
                // },
                // hmr: {
                //     host: '127.0.0.1',
                //     port: 4000
                // }
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
        '@': fileURLToPath(new URL('./src', import.meta.url))
    };
    return config;
});

import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import { MotionPlugin } from '@vueuse/motion'
import 'normalize.css'
import "tailwindcss/tailwind.css"
import 'animate.css'
import basename from '@/utils/basename'
import Toast, { PluginOptions, POSITION } from "vue-toastification";
import { createPinia } from 'pinia'
// Import the CSS or use your own!
import "vue-toastification/dist/index.css";
// 解决tailwind对el组件样式的覆盖问题
import './preflight.css'
// 解决el组件弹窗等组件按需引入问题
import 'element-plus/theme-chalk/el-message.css';
import 'element-plus/theme-chalk/el-message-box.css';

const app = createApp(App)
// 自动注册 directives 下的所有指令
const directivesModel = import.meta.glob('@/directives/*.ts')
for (const file in directivesModel) {
    const fileName = basename(file, false)
    const fileContent = directivesModel[file]() as Promise<{ default: object }>
    app.directive(fileName, await fileContent.then(res => res.default))
}
// 弹窗默认配置
const toastOptions: PluginOptions = {
    position: POSITION.TOP_CENTER,
    timeout: 1500,
    showCloseButtonOnHover: true
}

app
    .use(Toast, toastOptions)
    .use(router)
    .use(MotionPlugin)
    .use(createPinia())
    .mount('#app')

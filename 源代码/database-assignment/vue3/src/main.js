import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import UndrawUi from 'undraw-ui'
import 'undraw-ui/dist/style.css'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus)
app.use(UndrawUi)
app.use(createPinia())
app.use(router)

//全局保存的用户信息
const user = JSON.parse(localStorage.getItem('user')) || null;
if (user) {
    app.config.globalProperties.$user = user;
}
// 添加导航守卫
router.beforeEach((to, from, next) => {
    const isLogin = user!=null/* 根据您的实际情况获取用户登录状态 */;
    console.log("isLogin" + isLogin);
    if (to.matched.some(record => record.meta.requireLogin)) {
        // 如果路由需要身份验证，并且用户未登录，则重定向到登录页面
        if (isLogin) {
            next();
        } else {
            next('/login');
        }
    } else {
        // 如果路由不需要身份验证，则继续正常导航
        next();
    }
});

app.mount('#app')

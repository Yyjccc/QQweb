import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/pinia/user'

// 展示
import OpeningMain from '@/pages/opening/MainPage/index.vue'
import OpeningHome from '@/pages/opening/HomePage/index.vue'

// 功能
import UsingInfo from '@/pages/using/InfoPage/page.vue'
import UsingMain from '@/pages/using/MainPage/page.vue'
import ChatList from '@/pages/using/MainPage/ChatList/page.vue'
import ChatGPT from '@/pages/using/MainPage/WaitList/page.vue'
import ChatUser from '@/pages/using/MainPage/ChatUser/page.vue'
import ChatGroup from '@/pages/using/MainPage/ChatGroup/page.vue'
import ChatInfo from '@/pages/using/MainPage/ChatInfo/page.vue'
import ChatSetting from '@/pages/using/MainPage/ChatSetting/page.vue'
import { getToken } from '@/utils/handler-token'
import { GetInfo } from '@/axios/user'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/info'
    },
    // {
    //     path: '/opening',
    //     name: 'MainPage',
    //     redirect: '/opening/home',
    //     component: OpeningMain,
    //     children: [
    //         {
    //             path: 'home',
    //             name: 'HomePage',
    //             component: OpeningHome,
    //         }
    //     ]
    // },
    {
        path: '/using',
        component: UsingMain,
        redirect: '/using/chat-list',
        children: [
            {
                path: 'chat-list',
                component: ChatList
            },
            {
                path: 'wait-list',
                component: ChatGPT
            },
            {
                path: 'chat-user',
                component: ChatUser
            },
            {
                path: 'chat-group',
                component: ChatGroup
            },
            {
                path: 'chat-info',
                component: ChatInfo
            },
            {
                path: 'chat-setting',
                component: ChatSetting
            }
        ]
    },
    {
        path: '/info',
        name: 'InfoPage',
        component: UsingInfo
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    console.log('从：', from)
    console.log('去往：', to.path.split('/'))
    const needToken = to.path.split('/')[1] === 'using'
    if (!needToken) return next()
    // 需要token
    const token = getToken()
    if (!token) return next('/info')
    // 存在token，查看pinia中是否存在用户信息
    const userStore = useUserStore()
    if (userStore.hasUser) return next()
    // 获取用户信息
    GetInfo().then(res => {
        userStore.user = res
        next({
            ...to,
            replace: true
        })
    })
})

export default router
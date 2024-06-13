<script setup lang='ts'>
import { onMounted, onUnmounted, ref } from 'vue'
import { Menu, Expand, Fold } from '@element-plus/icons-vue'
import { Icon } from '@iconify/vue';
import { useRoute, useRouter } from 'vue-router'
import MenuMore from './menu-more.vue'
import { useDebounce } from '@/utils/useDebounce'
import { useUserStore } from '@/pinia/user';
import { UserAPI } from '@/axios/filter';
import { clearToken, getToken } from '@/utils/handler-token';
import { User } from '@/constant/types';
import {Logout} from "@/axios/user";

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
// 侧边栏是否关闭
const isCollapse = ref(true)
// 侧边栏铺平模式
const isPave = ref(true)

// 监听屏幕大小变化
const handlerSide = () => {
    const clientWidth = document.documentElement.clientWidth
    const isXl = clientWidth >= 1024
    setCollapse(!isXl, () => {
        isPave.value = !isXl
    })
}
const changeSide = useDebounce(handlerSide)

const logout=async ()=>{
    await Logout();
    router.push('/info')
            clearToken()
            const store = useUserStore()
            store.user = {} as User
}

onMounted(() => {
    window.addEventListener('resize', changeSide)
    handlerSide()
})
onUnmounted(() => {
    window.removeEventListener('resize', changeSide)
})
// 在指定方法执行后改变状态
const setCollapse = (state:boolean, fn:() => void) => {
    fn()
    setTimeout(() => {
        isCollapse.value = state
    })
}
// 点击下方切换
const changeCollapse = () => {
    setCollapse(!isCollapse.value, () => {
        isPave.value = !isCollapse.value
    })
}
</script>

<template>
    <el-menu
        router
        :default-active="route.path"
        :collapse="isCollapse"
        class="border-none relative z-10"
        :style="{
        // 文字颜色
        '--el-menu-text-color': 'rgb(138, 138, 138)',
        // 活动文字颜色
        '--el-menu-active-color': 'rgb(7, 193, 96)',
        // 菜单栏背景色
        '--el-menu-bg-color': 'rgb(46, 46, 46)',
        // 活动菜单栏背景色
        '--el-menu-hover-bg-color': 'transparent'
    }">
        <div class="text-center mt-[40px] mb-[15px]">
            <el-avatar shape="square" class="cursor-pointer" :src="userStore.user.avatar" @click="router.push('/using/chat-setting')"/>
        </div>
        <el-menu-item index="/using/chat-list">
            <el-icon>
                <Icon icon="mdi:message-badge" />
            </el-icon>
            <template #title>聊天</template>
        </el-menu-item>
        <el-menu-item index="/using/chat-user">
            <el-icon>
                <Icon icon="mdi:user" />
            </el-icon>
            <template #title>好友</template>
        </el-menu-item>
        <el-menu-item index="/using/chat-group">
            <el-icon>
                <Icon icon="material-symbols:group-rounded" />
            </el-icon>
            <template #title>群组</template>
        </el-menu-item>
        <el-menu-item index="/using/wait-list">
            <el-icon>
                <Icon icon="mdi:robot-confused" />
            </el-icon>
            <template #title>处理</template>
        </el-menu-item>
        <MenuMore v-if="isPave"/>
        <transition
            enter-active-class="animate__animated animate__fadeIn"
            leave-active-class="animate__animated animate__fadeOut">
            <el-sub-menu index="/using/chat-setting" v-if="!isPave">
                <template #title>
                    <el-icon>
                        <Menu />
                    </el-icon>
                    <span>更多模块</span>
                </template>
                <MenuMore />
            </el-sub-menu>
        </transition>
        <el-menu-item @click="logout">
            <el-icon>
                <Icon icon="mdi:user" />
            </el-icon>
            <template #title>退出登录</template>
        </el-menu-item>
        <div class="absolute bottom-0 w-full text-[#8a8a8a] flex justify-center gap-[30px] h-[50px] items-center">
            <el-icon size="20" @click="changeCollapse">
                <Expand v-if="isCollapse"/>
                <Fold v-else/>
            </el-icon>
        </div>
    </el-menu>
</template>


<style scoped lang='scss'>
.el-menu-item.is-active {
    svg {
        @apply text-[#07c160] transition-colors duration-300;
    }
}

.el-icon {
    svg {
        @apply text-[19px] text-[#8a8a8a];
    }
}
</style>

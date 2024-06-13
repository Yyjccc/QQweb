<script setup lang='ts'>
import { useRouter } from 'vue-router'
import { onMounted, onUnmounted, ref, watch, watchEffect } from 'vue'
import { Sunrise, MoonNight, Check, Close, Operation, Edit } from '@element-plus/icons-vue'
import type { TabsPaneContext } from 'element-plus'
import { Logout } from '@/axios/user'
import { useUserStore } from '@/pinia/user'
import { useSettingStore } from '@/pinia/setting'
import { User } from '@/constant/types'
import { clearToken } from '@/utils/handler-token'
import { useToast } from 'vue-toastification'
import moment from 'moment'
import { useDebounce } from '@/utils/useDebounce'
import UserForm from './user-form.vue'


const router = useRouter()
const store = useUserStore()
const settingStore = useSettingStore()
const goBack = () => {
    router.go(-1)
}

// 暗夜模式
const isDark = ref(settingStore.isDark)
// 灰色模式
const isGrey = ref(settingStore.isGrey)

watchEffect(() => {
    settingStore.isDark = isDark.value
    settingStore.isGrey = isGrey.value
})

watch(isGrey, after => {
    document.documentElement.style.filter = after ? 'grayscale(100%)': ''
})
// 设置抽屉
const settingDrawer = ref(false)

const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}

const logout = () => {
    Logout().then(() => {
        const toast = useToast()
        toast.success('账号已退出，请重新登录！')
        store.user = {} as User
        clearToken()
        router.push({
            path: '/info',
            replace: true
        })
    })
}

// 处理时间
const handlerTime = (time: string) => moment(time).format('YYYY年MM月DD日')

const isSm = ref(true)
// 监听屏幕大小变化
const changeSide = () => {
    const clientWidth = document.documentElement.clientWidth
    isSm.value = clientWidth >= 640
}
// 防抖hook
const onResize = useDebounce(changeSide)

onMounted(() => {
    window.addEventListener('resize', onResize)
    changeSide()
})
onUnmounted(() => {
    window.removeEventListener('resize', onResize)
})

const editDrawer = ref(false)

const getSex = (sex: '0' | '1' | undefined) => {
    if (!sex) return '暂无'
    return sex === '0' ? '男' : '女'
}
</script>
<template>
    <div class="flex-1 flex flex-col min-h-0 overflow-y-auto">
        <div class="flex flex-shrink-0 justify-between items-center h-[50px] bg-white px-[20px] sm:pr-[50px]">
            <el-page-header @back="goBack">
                <template #content>
                    <span class="text-large font-600 mr-3">返回</span>
                </template>
            </el-page-header>
            <!-- 导航条右边 -->
            <el-dropdown trigger="click" class="h-full ml-auto mr-[10px]">
                <div class="flex items-center hover:bg-[#f6f6f6] transition-all p-[5px]">
                    <el-avatar :size="30" :src="store.user.avatar" />
                    <span class="ml-[8px]">{{ store.user.nickname }}</span>
                </div>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
            <!-- 功能展示开关 -->
            <div class="hidden sm:flex items-center text-[20px] cursor-pointer p-[5px] hover:bg-[#f6f6f6] transition-all rounded-[5px]" @click="settingDrawer=true">
                <el-icon>
                    <Operation />
                </el-icon>
            </div>
        </div>
        <!-- 移动下展示功能栏 -->
        <div class="sm:hidden mx-[10px]">
            <div class="py-[10px] px-[30px] flex items-center bg-white mt-[20px] rounded-[20px]">
                <span>夜间聊天背景</span>
                <el-switch
                    v-model="isDark"
                    size="large"
                    class="ml-[30px]"
                    inline-prompt
                    :active-icon="MoonNight"
                    :inactive-icon="Sunrise"
                />
            </div>
            <div class="py-[10px] px-[30px] flex items-center sm:hidden bg-white mt-[20px] rounded-[20px]">
                <span>灰色模式</span>
                <el-switch
                    v-model="isGrey"
                    size="large"
                    class="ml-[30px]"
                    inline-prompt
                    :active-icon="Check"
                    :inactive-icon="Close"
                />
            </div>
        </div>
        <el-drawer size="300" v-model="settingDrawer" title="I am the title" :with-header="false">
            <el-divider content-position="center">界面展示</el-divider>
            <div class="flex items-center justify-between mt-[20px] rounded-[20px]">
                <span>夜间聊天背景</span>
                <el-switch
                    v-model="isDark"
                    size="large"
                    inline-prompt
                    :active-icon="MoonNight"
                    :inactive-icon="Sunrise"
                />
            </div>
            <div class="flex items-center justify-between mt-[20px] rounded-[20px]">
                <span>灰色模式</span>
                <el-switch
                    v-model="isGrey"
                    size="large"
                    class="ml-[30px]"
                    inline-prompt
                    :active-icon="Check"
                    :inactive-icon="Close"
                />
            </div>
        </el-drawer>

        <!-- 用户展示表单 -->
        <el-tabs v-model="activeName" class="bg-white flex-1 m-[10px] px-[20px] py-[10px]" @tab-click="handleClick">
            <el-tab-pane label="基础信息" name="first">
                <div class="base-user">
                    账号设置
                    <el-link class="ml-[20px]" type="primary" :icon="Edit" @click="editDrawer=true">修改</el-link>
                </div>
                <div class="my-[30px] flex h-[200px] gap-[30px] flex-col sm:flex-row">
                    <el-avatar class="flex-shrink-0 w-[80px] h-[80px] sm:w-[100px] sm:h-[100px]" :src="store.user.avatar" />
                    <div class="text-[#999]">
                        <div class="user-info">
                           <span>昵称：</span>
                           <span>{{ store.user.nickname }}</span>
                        </div>
                        <div class="user-info">
                           <span>性别：</span>
                           <span>{{ getSex(store.user.sex) }}</span>
                        </div>
                        <div class="user-info flex">
                           <span class="flex-shrink-0">个性签名：</span>
                           <span>{{ store.user.description ?? '暂无' }}</span>
                        </div>
                    </div>
                </div>
                <div class="base-user mb-[20px]">
                    更多信息
                </div>
                <el-descriptions :column="1">
                    <el-descriptions-item label="QO号:">{{ store.user.qoNum }}</el-descriptions-item>
                    <el-descriptions-item label="绑定手机:">{{ store.user.phone ?? '暂无' }}</el-descriptions-item>
                    <el-descriptions-item label="注册时间:">{{ handlerTime(store.user.createTime) }}</el-descriptions-item>
                </el-descriptions>
            </el-tab-pane>
            <el-tab-pane label="安全设置" name="second">
                <div class="base-user">
                    账号绑定
                </div>
                <div class="user-info">
                    <span>绑定手机号：</span>
                    <span>{{ store.user.phone ?? '暂无' }}</span>
                </div>
                <div class="user-info">
                    <span>绑定邮箱：</span>
                    <span>{{ store.user.email ?? '暂无' }}</span>
                </div>
            </el-tab-pane>
        </el-tabs>

        <el-drawer
            v-model="editDrawer"
            :with-header="false"
            :size="isSm? '400': '100%'"
        >
            <div class="flex flex-col h-full">
                <div class="mt-[20px]">
                    <div class="base-user">修改个人信息</div>
                    <user-form v-if="editDrawer"/>
                </div>
            </div>
        </el-drawer>
    </div>
</template>
<style lang='scss' scoped>
.base-user {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    
    &::before {
        content: '';
        height: 15px;
        width: 4px;
        margin-right: 5px;
        background: linear-gradient(0deg, #4458AB 25%,#2D6AB9 75%);
    }
}
.user-info {
    height: 30px;
}
</style>

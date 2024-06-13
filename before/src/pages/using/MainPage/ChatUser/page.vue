<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import UserDetail from './user-detail.vue'
import UserList from './user-list.vue'
import { useDebounce } from '@/utils/useDebounce'
import { User } from '@/constant/types'
import { GetAddUser, AddUser } from '@/axios/user'
import { useToast } from 'vue-toastification'



const toast = useToast()
// 当前打开的消息列表
const openList = ref<any>()
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
// 抽屉展示
const drawerModel = ref(false)
const swipedown = () => {
    // 关闭抽屉
    drawerModel.value = false
    // 清空当前
    openList.value = undefined
}
// 选中用户
const selectedUser = (userItem: any) => {
    drawerModel.value = true
    setTimeout(()=>{
        openList.value = userItem
    }, 500)
}

const userListRef = ref()
const updateData = () => {
    userListRef.value.getDataList()
}
// 添加好友框
const addDrawer = ref(false)
const openDrawer = async () => {
    addDrawer.value = true
}
const getUserList = async () => {
    if (!searchName.value) return
    const res = await GetAddUser(searchName.value)
    renderAddUser.value = res
}
const searchName = ref('')
const renderAddUser = ref<User[]>()
const goBack = () => {
    addDrawer.value = false
}

const addUser = (qoNum: string) => {
    AddUser(qoNum).then(() => {
        toast.success('已发送好友申请！')
        getUserList()
    })
}
</script>

<template>
    <div class="chat-content">
        <!-- 消息列表 -->
        <div class="chat-list">
            <div class="list-top">
                <el-button class="w-full" @click="openDrawer">添加好友</el-button>
            </div>
            <div class="flex-1 min-h-0">
                <!-- 消息列表，绑定 -->
                <UserList ref="userListRef" @click="selectedUser"/>
            </div>
        </div>
        <!-- 聊天窗口 -->
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn animate__faster"
            leave-active-class="animate__animated animate__fadeOut animate__faster">
            <div v-if="isSm" class="flex-1">
                <UserDetail ref="userListRef" :data="openList" :getUserList="updateData" :clearDetail="swipedown"/>
            </div>
        </transition>
        <!-- 移动端窗口 -->
        <el-drawer
            v-if="!isSm"
            style="--el-drawer-padding-primary: 0;"
            v-model="drawerModel"
            direction="btt"
            :with-header="false"
            size="100%">
            <div class="h-full">
                <UserDetail :data="openList" @swipedown="swipedown" :getUserList="updateData" :clearDetail="swipedown"/>
            </div>
        </el-drawer>

        <el-drawer
            v-model="addDrawer"
            :with-header="false"
            :size="isSm? '300': '100%'"
        >
            <div class="flex flex-col h-full">
                <el-page-header @back="goBack" />
                <div class="flex items-center mt-[10px] mb-[10px]">
                    <el-input v-model="searchName" placeholder="昵称搜索" :prefix-icon="Search"/>
                    <el-button @click="getUserList">搜索</el-button>
                </div>
                <div class="flex-1 min-h-0 overflow-y-auto">
                    <div class="flex p-[10px] items-center" v-for="user in renderAddUser" :key="user.qoNum">
                        <div class="flex items-center">
                            <el-avatar :size="40" class="mr-[10px]" :src="user.avatar" />
                            <span>{{ user.nickname }}</span>
                        </div>
                        <el-button class="ml-auto" type="success" @click="addUser(user.qoNum)">添加</el-button>
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<style lang="scss" scoped>
.chat-list {
    @apply
    h-full
    w-full
    sm:w-[220px]
    xl:w-[280px]
    bg-[#f7f7f7]
    flex
    overflow-hidden
    flex-col;
    transition: all cubic-bezier(0.74,-0.02, 0.44, 0.82) .4s;

    .list-top {
        @apply
        flex
        items-center
        p-[10px];
    }
}
</style>
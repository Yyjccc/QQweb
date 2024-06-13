<script setup lang='ts'>
import { GetAddGroupUser, OutGroup, InviteUserToGroup } from '@/axios/user';
import { Group, User } from '@/constant/types';
import { Icon } from '@iconify/vue';
// @ts-ignore
import { pinyin } from 'pinyin-pro'
import { onMounted, onUnmounted, ref } from 'vue';
import { useToast } from 'vue-toastification';
import moment from 'moment';
import { useUserStore } from '@/pinia/user'
import { useDebounce } from '@/utils/useDebounce';

const userStore = useUserStore()
const toast = useToast()
const props = defineProps<{
    data: Group | undefined,
    getGroupData: Function,
    clearDetail: Function,
    renderUser: User[] | [],
}>()

const emits = defineEmits<{
    (name: 'swipedown'): void
}>()
// 向下滑动触发返回
const swipedown = () => {
    console.log(1)
    emits('swipedown')
}

const outGroup = () => {
    OutGroup(props.data!.groupNum).then(() => {
        toast.success('已退出' + props.data!.groupName)
        props.getGroupData()
        props.clearDetail()
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
// 添加好友框
const addDrawer = ref(false)
const openDrawer = async () => {
    getUserList()
    addDrawer.value = true
}
const getUserList = async () => {
    const res = await GetAddGroupUser(props.data!.groupNum)
    renderAddUser.value = res
}
const renderAddUser = ref<User[]>()
const goBack = () => {
    addDrawer.value = false
}

const inviteUserToGroup = (qoNum: string) => {
    InviteUserToGroup(qoNum, props.data!.groupNum).then(() => {
        toast.success('邀请成功！')
        getUserList()
    })
}
</script>
<template>
    <div class="h-full flex justify-center items-center" v-hammer="{ swipedown }">
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn animate__faster"
            leave-active-class="animate__animated animate__fadeOut animate__faster"
        >
            <div v-if="data" class="text-center w-[450px]">
                <div class="flex justify-center">
                    <el-avatar shape="square" :size="70" :src="data.groupAvatar" />
                    <!-- 对好友的更多操作 -->
                    <el-dropdown trigger="click">
                        <Icon
                            icon="iwwa:option-horizontal" 
                            class="focus:outline-none hover:bg-[#dddddd] active:bg-[#dddddd] transition-all rounded-[3px] mx-[20px]" width="20" color="#7a7a7a"
                        />
                        <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>
                                <el-link :underline="false" type="danger" @click="outGroup">退出群聊</el-link>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
                <el-divider class="my-[30px]" />
                <div class="flex mb-[10px] ml-[50px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right">群名称：</div>
                    <div class="text-left flex items-center relative">
                        <span class="flex-shrink-0 mr-[5px]">{{ data.groupName }}</span>
                        <!-- 音标 -->
                        <p class="absolute -top-[18px] left-0 text-[12px] w-[200%]">{{ pinyin(data.groupName ?? '') }}</p>
                    </div>
                </div>
                <div class="flex mb-[10px] ml-[50px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right">群号：</div>
                    <div class="text-left">
                        {{ data.groupNum }}
                    </div>
                </div>
                <div class="flex mb-[10px] ml-[50px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right flex-shrink-0">群公告：</div>
                    <div class="text-left">
                        {{ data.groupRemark ?? '暂无' }}
                    </div>
                </div>
                <div class="flex ml-[50px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right flex-shrink-0">创建时间：</div>
                    <div class="text-left">
                        {{ handlerTime(data.createTime) }}
                    </div>
                </div>
                <el-divider class="my-[15px]" />
                <div class="h-[200px] overflow-y-auto grid grid-cols-2 gap-[10px]" style="align-content: start;">
                    <div class="flex items-center justify-center" v-for="user in renderUser" :key="user.qoNum + 'group'">
                        <img class="w-[40px] h-[40px] object-cover rounded-full flex-shrink-0" :src="user.avatar" />
                        <span class="ml-[10px] flex-shrink-0" :class="{
                            'is-self': userStore.user.qoNum === user.qoNum
                        }">{{ user.qoNum }}</span>
                    </div>
                    <div class="flex items-center cursor-pointer hover:text-[#999] justify-center py-[5px]" @click="openDrawer">
                        + 邀请好友
                    </div>
                </div>
            </div>
            <img v-else src="@/assets/img/icon.png" class="w-[70px] h-[70px] opacity-30" alt="">
        </transition>
        <el-drawer
            v-model="addDrawer"
            :with-header="false"
            :size="isSm? '300': '100%'"
        >
            <div class="flex flex-col h-full">
                <el-page-header @back="goBack" />
                <p class="mb-[20px]">选择好友</p>
                <div class="flex-1 min-h-0 overflow-y-auto">
                    <div class="flex p-[10px] items-center" v-for="user in renderAddUser" :key="user.qoNum">
                        <div class="flex items-center">
                            <el-avatar :size="40" class="mr-[10px]" :src="user.avatar" />
                            <span>{{ user.nickname }}</span>
                        </div>
                        <el-button class="ml-auto" @click="inviteUserToGroup(user.qoNum)">邀请</el-button>
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>
<style scoped lang='scss'>
.is-self {
    &::after {
        content: '（自己）';
    }
}
</style>

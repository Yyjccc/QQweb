<script setup lang='ts'>
import { ref, reactive, watchEffect } from 'vue'
import { Icon } from '@iconify/vue';
// @ts-ignore
import { pinyin } from 'pinyin-pro'
import { User } from '@/constant/types';
import { DelUser, Send } from '@/axios/user';
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification';

const router = useRouter()
const toast = useToast()
const props = defineProps<{
    data: User,
    getUserList: Function,
    clearDetail: Function,
}>()

// 用户信息加载状态
const isLoading = ref(true)
const userInfoData = reactive<User>({
    qoNum: '',
    avatar: undefined,
    password: '',
    nickname: '',
    phone: undefined,
    email: undefined,
    sex: undefined,
    description: undefined,
    createTime: '',
    updateTime: '',
    delLogic: '0',
})
// 获取用户信息
const getUserInfo = (data: User) => {
    if (!props.data) return isLoading.value = false
    Object.assign(userInfoData, data)
    isLoading.value = false
}

// 监听每次的传值变化，同时第一次也要监听
watchEffect(() => {
    isLoading.value = true
    getUserInfo(props?.data)
})

const emits = defineEmits<{
    (name: 'swipedown'): void
}>()

// 向下滑动触发返回
const swipedown = () => {
    emits('swipedown')
}
// 发送一个默认消息后弹到聊天
const sendDefault = () => {
    Send({
        toUser: {...props.data}.qoNum,
        recordContent: '😀，你好呀！',
        recordType: 'text'
    })
    router.push('/using/chat-list')
}

// 删除好友
const delUser = () => {
    DelUser(props.data.qoNum).then(res => {
        toast.success('已删除' + props.data.nickname)
        props.getUserList()
        props.clearDetail()
    })
}
</script>
<template>
    <div class="h-full flex justify-center items-center" v-hammer="{ swipedown }" v-loading="isLoading">
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn animate__faster"
            leave-active-class="animate__animated animate__fadeOut animate__faster"
        >
            <div v-if="data" class="text-center w-[300px]">
                <div class="flex justify-center">
                    <el-avatar shape="square" :size="70" :src="userInfoData.avatar" />
                    <!-- 对好友的更多操作 -->
                    <el-dropdown trigger="click">
                        <Icon
                            icon="iwwa:option-horizontal" 
                            class="focus:outline-none hover:bg-[#dddddd] active:bg-[#dddddd] transition-all rounded-[3px] mx-[20px]" width="20" color="#7a7a7a"
                        />
                        <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>
                                <el-link :underline="false" type="danger" @click="delUser">删除好友</el-link>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
                <el-divider class="my-[30px]" />
                <div class="flex mb-[10px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right">昵称：</div>
                    <div class="text-left flex items-center relative">
                        <span class="flex-shrink-0 mr-[5px]">{{ userInfoData.nickname }}</span>
                        <!-- 性别图标 -->
                        <Icon icon="material-symbols:female" v-if="userInfoData.sex==='1'" width="20" color="#f37e7d" />
                        <Icon icon="material-symbols:male" v-else-if="userInfoData.sex==='0'" width="20" color="#10aeff" />
                        <!-- 音标 -->
                        <p class="absolute -top-[18px] left-0 text-[12px] w-[200%]">{{ pinyin(userInfoData.nickname ?? '') }}</p>
                    </div>
                </div>
                <div class="flex mb-[10px]">
                    <div class="w-[80px] text-[#9e9e9e] text-right">qo号：</div>
                    <div class="text-left">
                        {{ userInfoData.qoNum }}
                    </div>
                </div>
                <div class="flex">
                    <div class="w-[80px] text-[#9e9e9e] text-right flex-shrink-0">个性签名：</div>
                    <div class="text-left">
                        {{ userInfoData.description ?? '暂无' }}
                    </div>
                </div>
                <el-divider class="my-[15px]" />
                <button
                    class="w-[100px] bg-[#07c160] px-[25px] py-[7px] text-[13px] text-white mt-[10px] rounded-[4px]"
                    v-ripple="'white'"
                    @click="sendDefault"
                >
                    打招呼
                </button>
            </div>
            <img v-else src="@/assets/img/icon.png" class="w-[70px] h-[70px] opacity-30" alt="">
        </transition>
   </div>
</template>
<style scoped lang='scss'>
</style>

<script setup lang='ts'>
import type { ChatData, GetGroupMsgResp, MessageList } from '@/constant/types'
import ChatRoom from './chat-room.vue'
import ChatInput from './chat-input.vue'
import { Icon } from '@iconify/vue'
import {onBeforeMount, onMounted, onUnmounted, ref} from 'vue'
import { handlerTime } from '@/utils/moment'
import emojipedia from '@/components/emojipedia.vue'
import { useUserStore } from '@/pinia/user'
import { GetChatData, GetChatDataAll, GetGroupAllRecord, GetGroupRecord, Send, SendToGroup } from '@/axios/user'
import { useInterval } from '@/utils/useInterval'
import { useSettingStore } from '@/pinia/setting'
import { getToken } from '@/utils/handler-token'
import { UploadProps } from 'element-plus'
import { AxiosResponse } from 'axios'
import { useToast } from 'vue-toastification'
import websocketService from "@/websocket";


const toast = useToast()
const userStore = useUserStore()
const settingStore = useSettingStore()
const props = defineProps<{
    data: MessageList | GetGroupMsgResp | undefined,
    isGroup: boolean,
    isMobile: boolean
}>()
// 当前页
const current = ref(1)
// 聊天记录加载状态
const isLoading = ref(true)
// 等待状态, true 未选择聊天
const isNoSelected = ref(false)

// 获取下一页数据
const getMoreData = () => {
    return !props.isGroup ? 
        GetChatData(current.value + 1, (props.data as MessageList).user.qoNum) :
        GetGroupRecord(current.value + 1, (props.data as GetGroupMsgResp).groupNum)
}
// 根据页数获取数据
const getAllData = () => {
    return !props.isGroup ? 
        GetChatDataAll(current.value, (props.data as MessageList).user.qoNum) :
        GetGroupAllRecord(current.value, (props.data as GetGroupMsgResp).groupNum)
}
// 轮询获取聊天数据（全部）
const intervalGetData = () => {
    getAllData().then(data => {
        isLoading.value = false
        chatContent.value = data
    })
}
const { stopInterval, startInterval } = useInterval(1000, intervalGetData)
// 根据列表信息获取聊天记录
const getChatRecord = async () => {
    // 未选择
    if (!props.data) return isNoSelected.value = true
    console.log('刷新获取聊天记录', { ...props.data })
    // isLoading.value = true
    // 建立轮询
   //startInterval()
  getAllData().then(data => {
    isLoading.value = false
    chatContent.value = data
  })
}

const emits = defineEmits<{
    (name: 'clickMenu' | 'swipedown'): void,
    (name: 'sendUrl', content: string): void
}>()

// 主题环境
const themeMode = ref<'dark' | 'light'>(settingStore.isDark ? 'dark': 'light')

const chatContent = ref<ChatData[]>([])
onBeforeMount(() => {
    getChatRecord()
})
// 点击头像
const clickAvatar = (data: any) => {
    console.log(data)
}
// 加载更多
const loadMore = () => {
    // 停止轮询
   // stopInterval()
    // 获取上一页的数据
    getMoreData().then(res => {
        chatContent.value = [...res, ...chatContent.value]
        // 继续使用轮询发送
        //startInterval()
      getAllData().then(data => {
        isLoading.value = false
        chatContent.value = data
      })
        if (!res.length) return
        current.value++
    })
}
const chatRoomRef = ref()
// 发送
const send = (value: string, type: 'text' | 'image') => {
    props.isGroup ? 
    SendToGroup({
        toGroup: (props.data as GetGroupMsgResp).groupNum,
        recordContent: value,
        recordType: type
    }): 
     Send({
        toUser: (props.data as MessageList).user.qoNum,
        recordContent: value,
        recordType: type
    })
}
// 当前消息列表状态
const isMsgShut = ref(false)
// 点击收缩icon
const changeMsgShut = () => {
    isMsgShut.value = !isMsgShut.value
    emits('clickMenu')
}
// 监听向下滑动头部关闭
const swipedown = () => {
    if (!props.isMobile) return
    emits('swipedown')
}
const chatInputRef = ref()
// 选中表情
const selectedEmo = (emo: string) => {
    console.log(emo, '选择了')
    chatInputRef.value.insertTo(emo)
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile: File) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        toast.error('图片格式必须为jpg或png！')
        return false
    } else if (rawFile.size / 1024 / 1024 > 2) {
        toast.error('图片大小不能超过2MB！')
        return false
    }
    return true
}

onMounted(() => {
  websocketService.addListener( getChatRecord);

  // 轮询获取，1秒重新获取列表
  //startInterval()
})

onUnmounted(() => {
  websocketService.removeListener(getChatRecord);
});
const handleAvatarSuccess: UploadProps['onSuccess'] = ({ data }: AxiosResponse) => {
    const path = '../' + data.url.split('\\src\\')[1]
    send(new URL(path, import.meta.url).href, 'image')
}
</script>
<template>
    <div class="flex w-full h-full flex-col">
        <!-- 聊天窗口头部 -->
        <div class="chat-head" v-hammer="{ swipedown }">
            <!-- 左侧收缩 -->
            <div class="chat-head-left" @click="changeMsgShut" v-if="!isMobile">
                <!-- 收缩消息列表 -->
                <Icon
                    :icon="!isMsgShut ? 'bi:menu-button-fill': 'bi:menu-button'"
                    class="text-[#333] text-[20px] ml-[15px]"
                />
            </div>
            <!-- 聊天对象 -->
            <div class="pointer-events-none text-center flex items-center" v-if="!isNoSelected">
                <el-avatar
                    shape="square"
                    :size="40"
                    class="mr-[10px]"
                    :src="isGroup ? (data as GetGroupMsgResp).groupAvatar : (data as MessageList).user.avatar"
                />
                <div>
                    <p class="text-[15px] flex items-center">
                        <span>{{ isGroup ? (data as GetGroupMsgResp).groupName : (data as MessageList).user.nickname }}</span>
                        <Icon icon="tabler:message-chatbot" class="ml-[5px]" color="#333" width="17" />
                    </p>
                    <p class="text-[10px]">{{ handlerTime(data?.record.createTime || '') }}</p>
                </div>
            </div>
        </div>
        <div class="flex flex-col flex-1 min-h-0 bg-gray-100" v-loading="isLoading" v-show="!isNoSelected">
            <div class="flex-1 min-h-0">
                <ChatRoom
                    ref="chatRoomRef"
                    :themeMode="themeMode"
                    :chat-content="chatContent"
                    :qo-num="userStore.user.qoNum"
                    @clickAvatar="clickAvatar"
                    @loadMore="loadMore"
                />
            </div>
            <div class="h-auto flex flex-col sm:h-[230px]">
                <div class="flex items-center p-[5px]" :style="{
                    '--el-color-primary': 'transparent'
                }">
                    <!-- 表情模块 -->
                    <emojipedia @selected="selectedEmo"/>
                    <el-upload
                        class="flex items-center"
                        :show-file-list="false"
                        action="/api/user/upload"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                        :headers="{
                            token: getToken() || ''
                        }"
                    >
                        <Icon icon="ph:image-light" class="ml-[5px] p-[5px] text-[31px] focus:bg-[#d8dbdd] hover:bg-[#d8dbdd] transition-[background] rounded-[5px]" color="#1b1b1c" />
                    </el-upload>
                </div>
                <div class="flex-1">
                    <ChatInput ref="chatInputRef" @send="value => send(value, 'text')"/>
                </div>
            </div>
        </div>
        <div class="h-full w-full flex justify-center items-center bg-gray-100" v-if="isNoSelected">
            <img src="@/assets/img/icon.png" class="w-[70px] h-[70px] opacity-30" alt="">
        </div>
    </div>
</template>
<style scoped lang='scss'>
.chat-head {
    @apply
    relative
    h-[60px]
    p-[10px]
    flex
    justify-center
    items-end;

    .chat-head-left {
        @apply
        absolute
        left-0
        top-0
        h-full
        w-[50px]
        flex
        items-center;
    }
}
</style>

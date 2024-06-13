<script setup lang='ts'>
import {ref, onMounted, onUnmounted} from 'vue'
import type { GetGroupMsgResp, MessageList } from '@/constant/types';
import { GetGroupMsg, GetMessage } from '@/axios/user';
import { useInterval } from '@/utils/useInterval';
import websocketService from "@/websocket";

const emits = defineEmits<{
    (name: 'click', content: MessageList | GetGroupMsgResp): void
}>()

const props = defineProps<{
    search: string
}>()

// 点击列表
const openChat = (content: MessageList | GetGroupMsgResp) => {
    selectMessageId.value = (content as GetGroupMsgResp).groupNum ?? (content as MessageList).user.qoNum
    // 传递给上一个组件
    emits('click', content)
}

// 当前选中列表id
const selectMessageId = ref<undefined | string | number>()
// 消息列表数据
const msgList = ref<MessageList[]>([])
// 群组列表
const groupList = ref<GetGroupMsgResp[]>()
const isLoading = ref(true)
// 获取数据
const getMsgList = async () => {
    if (isLoading.value) isLoading.value = false

    msgList.value = await GetMessage(props.search)

    groupList.value = await GetGroupMsg()
}
const { startInterval, stopInterval } = useInterval(1500, getMsgList)
onMounted(() => {
  websocketService.addListener( getMsgList);
  getMsgList();
    // 轮询获取，1秒重新获取列表
   //startInterval()
})

onUnmounted(() => {
  websocketService.removeListener(getMsgList);
});
</script>
<template>
    <!-- 消息列表 -->
    <div class="h-full overflow-auto bg-[#e8e6e6]" v-loading="isLoading">
        <!-- 群组 -->
        <div class="h-[70px] px-[10px] flex items-center cursor-default hover:bg-[#cac8c7] transition-all duration-150"
            :class="{
                'isSelected': selectMessageId === chatItem.groupNum
            }" v-for="chatItem in groupList" :key="chatItem.record?.recordId" @click="openChat({ ...chatItem })">
            <el-avatar class="flex-shrink-0" shape="square" :src="chatItem?.groupAvatar" />
            <div class="ml-[10px] min-w-0">
                <p class="text-[14px]">{{ chatItem.groupName }}（群聊）</p>
                <p class="text-[12px] text-ellipsis whitespace-nowrap overflow-hidden">
                    {{ chatItem.record.recordType==='image'? '[图片]': chatItem.record.recordContent }}
                </p>
            </div>
        </div>
        <!-- 用户 -->
        <div class="h-[70px] px-[10px] flex items-center cursor-default hover:bg-[#cac8c7] transition-all duration-150"
            :class="{
                'isSelected': selectMessageId === chatItem.user.qoNum
            }" v-for="chatItem in msgList" :key="chatItem.record.recordId" @click="openChat({ ...chatItem })">
            <el-avatar class="flex-shrink-0" :src="chatItem.user.avatar" />
            <div class="ml-[10px] min-w-0">
                <p class="text-[14px]">{{ chatItem.user.nickname }}</p>
                <p class="text-[12px] text-ellipsis whitespace-nowrap overflow-hidden">
                    {{ chatItem.record.recordType==='image'? '[图片]': chatItem.record.recordContent }}
                </p>
            </div>
        </div>
    </div>
</template>
<style scoped lang='scss'>
.isSelected {
    @apply bg-[#cac8c7];
}
</style>

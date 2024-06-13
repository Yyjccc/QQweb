<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import MessageList from './message-list.vue'
import ChatContent from '@/components/chat-content.vue'
import { Search } from '@element-plus/icons-vue'
import type { GetGroupMsgResp, MessageList as IMessageList } from '@/constant/types'
import { useDebounce } from '@/utils/useDebounce'

// 搜索框
const searchValue = ref('')
// 当前打开的消息列表
const openList = ref<IMessageList | GetGroupMsgResp | undefined>()
const chatRoomKey = ref(0)
// 子组件点击列表
const clickList = (content: IMessageList | GetGroupMsgResp) => {
    console.log('点击列表', content)
    chatRoomKey.value++
    openList.value = content
    drawerModel.value = true
}
// 消息列表是否缩小
const isListShut = ref(false)
const clickMenu = () => {
    isListShut.value = !isListShut.value
}

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

const drawerModel = ref(false)
</script>

<template>
    <div class="chat-content">
        <!-- 消息列表 -->
        <div class="chat-list" :class="{
            'shut-list': isListShut           
        }">
            <div class="list-top">
                <!-- 简单搜索框 -->
                <el-input
                    v-model="searchValue"
                    size="large"
                    class="ml-[10px]"
                    placeholder="好友昵称"
                    :prefix-icon="Search"
                    clearable
                />
            </div>
            <!-- 消息列表，绑定 -->
            <MessageList :search="searchValue" @click="clickList"/>
        </div>
        <!-- 聊天窗口 -->
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn animate__faster"
            leave-active-class="animate__animated animate__fadeOut animate__faster">
            <div class="flex-1" v-if="isSm">
                <ChatContent
                    :key="chatRoomKey"
                    :data="openList"
                    @click-menu="clickMenu" 
                    :is-mobile="false"
                    :is-group="!!openList && 'groupName' in openList"
                />
            </div>
        </transition>
        <!-- 移动端聊天窗口 -->
        <el-drawer
            v-if="!isSm"
            style="--el-drawer-padding-primary: 0;"
            v-model="drawerModel"
            direction="btt"
            :with-header="false"
            size="100%">
            <div class="flex h-full" v-if="drawerModel">
                <ChatContent
                    v-if="drawerModel"
                    :key="'m' + chatRoomKey"
                    :data="openList"
                    @click-menu="clickMenu"
                    @swipedown="drawerModel=false"
                    :is-mobile="true"
                    :is-group="!!openList && 'groupName' in openList"
                />
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
.shut-list {
    @apply
    sm:w-0
    xl:w-0;
}
</style>
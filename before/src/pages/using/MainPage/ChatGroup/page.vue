<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { useDebounce } from '@/utils/useDebounce'
import { GetGroupList, GetUserByGroup } from '@/axios/user'
import { Group, User } from '@/constant/types'
import GroupDetail from './group-detail.vue'
import GroupForm from './group-form.vue';

// 当前打开的消息列表
const openList = ref<Group>()
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
const openGroupUser = ref<User[]>([])
// 选中
const selected = (item: Group) => {
    drawerModel.value = true
    GetUserByGroup(item.groupNum).then(res => {
        openGroupUser.value = res
        openList.value = item
    })
}
const renderList = ref<Group[]>([])
const getGroupData = () => {
    GetGroupList().then(res => {
        renderList.value = res
    })
}
onMounted(() => {
    getGroupData()
})
const clearDetail = () => {
    swipedown()
}
const addDrawer = ref(false)
const openDrawer = () => {
    addDrawer.value = true
}
const goBack = () => {
    addDrawer.value = false
}
const closeAddGroup = () => {
    goBack()
    getGroupData()
}
</script>

<template>
    <div class="chat-content">
        <!-- 消息列表 -->
        <div class="chat-list">
            <div class="list-top">
                <el-button class="w-full" @click="openDrawer">新建群组</el-button>
            </div>
            <div class="flex-1 min-h-0 bg-[#eae8e8]">
                <div
                    class="flex items-center h-[70px] gap-[10px] transition-all hover:bg-[#cac8c7] px-[10px] duration-150"
                    :class="{
                        'bg-[#cac8c7]': openList?.groupNum === group?.groupNum
                    }"
                    @click="selected(group)"
                    v-for="group in renderList"
                    :key="group?.groupNum"
                >
                    <el-avatar shape="square" class="flex-shrink-0" :size="45" :src="group?.groupAvatar"/>
                    <p class="text-[14px] truncate">
                        <span>{{ group?.groupName }}</span>
                    </p>
                </div>
            </div>
        </div>
        <!-- 聊天窗口 -->
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn animate__faster"
            leave-active-class="animate__animated animate__fadeOut animate__faster">
            <div v-if="isSm" class="flex-1">
                <GroupDetail :renderUser="openGroupUser" :data="openList" :clearDetail="clearDetail" :getGroupData="getGroupData"/>
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
                <GroupDetail :renderUser="openGroupUser" :data="openList" :clearDetail="clearDetail" :getGroupData="getGroupData" @swipedown="swipedown"/>
            </div>
        </el-drawer>

        <el-drawer
            v-model="addDrawer"
            :with-header="false"
            :size="isSm? '300': '100%'"
        >
            <div class="flex flex-col h-full">
                <el-page-header @back="goBack" />
                <div class="mt-[20px]">
                    <div class="base-user">新建群组</div>
                    <group-form v-if="addDrawer" :closeAddGroup="closeAddGroup"/>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<style lang="scss" scoped>
.base-user {
    display: flex;
    align-items: center;
    margin-bottom: 20px;

    &::before {
        content: '';
        height: 15px;
        width: 4px;
        margin-right: 5px;
        background: linear-gradient(0deg, #4458AB 25%, #2D6AB9 75%);
    }
}
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
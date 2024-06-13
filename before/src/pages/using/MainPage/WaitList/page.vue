<script setup lang='ts'>
import { GetWaitGroup, GetWaitUser, AgreeUser, AgreeGroup } from '@/axios/user'
import { Group, User } from '@/constant/types';
import { useInterval } from '@/utils/useInterval';
import { onMounted, ref } from 'vue';
import { useToast } from 'vue-toastification';


const toast = useToast()
const renderUser = ref<User[]>([])
const renderGroup = ref<Group[]>([])
const getData = async () => {
    const userList = await GetWaitUser()
    const groupList = await GetWaitGroup()
    noWaitUser.value = userList.length === 0
    noWaitGroup.value = groupList.length === 0
    renderUser.value = userList
    renderGroup.value = groupList
}
const { stopInterval, startInterval } = useInterval(3000, getData)
onMounted(() => {
    getData()

    //startInterval()
})

const noWaitUser = ref(false)
const noWaitGroup = ref(false)
const handlerUser = (qoNum: string, status: '0' | '1') => {
    AgreeUser(qoNum, status).then(() => {
        toast.success('已处理' + qoNum + '的好友请求！')
        getData()
    })
}

const handlerGroup = (groupNum: number, status: '0' | '1') => {
    AgreeGroup(groupNum, status).then(() => {
        toast.success('已处理' + groupNum + '的群组请求！')
        getData()
    })
}

const getSex = (sex: '0' | '1' | undefined) => {
    if (!sex) return '暂无'
    return sex === '0' ? '男' : '女'
}
</script>
<template>
    <div class="flex-1 p-[10px] sm:p-[20px] min-h-0 overflow-y-auto">
        <div class="base-user">待处理：好友请求</div>
        <el-collapse accordion class="lg:w-[75%] xl:w-[60%]" v-if="!noWaitUser">
            <el-collapse-item
                name="1"
                :style="{
                    '--el-collapse-header-height': '70px'
                }"
                v-for="user in renderUser"
                :key="user.qoNum"
            >
                <template #title>
                    <div class="pl-[10px] flex items-center">
                        <el-avatar :size="40" class="mr-[10px]" :src="user.avatar" />
                        <span class="flex-1 min-w-0 tracking-widest">{{ user.qoNum }}</span>
                    </div>
                </template>
                <div class="px-[20px] flex flex-col justify-between sm:flex-row gap-[10px]">
                    <div>
                        <el-tag v-if="user.sex">{{ getSex(user.sex) }}</el-tag>
                        <el-tag v-if="user.description" type="success">{{ user.description }}</el-tag>
                    </div>
                    <div class="flex items-center">
                        <el-button class="ml-auto" @click="handlerUser(user.qoNum, '0')">同意</el-button>
                        <el-button class="ml-auto" type="danger" @click="handlerUser(user.qoNum, '1')">拒绝</el-button>
                    </div>
                </div>
            </el-collapse-item>
        </el-collapse>
        <div v-else class="text-[#999]">暂无好友请求</div>
        <div class="base-user mt-[20px]">待处理：群组邀请</div>
        <el-collapse accordion class="lg:w-[75%] xl:w-[60%]" v-if="!noWaitGroup">
            <el-collapse-item
                name="1"
                :style="{
                    '--el-collapse-header-height': '70px'
                }"
                v-for="group in renderGroup"
                :key="group.groupNum"
            >
                <template #title>
                    <div class="pl-[10px] flex items-center">
                        <el-avatar shape="square" :size="40" class="mr-[10px]" src="" />
                        <span class="flex-1 min-w-0 tracking-widest">{{ group.groupName }}</span>
                    </div>
                </template>
                <div class="px-[20px] flex flex-col justify-between sm:flex-row gap-[10px]">
                    <div>
                        {{ group.groupRemark }}
                    </div>
                    <div class="flex items-center">
                        <el-button class="ml-auto" @click="handlerGroup(group.groupNum, '0')">同意</el-button>
                        <el-button class="ml-auto" type="danger" @click="handlerGroup(group.groupNum, '1')">拒绝</el-button>
                    </div>
                </div>
            </el-collapse-item>
        </el-collapse>
        <div v-else class="text-[#999]">暂无群组邀请</div>
    </div>
</template>
<style scoped lang='scss'>
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
.wait-list {
    @apply h-[70px] bg-white rounded-xl lg:w-[75%] xl:w-[60%] 2xl:w-[40%] flex items-center py-[10px] px-[10px] sm:px-[20px];

    +.wait-list {
        margin-top: 10px;
    }
}
.el-tag {
    margin: 5px;
}
</style>

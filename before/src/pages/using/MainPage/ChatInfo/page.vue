<script setup lang='ts'>
import { useDebounce } from '@/utils/useDebounce'
import { onMounted, onUnmounted, ref } from 'vue'

// 展示列数
const listColumn = ref(3)
// 监听屏幕大小变化
const handlerSide = () => {
    const clientWidth = document.documentElement.clientWidth
    const isXl = clientWidth >= 1024
    const isSm = clientWidth >= 640
    listColumn.value = 1
    if (isSm) listColumn.value = 2
    if (isXl) listColumn.value = 3
}
const changeSide = useDebounce(handlerSide)

onMounted(() => {
    window.addEventListener('resize', changeSide)
    handlerSide()
})
onUnmounted(() => {
    window.removeEventListener('resize', changeSide)
})
</script>
<template>
    <div class="flex-1 chat-info min-h-0 overflow-y-auto">
        <div class="p-[15px] text-[#303133] bg-white">
            <el-divider content-position="left">关于</el-divider>
                QO-Chat 是一个基于Vue3、TypeScript的在线聊天室项目，作为FAS的毕业设计项目，请等待后续接入GPT和更多功能
            <el-divider />
        </div>
        <div class="bg-white m-[20px]">
            <p class="p-[20px]">开发者信息</p>
            <el-divider class="m-0"/>
            <el-descriptions class="p-[20px]" border :column="listColumn">
                <el-descriptions-item label="开发者">FAS</el-descriptions-item>
                <el-descriptions-item label="手机号">17346834560</el-descriptions-item>
                <el-descriptions-item label="居住地">
                    温州
                </el-descriptions-item>
                <el-descriptions-item label="标签">
                    <el-tag size="small" class="m-[5px]" type="success">热爱前端</el-tag>
                    <el-tag size="small" class="m-[5px]">应届生</el-tag>
                    <el-tag size="small" class="m-[5px]" type="danger">好学</el-tag>
                    <el-tag size="small" class="m-[5px]" type="info">专科</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="个性签名">
                    好的坏的都会过去，时间是一直在走的
                </el-descriptions-item>
            </el-descriptions>
        </div>
        <div class="bg-white m-[20px]">
            <p class="p-[20px]">项目信息</p>
            <el-divider class="m-0"/>
            <el-descriptions class="p-[20px]" border :column="listColumn">
                <el-descriptions-item label="项目名称">QO-Chat</el-descriptions-item>
                <el-descriptions-item label="介绍">只比QQ少一点的在线聊天室</el-descriptions-item>
                <el-descriptions-item label="版本">
                    @0.0.1-beta
                </el-descriptions-item>
            </el-descriptions>
        </div>
   </div>
</template>
<style lang='scss'>
.chat-info .el-divider__text {
    font-size: 20px;
}
</style>

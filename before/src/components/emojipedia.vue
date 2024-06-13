<script setup lang='ts'>
import { Icon } from '@iconify/vue'

const emit = defineEmits<{
    (name: 'selected', content: string): void
}>()
// 选中表情
const selected = (e: Event) => {
    emit('selected', (e?.target as HTMLSpanElement).innerText)
}
// 全部表情
const emoList = [
    '😀',
    '😅',
    '🙂',
    '😍',
    '😜',
    '🤣',
    '🙃'
]
</script>
<template>
    <el-popover
        placement="top-start"
        title="Select..."
        :width="500"
        trigger="click"
    >
        <template #default>
            <div class="h-[100px] overflow-auto flex flex-wrap content-start">
                <p class="cursor-default select-none p-[3px] text-[18px]" @click="selected" v-for="emo in emoList" :key="emo">{{emo}}</p>
            </div>
        </template>
        <template #reference>
            <Icon
                class="focus:outline-none focus:bg-[#d8dbdd] hover:bg-[#d8dbdd] transition-[background] rounded-[5px] p-[5px] text-[30px]"
                icon="solar:smile-square-broken"
            />
        </template>
    </el-popover>
</template>
<style scoped lang='scss'>
</style>

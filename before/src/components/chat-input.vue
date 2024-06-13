<script setup lang='ts'>
import { ref } from 'vue'

// input
const textarea = ref('')
let curStart = 0
let curEnd = 0

const emits = defineEmits<{
    (name: 'send', content: string): void
}>()
// 空白提示
const isNull = ref(false)
// 发送
const send = () => {
    // 为空
    if (!textarea.value.trim()) return isNull.value = true
    isNull.value = false
    emits('send', textarea.value)
    textarea.value = ''
}
const handlerOutSide = () => {
    isNull.value = false
}
// input ref
const inputRef = ref()
/**
 * 对字符串新增或替换，如
 * addTextToCur('1', '2345') // '12345'
 * addTextToCur('1', '2', 0, 0) // '21'
 * addTextToCur('1', '2', 0, 1) // '2'
 * addTextToCur('1', '2', 0, 2) // '2'
 * @param str 操作字符串
 * @param newStr 新增或替换的字符串
 * @param start 新增下标，默认追加
 * @param end 结束下标，默认追加
 */
const addTextToCur = (
        str: string,
        newStr: string,
        start: number = str.length,
        end: number = str.length
    ) => {
    const minStart = Math.min(start, str.length)
    const minEnd = Math.min(end, str.length)
    return str.substring(0, minStart) +
        newStr + 
        str.substring(minEnd)
}
// 插入文字到上次光标位置，如果选中多个则是替换
const insertTo = (str: string) => {
    const newStr = addTextToCur(textarea.value, str, curStart, curEnd)
    textarea.value = newStr
    // 插入之后追增光标
    curStart += str.length
    curEnd += str.length
}
defineExpose({
    insertTo
})
// 失去焦点：记录光标位置
const blur = () => {
    curStart = inputRef.value.ref.selectionStart
    curEnd = inputRef.value.ref.selectionEnd
    console.log('选中', curStart, curEnd)
}
</script>
<template>
    <div class="flex h-full flex-col">
        <el-input
            ref="inputRef"
            @keydown.enter.prevent="send"
            v-model="textarea"
            @blur="blur"
            class="chat-input"
            placeholder="输入聊天文字..."
            type="textarea">
        </el-input>
        <el-tooltip :visible="isNull" placement="top-end">
            <template #content>
                <span>不能发送空白消息</span>
            </template>
            <button class="send-btn" v-click-outside="handlerOutSide" @click="send">
                发送(S)
            </button>
        </el-tooltip>
    </div>
</template>
<style lang='scss'>
.chat-input {
    @apply
    relative
    flex-1
    text-justify
    tracking-wide;

    .el-textarea__inner {
        @apply
        h-full
        bg-transparent
        shadow-none
        resize-none
        text-[17px];
    }
}

.send-btn {
    @apply
    w-[100px]
    h-[35px]
    ml-auto
    mr-[20px]
    mb-[15px]
    mt-[10px]
    text-[14px]
    transition-all
    rounded-[5px]
    active:bg-[#c6c6c6]
    hover:bg-[#d2d2d2]
    text-[#07c160]
    bg-[#e9e9e9];
}
</style>

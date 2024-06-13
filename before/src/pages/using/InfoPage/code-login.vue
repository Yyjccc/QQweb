<script setup lang='ts'>
const emit = defineEmits(['back'])
</script>

<template>
    <div>
        <!-- 放二维码的框 -->
        <div
            class="w-[200px] h-[200px] bg-slate-200 mx-auto flex justify-center items-center"
            v-motion
            :initial="{
                opacity: 0,
                y: 150,
            }"
            :enter="{
                opacity: 1,
                y: 0,
                transition: {
                    type: 'spring',
                    mass: 0.6
                },
            }">
            <!-- 二维码 -->
            <div class="w-[160px] h-[160px] bg-slate-400"></div>
        </div>
        <el-divider 
            v-motion
            :initial="{
                opacity: 0,
                y: 150,
            }"
            :enter="{
                opacity: 1,
                y: 0,
                transition: {
                    type: 'spring',
                    mass: 0.6,
                    delay: 40
                },
            }">
            <p class="text-[#6b7280] text-[12px] w-[200px] text-center">扫码后点击“确认”，即可完成登录</p>
        </el-divider>
        <div
            class="pt-[15px]"
            v-motion
            :initial="{
                opacity: 0,
                y: 150,
            }"
            :enter="{
                opacity: 1,
                y: 0,
                transition: {
                    type: 'spring',
                    mass: 0.6,
                    delay: 80
                },
            }">
            <el-button
                class="w-full"
                @click="emit('back')">
                返回
            </el-button>
        </div>
    </div>
</template>
<style scoped lang='scss'>
</style>

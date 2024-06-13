<script setup lang='ts'>
import { Iphone, Connection } from '@element-plus/icons-vue'
import { useToast } from 'vue-toastification'

const emit = defineEmits(['back'])

const toast = useToast()
const waitOut = () => {
    toast.info('请等待后续开发！')
}
</script>

<template>
   <el-form class="form-content">
        <el-form-item
            size="large"
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
                },
            }">
            <el-input
                placeholder="手机号码"
                :prefix-icon="Iphone"
            />
        </el-form-item>
        <el-form-item
            size="large"
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
            <div class="flex w-full h-full">
                <el-input
                    placeholder="短信验证码"
                    :prefix-icon="Connection"
                />
                <el-button class="ml-[10px]" @click="waitOut">获取验证码</el-button>
            </div>
        </el-form-item>
        <el-form-item
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
            <el-button class="w-full" type="primary" @click="waitOut">登录</el-button>
        </el-form-item>
        <el-form-item
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
                    delay: 120
                },
            }">
            <el-button class="w-full" @click="emit('back')">返回</el-button>
        </el-form-item>
    </el-form>
</template>
<style scoped lang='scss'>
.form-content {
    .more-login {
        @apply
        flex
        w-full
        justify-around;

        > svg {
            @apply
            text-[#666]
            hover:text-[#60a5fa]
            cursor-pointer;
        }
    }
}
</style>

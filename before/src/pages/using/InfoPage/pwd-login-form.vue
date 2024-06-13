<script setup lang='ts'>
import { ref, reactive, onMounted } from 'vue'
import { User, Lock, Connection } from '@element-plus/icons-vue'
import { Icon } from '@iconify/vue';
import type { BaseForm } from '@/constant/types'
import { 
    qoNum,
    password,
    code,
    validForm
} from '@/constant/element-valid'
import { FormRules, FormInstance } from 'element-plus'
import { useRouter } from 'vue-router'
import { POSITION, useToast } from 'vue-toastification';
import { setToken } from '@/utils/handler-token'
import { GetCaptchaImg, Login } from '@/axios/base';


const router = useRouter()
const toast = useToast()

const emit = defineEmits<{
    (name: 'toPhone'): void,
    (name: 'toCode'): void,
    (name: 'toRegister'): void,
    (name: 'toForget'): void,
    (name: 'toEmail'): void
}>()
// 登录表单
const formData = reactive<BaseForm>({
    qoNum: '',
    password: '',
    code: '',
    codeKey: ''
})
// 表单验证
const formRules: FormRules = {
   
}
// 表单对象
const formRef = ref<FormInstance | undefined>()
// 登录
const login = async () => {
    try {
        isLoading.value = true
        const { token } = await Login({
            ...formData,
            codeKey: captchaKey.value,
            qoNum: formData.qoNum
        })
        toast.success('欢迎您，' + formData.qoNum, {
            position: POSITION.TOP_RIGHT
        })
        setToken(token)
        isLoading.value = false
        router.push('/using')
    } catch (err) {
        isLoading.value = false
    }
}
// 验证码base64
const captchaBase64 = ref('')
// 图片key
const captchaKey = ref('')
// 获取验证码图片
const getCaptchaImg = async () => {
    const res = await GetCaptchaImg()
    captchaKey.value = res.codeKey
    captchaBase64.value = res.image
}
onMounted(() => {
    getCaptchaImg()
})
// 加载
const isLoading = ref(false)
const waitOut = () => {
    toast.info('请等待后续开发！')
}
</script>
<template>
   <el-form
        class="form-content pwd-login"
        :model="formData"
        :rules="formRules"
        v-loading="isLoading"
        ref="formRef"
    >
        <el-form-item
            prop="qoNum"
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
                clearable
                v-model="formData.qoNum"
                placeholder="账号"
                :prefix-icon="User"
            >
                <!-- <template #prepend>qo_</template> -->
            </el-input>
        </el-form-item>
        <el-form-item
            prop="password"
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
            <el-input
                clearable
                type="password"
                show-password
                v-model="formData.password"
                placeholder="密码"
                :prefix-icon="Lock"
            />
        </el-form-item>
        <el-form-item
            prop="code"
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
            <!-- <el-input
                clearable
                v-model="formData.code"
                placeholder="验证码"
                size="large"
                :prefix-icon="Connection"
            >
                <template #append>
                    <img :src="captchaBase64" @click="getCaptchaImg" class="captcha-img" alt="加载出错">
                </template>
            </el-input> -->
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
            <div class="remember-forget">
                <el-link type="primary" :underline="false" @click="emit('toRegister')">没有账号？立即注册</el-link>
                <el-link type="primary" :underline="false" @click="emit('toForget')">忘记密码?</el-link>
            </div>
            <el-button
                class="login-btn"
                type="primary"
                @click="validForm(formRef, login)"
            >
                登录
            </el-button>
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
                    delay: 160
                },
            }">
            <!-- <div class="flex justify-between w-full h-[20px]">
                <el-button class="flex-1" @click="emit('toPhone')">手机登录</el-button>
                <el-button class="flex-1" @click="emit('toCode')">二维码登录</el-button>
                <el-button class="flex-1" @click="emit('toEmail')">邮箱登录</el-button>
            </div> -->
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
                    delay: 200
                },
            }">
            <!-- <el-divider>
                <span class="text-[#6b7280]">第三方登录</span>    
            </el-divider>
            <div class="more-login">
                <Icon @click="waitOut" icon="ic:baseline-wechat" width="22" height="22" />
                <Icon @click="waitOut" icon="ri:alipay-fill" width="22" height="22" />
                <Icon @click="waitOut" icon="mingcute:qq-fill" width="22" height="22" />
                <Icon @click="waitOut" icon="ri:weibo-fill" width="22" height="22" />
            </div> -->
        </el-form-item>
    </el-form>
</template>
<style scoped lang='scss'>
.form-content {
    .captcha-img {
        @apply
        w-[120px]
        h-full
        leading-none;
        border: 1px solid var(--el-input-border-color);
        border-left: none
    }
    .remember-forget {
        @apply
        flex
        justify-between
        w-full;
    }
    .login-btn {
        @apply
        w-full
        mt-[10px];
    }
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

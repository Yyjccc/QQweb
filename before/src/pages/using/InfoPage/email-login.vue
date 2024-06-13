<script setup lang='ts'>
// Message取个别名，和不知名的地方重复了
import { EmailLoginForm } from '@/constant/types';
import { Message as ElMessage, Connection } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'
import { 
    emailCode,
    email,
    validForm,
} from '@/constant/element-valid'
import { FormRules, FormInstance } from 'element-plus'
import { EmailLogin, SendEmail } from '@/axios/base';
import { useToast, POSITION } from 'vue-toastification';
import { setToken } from '@/utils/handler-token';
import { useRouter } from 'vue-router';

const emit = defineEmits(['back'])
const toast = useToast()
const router = useRouter()

// 表单数据
const formData = reactive<EmailLoginForm>({
    email: '',
    emailCode: ''
})
// 表单对象
const formRef = ref<FormInstance | undefined>()
// 获取验证码按钮加载状态
const emailCodeLoading = ref(false)
// 获取验证码
const getEmailCode = () => {
  // 校验邮箱字段
  formRef.value!.validateField('email', async valid => {
        if (!valid) return
        emailCodeLoading.value = true
        try {
            await SendEmail(formData.email)
            setTimeout(() => {
                toast.success('邮箱验证码发送成功！请注意查收或查看垃圾邮件！')
                emailCodeLoading.value = false
            }, 1500)
        } catch (err) {
            emailCodeLoading.value = false
        }
    })
}

// 表单验证
const formRules: FormRules = {
    email,
    emailCode
}
const isLoading = ref(false)
// 登录
const emailLogin = async () => {
    try {
        isLoading.value = true
        const { token } = await EmailLogin(formData)
        toast.success('欢迎您！', {
            position: POSITION.TOP_RIGHT
        })
        setToken(token)
        isLoading.value = false
        router.push('/using')
    } catch (e) {
        isLoading.value = false
    }
}
</script>

<template>
   <el-form
        class="form-content"
        :rules="formRules"
        :model="formData"
        ref="formRef"
        v-loading="isLoading"
    >
        <el-form-item
            prop="email"
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
                v-model="formData.email"
                placeholder="邮箱号码"
                :prefix-icon="ElMessage"
            />
        </el-form-item>
        <el-form-item
            prop="emailCode"
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
                    v-model="formData.emailCode"
                    placeholder="邮箱验证码"
                    :prefix-icon="Connection"
                />
                <el-button class="ml-[10px]" v-loading="emailCodeLoading" @click="getEmailCode">获取验证码</el-button>
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
            <el-button
                class="w-full" 
                type="primary"
                @click="validForm(formRef, emailLogin)">登录</el-button>
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

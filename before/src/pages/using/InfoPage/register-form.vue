<script setup lang='ts'>
import { RegisterForm } from '@/constant/types';
import { 
    User,
    Lock,
    Message as ElMessage,
    Connection
} from '@element-plus/icons-vue'
import { FormRules, FormInstance } from 'element-plus'
import { reactive } from 'vue';
import { 
    qoNum,
    password,
    emailCode,
    email,
    validForm,
    isAgree
} from '@/constant/element-valid'
import { ref } from 'vue'
import { SendEmail, Register } from '@/axios/base';
import { useToast } from 'vue-toastification';


const emit = defineEmits<{
    (name: 'back'): void
}>()

const toast = useToast()

// 注册表单
const formData = reactive<RegisterForm>({
    qoNum: '',
    password: '',
    againPwd: '',
    email: '',
    emailCode: '',
    isAgree: false
})

// 再次密码校验
const againPwdValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (value !== formData.password) callback(new Error('两次输入的密码不一致！'))
    callback()
}

// 表单验证
const formRules: FormRules = {
   // qoNum,
    //password,
    // email,
    // emailCode,
    againPwd: [
        {
            required: true,
            message: '请再次输入密码！',
            trigger: 'change'
        },
        {
            validator: againPwdValidator,
            trigger: 'blur'
        }
    ],
    isAgree
}
// 表单对象
const formRef = ref<FormInstance | undefined>()
// 加载状态
const isLoading = ref(false)
// 注册
const register = async () => {
    const {
        qoNum,
        password,
        email,
        emailCode
    } = formData

    try {
        isLoading.value = true
        await Register({
            qoNum,
            password,
            email,
            emailCode
        })
        isLoading.value = false
        toast.success('账号注册成功，请前往登录，欢迎您！')
        emit('back')
    } catch (err) {
        isLoading.value = false
    }
}

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
</script>
<template>
    <el-form
        :model="formData"
        :rules="formRules"
        size="large"
        ref="formRef"
    >
        <el-form-item
            prop="qoNum"
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
<!--                <template #prepend>qo_</template>-->
            </el-input>
        </el-form-item>
<!--        <el-form-item-->
<!--            prop="email"-->
<!--            v-motion-->
<!--            :initial="{-->
<!--                opacity: 0,-->
<!--                y: 150,-->
<!--            }"-->
<!--            :enter="{-->
<!--                opacity: 1,-->
<!--                y: 0,-->
<!--                transition: {-->
<!--                    type: 'spring',-->
<!--                    mass: 0.6,-->
<!--                    delay: 40-->
<!--                },-->
<!--            }">-->
<!--            <el-input-->
<!--                v-model="formData.email"-->
<!--                placeholder="邮箱"-->
<!--                :prefix-icon="ElMessage"-->
<!--            />-->
<!--        </el-form-item>-->
<!--        <el-form-item-->
<!--            prop="emailCode"-->
<!--            v-motion-->
<!--            :initial="{-->
<!--                opacity: 0,-->
<!--                y: 150,-->
<!--            }"-->
<!--            :enter="{-->
<!--                opacity: 1,-->
<!--                y: 0,-->
<!--                transition: {-->
<!--                    type: 'spring',-->
<!--                    mass: 0.6,-->
<!--                    delay: 80-->
<!--                },-->
<!--            }">-->
<!--            <div class="flex w-full h-full">-->
<!--                <el-input-->
<!--                    v-model="formData.emailCode"-->
<!--                    placeholder="邮箱验证码"-->
<!--                    :prefix-icon="Connection"-->
<!--                />-->
<!--                <el-button class="ml-[10px]" v-loading="emailCodeLoading" @click="getEmailCode">获取验证码</el-button>-->
<!--            </div>-->
<!--        </el-form-item>-->
        <el-form-item
            prop="password"
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
            <el-input
                clearable
                v-model="formData.password"
                type="password"
                show-password
                placeholder="密码"
                :prefix-icon="Lock"
            />
        </el-form-item>
        <el-form-item
            prop="againPwd"
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
            <el-input
                clearable
                v-model="formData.againPwd"
                type="password"
                show-password
                placeholder="确认密码"
                :prefix-icon="Lock"
            />
        </el-form-item>
        <el-form-item
            prop="isAgree"
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
            <el-checkbox v-model="formData.isAgree">
                <template #default>
                    <span>我已仔细阅读并接受</span>
                    <el-link @click.prevent type="primary" :underline="false" class="-mt-[2px]">《隐私政策》</el-link>
                </template>
            </el-checkbox>
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
                    delay: 240
                },
            }">
            <el-button
                class="w-full"
                size="default"
                type="primary"
                @click="validForm(formRef, register)"
            >
                确定
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
                    delay: 260
                },
            }">
            <el-button
                class="w-full"
                size="default"
                @click="emit('back')">
                返回
            </el-button>
        </el-form-item>
    </el-form>
</template>
<style scoped lang='scss'>
</style>

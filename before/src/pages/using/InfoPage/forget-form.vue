<script setup lang='ts'>
import { ForgetForm } from '@/constant/types'
import { 
    Lock,
    Message as ElMessage,
    Connection
} from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'
import { 
    emailCode,
    email,
    password,
    validForm,
} from '@/constant/element-valid'
import { FormRules, FormInstance } from 'element-plus'
import { ForgetPwd, SendEmail } from '@/axios/base'

const emit = defineEmits<{
    (name: 'back'): void
}>()

const toast = useToast()

// 表单数据
const formData = reactive<ForgetForm>({
    email: '',
    emailCode: '',
    password: '',
    againPwd: ''
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

// 再次密码校验
const againPwdValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (value !== formData.password) callback(new Error('两次输入的密码不一致！'))
    callback()
}

// 表单验证
const formRules: FormRules = {
    email,
    emailCode,
    password,
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
}
const isLoading = ref(false)

const forget = () => {
    const {
        email,
        emailCode,
        password
    } = formData
    ForgetPwd({
        email,
        emailCode,
        password
    }).then(() => {
        toast('密码重置成功，请牢记！')
        emit('back')
    }).finally(() => {
        isLoading.value = false
    })
}
</script>
<template>
    <el-form
        :model="formData"
        :rules="formRules"
        size="large"
        ref="formRef"
        v-loading="isLoading"
    >
        <el-form-item
            prop="email"
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
            <el-input
                v-model="formData.email"
                placeholder="邮箱号码"
                :prefix-icon="ElMessage"
            />
        </el-form-item>
        <el-form-item
            prop="emailCode"
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
                <el-button class="ml-[10px]" @click="getEmailCode" v-loading="emailCodeLoading">获取验证码</el-button>
            </div>
        </el-form-item>
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
                    delay: 80
                },
            }">
            <el-input
                v-model="formData.password"
                clearable
                type="password"
                show-password
                placeholder="新密码"
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
                    delay: 120
                },
            }"
        >
            <el-input
                v-model="formData.againPwd"
                clearable
                type="password"
                show-password
                placeholder="确认密码"
                :prefix-icon="Lock"
            />
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
            }"
        >
            <el-button
                class="w-full"
                size="default"
                type="primary"
                @click="validForm(formRef, forget)"
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
                    delay: 200
                },
            }"
        >
            <el-button
                class="w-full"
                size="default"
                @click="emit('back')"
            >
                返回
            </el-button>
        </el-form-item>
    </el-form>
</template>
<style scoped lang='scss'>
</style>

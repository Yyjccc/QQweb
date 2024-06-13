<script setup lang='ts'>
import { onMounted, ref } from 'vue'
import loginTitleBg from '@/assets/img/login-title-bg.png'
import loginTitlePrev from '@/assets/img/login-title-prev.png'
import PwdLoginForm from './pwd-login-form.vue';
import PhoneLoginForm from './phone-login-form.vue';
import EmailLogin from './email-login.vue';
import CodeLogin from './code-login.vue';
import RegisterForm from './register-form.vue';
import ForgetForm from './forget-form.vue';
import { getToken, clearToken } from '@/utils/handler-token' 
import { useToast } from 'vue-toastification'
import ToastNext from './toast-next.vue'
import { useRoute } from 'vue-router'

const toast = useToast()
const route = useRoute()
// 打字机效果表单标题
const formTitleText = ref('')
onMounted(() => {
    formTitleText.value = 'LOGIN'
    hasTokenHandler()
})

// 当前表单状态
type FormState = 
    'pwdLogin' | 
    'phoneLogin' | 
    'codeLogin' | 
    'register' |
    'forget' |
    'email'

const formState = ref<FormState>('pwdLogin')
// 切换表单状态
const setFormState = (state: FormState) => {
    formState.value = state
}
// 去注册
const toRegister = () => {
    const state: FormState = 'register'
    setFormState(state)
    formTitleText.value = state.toUpperCase()
}
// 注册返回登录
const registerBack = () => {
    const state: FormState = 'pwdLogin'
    setFormState(state)
    formTitleText.value = 'LOGIN'
}
// 处理当前存在token
const hasTokenHandler = () => {
    // 存在token直接进入
    if (getToken()) {
        toast(ToastNext, {
            timeout: 3000,
            onClose () {
                // 弹窗结束，未跳转就清空上一个账号
                if (route.path === '/info') clearToken()
            }
        })
    }
}
</script>
<template>
   <div class="login-box login-bg">
        <!-- 左边图片区 -->
        <section class="login-icon">
            <img :src="loginTitleBg" class="w-[240px]" alt="">
        </section>
        <!-- 表单区域 -->
        <section class="form-container">
            <!-- 固定不变头部 -->
            <div class="form-head">
                <img :src="loginTitlePrev" class="form-title-icon" alt="加载失败">
                <p class="form-title-text" v-wait.700="formTitleText" />
            </div>
                <PwdLoginForm
                    v-if="formState==='pwdLogin'"
                    @to-phone="setFormState('phoneLogin')"
                    @to-code="setFormState('codeLogin')"
                    @to-register="toRegister"
                    @to-forget="setFormState('forget')"
                    @to-email="setFormState('email')"
                />
<!--                 <PhoneLoginForm v-if="formState==='phoneLogin'" @back="setFormState('pwdLogin')"/>-->
                <CodeLogin v-if="formState==='codeLogin'" @back="setFormState('pwdLogin')"/>
                <RegisterForm v-if="formState==='register'" @back="registerBack"/>
                <ForgetForm v-if="formState==='forget'" @back="setFormState('pwdLogin')"/>
                <EmailLogin v-if="formState==='email'" @back="setFormState('pwdLogin')"/>
        </section>
   </div>
</template>
<style>
.pwd-login .el-input-group__append {
    padding: 0;
}
</style>
<style scoped lang='scss'>
.login-box {
    @apply
    h-full
    flex
    px-[32px]
    gap-[200px]
    xl:gap-[300px]
    items-center
    overflow-hidden;

    .login-icon {
        @apply
        flex-1
        justify-center
        hidden
        lg:flex;

        transform: rotateY(180deg)
    }

    .form-container {
        @apply
        flex-1
        flex
        flex-col
        items-center
        lg:items-start;

        > * {
            @apply w-[290px] md:w-[360px];
        }

        .form-head {
            @apply
            relative;

            .form-title-icon {
                @apply
                w-[150px]
                mx-auto
                absolute
                -top-[75px]
                left-[50px]
                -z-[1];
            }
            .form-title-text {
                @apply
                text-[32px]
                font-bold
                text-center
                text-[#999]
                my-[25px]
                overflow-hidden
                mx-auto
                tracking-[3px];

                &::after {
                    content: '.';
                    opacity: 0;
                    pointer-events: none;
                }
            }
        }
    }
}

.login-bg {
    @apply
    bg-[url('@/assets/img/login-bg.png')]
    bg-no-repeat;
    background-size: 0;

    @media (min-width: 1024px) {
        background-size: 100% 100%;
    }
}
</style>

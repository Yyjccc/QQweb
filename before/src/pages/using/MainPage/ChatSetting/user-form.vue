<script setup lang='ts'>
import { UpdateSelfReq, User } from '@/constant/types'
import { AxiosResponse } from 'axios'
import { FormInstance, FormRules, UploadProps } from 'element-plus'
import { reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'
import { Plus } from '@element-plus/icons-vue'
import { clearToken, getToken } from '@/utils/handler-token'
import { 
    validForm,
    phone,
    sex,
    avatar,
    updatePassword,
    nickname
} from '@/constant/element-valid'
import { UpdateSelf } from '@/axios/user'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/pinia/user'


const toast = useToast()
const router = useRouter()
const userStore = useUserStore()
const logout = () => {
    toast.success('修改个人信息成功，请重新登录！')
    userStore.user = {} as User
    clearToken()
    router.push({
        path: '/info',
        replace: true
    })
}
const onSubmit = () => {
    UpdateSelf(formData).then(() => {
        logout()
    })
}
// 表单数据
const formData = reactive<UpdateSelfReq>({
    avatar: userStore.user.avatar!,
    password: '',
    nickname: userStore.user.nickname!,
    phone: userStore.user.phone ?? '',
    sex: userStore.user.sex ?? '0',
    description: userStore.user.description ?? ''
})

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile: File) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        toast.error('图片格式必须为jpg或png！')
        return false
    } else if (rawFile.size / 1024 / 1024 > 10) {
        toast.error('图片大小不能超过2MB！')
        return false
    }
    return true
}
const handleAvatarSuccess: UploadProps['onSuccess'] = ({ data }: AxiosResponse) => {
    // 将图片从绝对路径处理为相对路径
    const path = '../../../../' + data.url.split('\\src\\')[1]
    formData.avatar = new URL(path, import.meta.url).href
}

// 表单对象
const formRef = ref<FormInstance | undefined>()
// 表单验证
const formRules: FormRules = {
    phone,
    sex,
    avatar,
    password: updatePassword,
    nickname
}
const isLoading = ref(false)
</script>

<template>
   <el-form
        class="form-content"
        :model="formData"
        :rules="formRules"
        ref="formRef"
        v-loading="isLoading"
        label-width="70"
    >
        <el-form-item
            label="头像"
            prop="avatar"
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
            <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                action="/api/user/upload"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :headers="{
                    token: getToken() || ''
                }"
            >
                <el-avatar :size="100" shape="square" v-if="formData.avatar" :src="formData.avatar"/>
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus/>
                </el-icon>
            </el-upload>
        </el-form-item>
        <el-form-item
            label="昵称"
            prop="nickname"
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
                    delay: 80
                },
            }">
            <el-input
                v-model="formData.nickname"
                maxlength="10"
                show-word-limit
                type="text"
                placeholder="请输入"
            />
        </el-form-item>
        <el-form-item
            label="密码"
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
                    delay: 120
                },
            }">
            <el-input
                clearable
                type="password"
                show-password
                v-model="formData.password"
                placeholder="密码（默认不修改）"
            />
        </el-form-item>
        <el-form-item
            label="手机号"
            prop="phone"
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
                    delay: 160
                },
            }">
            <el-input
                v-model="formData.phone"
                placeholder="Please input"
            />
        </el-form-item>
        <el-form-item
            label="性别"
            prop="sex"
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
                    delay: 200
                },
            }">
            <el-radio-group v-model="formData.sex">
                <el-radio label="0" size="large">男</el-radio>
                <el-radio label="1" size="large">女</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item
            label="个性签名"
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
                    delay: 240
                },
            }">
            <el-input
                v-model="formData.description"
                maxlength="30"
                placeholder="Please input"
                show-word-limit
                type="textarea"
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
                    delay: 280
                },
            }">
            <el-button class="w-full" type="primary" @click="validForm(formRef, onSubmit)">修改</el-button>
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

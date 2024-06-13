<script setup lang='ts'>
import { AddGroupReq } from '@/constant/types'
import { AxiosResponse } from 'axios'
import { FormInstance, FormRules, UploadProps } from 'element-plus'
import { reactive, ref } from 'vue'
import { useToast } from 'vue-toastification'
import { Plus } from '@element-plus/icons-vue'
import { getToken } from '@/utils/handler-token'
import { 
    validForm,
    groupAvatar,
    groupName
} from '@/constant/element-valid'
import { AddGroup } from '@/axios/user'


const props = defineProps<{
    closeAddGroup: Function
}>()


const toast = useToast()
const onSubmit = () => {
    AddGroup(formData).then(() => {
        toast.success('新建群组成功，快来邀请好友吧！')
        props.closeAddGroup()
    })
}
// 表单数据
const formData = reactive<AddGroupReq>({
    groupAvatar: '',
    groupName: '',
    groupRemark: ''
})
const uploadLoading = ref(false)
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile: File) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        toast.error('图片格式必须为jpg或png！')
        return false
    } else if (rawFile.size / 1024 / 1024 > 2) {
        toast.error('图片大小不能超过2MB！')
        return false
    }
    uploadLoading.value = true
    return true
}
const handleAvatarSuccess: UploadProps['onSuccess'] = ({ data }: AxiosResponse) => {
    // 将图片从绝对路径处理为相对路径
    const path = '../../../../' + data.url.split('\\src\\')[1]
    formData.groupAvatar = new URL(path, import.meta.url).href
    uploadLoading.value = false
}

// 表单对象
const formRef = ref<FormInstance | undefined>()
// 表单验证
const formRules: FormRules = {
    groupName,
    groupAvatar
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
    >
        <el-form-item
            label="群组头像"
            prop="groupAvatar"
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
                v-loading="uploadLoading"
                :headers="{
                    token: getToken() || ''
                }"
            >
                <el-avatar :size="100" shape="square" v-if="formData.groupAvatar" :src="formData.groupAvatar"/>
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus/>
                </el-icon>
            </el-upload>
        </el-form-item>
        <el-form-item
            label="群组名称"
            prop="groupName"
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
                v-model="formData.groupName"
                maxlength="8"
                show-word-limit
                type="text"
                placeholder="请输入"
            />
        </el-form-item>
        <el-form-item
            label="群组公告"
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
                v-model="formData.groupRemark"
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
                    delay: 80
                },
            }">
            <el-button class="w-full" type="primary" @click="validForm(formRef, onSubmit)">创建</el-button>
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

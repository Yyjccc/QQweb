import type { FormInstance, FormItemRule } from 'element-plus'
import { Arrayable } from 'element-plus/es/utils'

type BaseRule = Arrayable<FormItemRule>
// qo号预校验
const qoValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (!/^[a-zA-Z0-9_]{6,12}$/.test(value)) callback(new Error('账号由6-12位数字字母下划线组成！'))
    callback()
}
// 密码预检验
const passwordValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (!/^[a-zA-Z0-9]{6,18}$/.test(value)) callback(new Error('密码由6-18位数字字母组成！'))
    callback()
}
// 邮箱预检验
const emailValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (!/^[A-Za-z\d]+([-_\.][A-Za-z\d]+)*@([A-Za-z\d]+[-\.])+[A-Za-z\d]{2,4}(,[A-Za-z\d]+([-_\.][A-Za-z\d]+)*@([A-Za-z\d]+[-\.])+[A-Za-z\d]{2,4})*$/.test(value)) callback(new Error('请输入正确的邮箱！'))
    callback()
}
// 检查协议
const agreeValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback(new Error('请先同意隐私政策！'))
    callback()
}
// 手机号
const phoneValidator = (rule: any, value: string, callback: any) => {
    if (!value) callback()
    if (!/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/.test(value)) callback(new Error('请输入正确的手机号！'))
    callback()
}

const qoNum: BaseRule = [
    {
        required: true,
        message: '账号不能为空！',
        trigger: 'change'
    },
    {
        validator: qoValidator,
        trigger: 'blur'
    }
]
const password: BaseRule = [
    {
        required: true,
        message: '密码不能为空！',
        trigger: 'change'
    },
    {
        validator: passwordValidator,
        trigger: 'blur'
    }
]
const updatePassword: BaseRule = [
    {
        validator: passwordValidator,
        trigger: 'blur'
    }
]
const code: BaseRule = [
    {
        required: true,
        message: '验证码不能为空！',
        trigger: 'change'
    },
    {
        min: 4,
        max: 4,
        message: '请输入正确的验证码！',
        trigger: 'blur'
    },
]
const emailCode: BaseRule = [
    {
        required: true,
        message: '验证码不能为空！',
        trigger: 'change'
    },
    {
        min: 6,
        max: 6,
        message: '请输入正确的验证码！',
        trigger: 'blur'
    },
]

const email: BaseRule = [
    {
        required: true,
        message: '邮箱不可为空！',
        trigger: 'blur'
    },
    {
        validator: emailValidator,
        trigger: 'blur'
    }
]
const isAgree: BaseRule = [
    {
        validator: agreeValidator,
        trigger: 'change'
    }
]
const phone: BaseRule = [
    {
        validator: phoneValidator,
        trigger: 'change'
    }
]
const sex: BaseRule = [
    {
        required: true,
        message: '性别不能为空！',
        trigger: 'change'
    },
]
const groupName: BaseRule = [
    {
        required: true,
        message: '群组名称不能为空！',
        trigger: 'change'
    }
]
const groupAvatar: BaseRule = [
    {
        required: true,
        message: '群组头像不能为空！',
        trigger: 'change'
    }
]
const nickname: BaseRule = [
    {
        required: true,
        message: '昵称不能为空！',
        trigger: 'change'
    }
]

const avatar = groupAvatar

// 校验表单
const validForm = (formRef: FormInstance | undefined, cb: () => void) => {
    if (!formRef) return
    formRef.validate(valid => {
        if (!valid) return
        cb()
    })
}

export {
    qoNum,
    password,
    code,
    validForm,
    emailCode,
    email,
    isAgree,
    groupName,
    groupAvatar,
    phone,
    sex,
    avatar,
    nickname,
    updatePassword,
}
import { 
    BaseForm,
    GetCaptchaImgResp,
    LoginResp,
    RegisterReq,
    EmailLoginForm,
    ForgetReq
} from '@/constant/types'
import { BaseAPI } from './filter'

// 登录
const Login = (data: BaseForm) => BaseAPI<any, LoginResp>({
    url: '/login',
    method: 'post',
    data
})

// 获取验证图片
const GetCaptchaImg = () => BaseAPI<any, GetCaptchaImgResp>({
    url: '/captcha',
    method: 'get'
})

// 发送邮箱验证码
const SendEmail = (email: string) => BaseAPI({
    url: '/email',
    method: 'get',
    params: {
        email
    }
})

// 注册账号
const Register = (data: RegisterReq) => BaseAPI({
    url: '/register',
    method: 'post',
    data
})

// 邮箱登录
const EmailLogin = (data: EmailLoginForm) => BaseAPI<any, LoginResp>({
    url: '/email',
    method: 'post',
    data
})

// 重置密码
const ForgetPwd = (data: ForgetReq) => BaseAPI({
    url: '/forget',
    method: 'post',
    data
})

export {
    Login,
    GetCaptchaImg,
    SendEmail,
    Register,
    EmailLogin,
    ForgetPwd
}
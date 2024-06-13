import { type } from "os";

// 登录基础表单
interface BaseForm {
    qoNum: string;
    password: string;
    code: string;
    codeKey: string;
}

// 获取验证码响应体
interface GetCaptchaImgResp {
    codeKey: string,
    image: string
}

// 登录请求响应体
interface LoginResp {
    token: string;
}

type LogicSelect = '0' | '1'
type recordType = 'text' | 'image'

// 基础用户
interface User {
    qoNum: string;
    avatar: string | undefined;
    password: string;
    nickname: string;
    phone: string | undefined;
    email: string | undefined;
    sex: LogicSelect | undefined;
    description: string | undefined;
    createTime: string;
    updateTime: string;
    delLogic: LogicSelect;
}
// 消息
interface Record {
    recordId: number;
    recordUser: string;
    recordContent: string;
    recordType: recordType;
    createTime: string;
}

// 注册接口请求体
interface RegisterReq {
    qoNum: string;
    password: string;
    email: string;
    emailCode: string;
}

// 注册表单
interface RegisterForm extends RegisterReq {
    againPwd: string;
    isAgree: boolean;
}

// 邮箱登录表单
interface EmailLoginForm {
    email: string;
    emailCode: string;
}

// 重置密码请求体
interface ForgetReq {
    password: string;
    email: string;
    emailCode: string;
}

// 重置密码表单
interface ForgetForm extends ForgetReq {
    againPwd: string;
}

// 消息列表信息
interface MessageList {
    user: User,
    record: Record
}

// 分页数据
type PageType<T> = {
    records: T;
    total: number;
    size: number;
    current: number;
}

// 聊天记录
interface ChatData extends Record{
    user: User;
}

// 发送接口
interface SendReq {
    toUser: string;
    recordContent: string;
    recordType: string;
}

// 群组
interface Group {
    groupName: string;
    groupNum: number;
    groupRemark: string;
    groupAvatar: string | undefined;
    groupUser: string;
    createTime: string;
}

// 消息列表中群组
interface GetGroupMsgResp extends Group {
    record: Record
}

interface SendToGroupReq {
    toGroup: number;
    recordContent: string;
    recordType: recordType;
}

interface Gu {
    createTime: string;
    groupAgree: LogicSelect;
    groupNum: number;
    updateTime: string;
    user: string;
    userAgree: LogicSelect
}

// 新增群组表单
interface AddGroupReq {
    groupRemark: string;
    groupAvatar: string;
    groupName: string;
}

// 修改用户信息
interface UpdateSelfReq {
    avatar: string;
    password: string;
    nickname: string;
    phone: string;
    sex: LogicSelect;
    description: string;
}


export type {
    BaseForm,
    GetCaptchaImgResp,
    LoginResp,
    RegisterReq,
    RegisterForm,
    EmailLoginForm,
    ForgetReq,
    ForgetForm,
    User,
    Record,
    MessageList,
    PageType,
    ChatData,
    SendReq,
    Group,
    GetGroupMsgResp,
    SendToGroupReq,
    Gu,
    AddGroupReq,
    UpdateSelfReq
}
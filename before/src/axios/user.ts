import { AddGroupReq, ChatData, GetGroupMsgResp, Group, Gu,
    MessageList, PageType, SendReq, SendToGroupReq, UpdateSelfReq, User } from "@/constant/types"
import { UserAPI } from "./filter"
import websocketService from "@/websocket";

const  basePath:string="/user"

interface Request{
    method:string
    path:string
    param?:any
    body?:any
}

interface Response{
    path:string
    status:number
    response?:any
    id:string
}

// 获取自己的信息
const GetInfo = () => UserAPI<any, User>({
    url: '/info',
    method: 'get'
})

// 获取消息列表
const GetMessage = async (nickname: string):Promise<MessageList[]> =>  {
    const request:Request={
        path: basePath+"/message",
        method:"get",
        param:{
            nickname:nickname
        }
    }
    const response:Response = await websocketService.sendRequest(request);
    const data= response.response;
    console.log('Received response:', response.response);
    return data
}
// 获取聊天记录（指定页数）
const GetChatData = (current: number, qoNum: string) => UserAPI<any, ChatData[]>({
    url: '/record',
    method: 'get',
    params: {
        current,
        qoNum
    }
})

// 获取聊天记录（直到页数，包括）

const GetChatDataAll = async (current: number, qoNum: string):Promise<ChatData[]> =>{
    const request:Request={
        path: basePath+"/record/all",
        method:"get",
        param:{
            current:current,
            qoNum:qoNum
        }
    }
    const response:Response = await websocketService.sendRequest(request);
    const data= response.response;
    return data
}


const Send =async (data: SendReq):Promise<void> => {
    const request:Request={
        path: basePath+"/send",
        method:"post",
        body:data
    }
    await websocketService.sendRequest(request);
}
// 获取好友列表
const GetUserList = () => UserAPI<any, User[]>({
    url: '/friend',
    method: 'get'
})

// 退出登录
const Logout = () => UserAPI({
    url: '/logout',
    method: 'delete'
})


// 获取消息列表中的群组
const GetGroupMsg = () => UserAPI<any, GetGroupMsgResp[]>({
    url: '/group/msg',
    method: 'get'
})


const GetGroupAllRecord = async (current: number, groupNum: number) : Promise<ChatData[]> => {
    const request:Request={
        path: basePath+"/group/record/all",
        method:"get",
        param:{
            current:current,
            groupNum:groupNum
        }
    }
    const response:Response = await websocketService.sendRequest(request);
    return  response.response;
}

// 获取群组中聊天数据（页数）
const GetGroupRecord = (current: number, groupNum: number) => UserAPI<any, ChatData[]>({
    url: '/group/record',
    method: 'get',
    params: {
        current,
        groupNum
    }
})


const SendToGroup = async (data: SendToGroupReq):Promise<void> => {
    const request:Request={
        path: basePath+"/group/send",
        method:"post",
        body:data
    }
    await websocketService.sendRequest(request);
}

// 获取群组列表
const GetGroupList = () => UserAPI<any, Group[]>({
    url: '/group/list',
    method: 'get'
})

// 获取群组用户列表
const GetUserByGroup = (groupNum: number) => UserAPI<any, User[]>({
    url: '/group/user',
    method: 'get',
    params: { groupNum }
})

// 退出群聊
const OutGroup = (groupNum: number) => UserAPI({
    url: '/group/user/' + groupNum,
    method: 'delete'
})

// 删除好友
const DelUser = (qoNum: string) => UserAPI({
    url: '/del/' + qoNum,
    method: 'delete'
})


const GetWaitUser = async (): Promise<User[]> => {
    const request:Request={
        path: basePath+"/wait/user",
        method:"get",
    }
    const response:Response = await websocketService.sendRequest(request);
    return  response.response;
}

const GetWaitGroup = async ():Promise<Group[]> => {
    const request:Request={
        path: basePath+"/wait/group",
        method:"get",
    }
    const response:Response = await websocketService.sendRequest(request);
    return  response.response;
}


// 同意好友
const AgreeUser = (qoNum: string, status: '0' | '1') => UserAPI({
    url: `/agree/user/${qoNum}/${status}`,
    method: 'put'
})

// 同意群聊邀请
const AgreeGroup = (groupNum: number, status: '0' | '1') => UserAPI({
    url: `/agree/group/${groupNum}/${status}`,
    method: 'put'
})

// 获取不在群组中或未发送请求的用户
const GetAddGroupUser = (groupNum: number) => UserAPI<any, User[]>({
    url: '/add/group/user',
    method: 'get',
    params: { groupNum }
})

// 邀请人进入群组
const InviteUserToGroup = (qoNum: string, groupNum: number) => UserAPI({
    url: `/group/user/${qoNum}/${groupNum}`,
    method: 'post'
})

// 获取可添加用户
const GetAddUser = (nickname: string) => UserAPI<any, User[]>({
    url: '/add/user',
    method: 'get',
    params: { nickname }
})

// 添加好友
const AddUser = (qoNum: string) => UserAPI({
    url: '/to/' + qoNum,
    method: 'post'
})

// 新增群组
const AddGroup = (data: AddGroupReq) => UserAPI({
    url: '/group',
    method: 'post',
    data
})

// 修改个人信息
const UpdateSelf = (data: UpdateSelfReq) => UserAPI({
    url: '/update',
    method: 'post',
    data
})

export {
    GetInfo,
    GetMessage,
    GetChatData,
    GetChatDataAll,
    Send,
    GetUserList,
    Logout,
    GetGroupMsg,
    GetGroupAllRecord,
    GetGroupRecord,
    SendToGroup,
    GetGroupList,
    GetUserByGroup,
    OutGroup,
    DelUser,
    GetWaitUser,
    GetWaitGroup,
    AgreeUser,
    AgreeGroup,
    GetAddGroupUser,
    InviteUserToGroup,
    GetAddUser,
    AddUser,
    AddGroup,
    UpdateSelf
}
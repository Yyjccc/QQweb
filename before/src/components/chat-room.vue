<script lang="ts" setup>
import {
    nextTick,
    onMounted,
    onUnmounted,
    ref,
    watch,
    watchEffect
} from 'vue'
import moment, { handlerTime, isShowAbs } from '@/utils/moment'
import { nanoid } from 'nanoid'

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

// 聊天记录
interface ChatData extends Record{
    user: User;
}

interface Props {
    chatContent: ChatData[];
    themeMode: 'dark' | 'light';
    qoNum: string
}

const props = defineProps<Props>()
const emits = defineEmits<{
    (name: 'clickAvatar', content: ChatData): void,
    (name: 'loadMore'): void
}>()

// 滚动盒子
const chatBoxRef = ref()

watchEffect(() => {
    // 监听切换暗色/浅色，启动时检查一遍
    if (props.themeMode === 'dark' || window.matchMedia('(prefers-color-scheme: dark)').matches) {
        document.documentElement.classList.add('dark')
    } else {
        // 存在-删除
        document.documentElement.classList.contains('dark') &&
        document.documentElement.classList.remove('dark')
    }
})

// 创建一个hook函数用来创建防抖
const createDebounceFn = <T, >(delay: number, cb: (args: T) => void) => {
    let timeout: ReturnType<typeof setTimeout>
    return (data: T) => {
        timeout && clearTimeout(timeout)
        timeout = setTimeout(() => {
            cb(data)
        }, delay)
    }
}

// 是否显示顶部内阴影
const isShowTShadow = ref(false)
// 是否显示底部内阴影
const isShowBShadow = ref(false)

// 防抖处理滚动事件
const watchScroll = createDebounceFn<HTMLDivElement>(100, target => {
    // 拥有数据并且滚动到顶部 加载更多数据
    if (haveMore.value && target.scrollTop === 0) {
        loadMore()
    }
    // 出现阴影
    isShowTShadow.value = target.scrollTop > 20
    isShowBShadow.value = target.scrollHeight - (target.scrollTop + target.clientHeight) > 20
})

// 监听滚动
const onChatBoxScroll = (event: any) => {
    watchScroll(event.target)
}

// 监听消息，滚动一定距离
watch(() => props.chatContent, (before, after) => {
    // 防止轮询触发
    if ((JSON.stringify(before) === JSON.stringify(after)) && !isLoading.value) return
    // 滚动条总高度（新数据加载前）
    const oldScrollHeight = chatBoxRef.value.scrollHeight
    // 滚动条距离顶部（新数据加载前）
    const scrollTop = chatBoxRef.value.scrollTop
    // 显示区域高度
    const clientHeight = chatBoxRef.value.clientHeight
    // 滚动条距离底部（新数据加载前）
    const scrollBottom = chatBoxRef.value.scrollHeight - (chatBoxRef.value.scrollTop + chatBoxRef.value.clientHeight)
    nextTick(() => {
        // 滚动条总高度（新数据加载后）
        const newScrollHeight = chatBoxRef.value.scrollHeight
        // 是否为向上加载更多数据
        if (isLoading.value) {
            if (newScrollHeight === oldScrollHeight) haveMore.value = false
            chatBoxRef.value.scrollTo({
                top: newScrollHeight - oldScrollHeight
            })
            isLoading.value = false
        } else {
            // 判断是否是自己发送的消息（用户为自己）
            if ((props.chatContent.at(-1)?.user.qoNum === props.qoNum) || scrollBottom <= 10) {
                // 滚动到最底部
                toBottom(true)
            } else {
                const addHeight = newScrollHeight - (scrollTop + clientHeight)
                chatBoxRef.value.scrollBy({
                    top: scrollBottom < 1000 ? Math.pow(0.9, scrollBottom / 20) * addHeight : 0,
                    behavior: 'smooth'
                })
            }
        }
    })
}, { deep: true })

// 是否显示时间：两条记录相差大于两分钟或为第一条消息，才显示时间
const isShowTime = (nowItemTime: string, beforeItemTime: string) =>
    moment(nowItemTime).diff(moment(beforeItemTime), 'm') > 2 || !beforeItemTime

// 相对日期的动态key
const chatTimeKey = ref(nanoid())

let updateInterval: ReturnType<typeof setInterval>
// 每10秒刷新相对时间
const startUpdateInterval = () => {
    updateInterval = setInterval(() => {
        chatTimeKey.value = nanoid()
    }, 10000)
}

// 滚动到底部（滚动条总高度 - 显示区域高度）
const toBottom = (isSmooth: boolean) => {
    console.log('到底部')
    chatBoxRef.value.scrollBy({ 
        top: chatBoxRef.value.scrollHeight - chatBoxRef.value.clientHeight,
        behavior: isSmooth ? 'smooth': 'auto'
    })
}
// 暴露出滚动事件
defineExpose({
    toBottom
})

// 页面加载后，启动定时刷新和默认到底部
onMounted(() => {
    // 开始刷新时间
    startUpdateInterval()
    toBottom(false)
})

// 去除刷新定时器
onUnmounted(() => {
    clearInterval(updateInterval)
})

// 是否可以获取更多信息
const haveMore = ref(true)
// 加载状态
const isLoading = ref(false)
const loadMore = () => {
    isLoading.value = true
    // 通知加载
    emits('loadMore')
}
</script>

<template>
    <div class="chat-box-shadow" :class="{
        'bottom-shadow': isShowBShadow,
        'top-shadow': isShowTShadow
    }">
        <div class="chat-box" ref="chatBoxRef" @scroll="onChatBoxScroll">
            <div class="more" v-if="haveMore">
                <span v-if="isLoading">加载中...</span>
                <span v-else @click="loadMore">+ 更多消息</span>
            </div>
            <div class="no-more" v-else>
                没有更多消息
            </div>
            <div class="chat-row"
                 v-for="(chatItem, index) in props.chatContent"
                 v-motion
                 :initial="{
                    opacity: 0,
                    x: props.qoNum === chatItem.user.qoNum ? 100: -100
                }"
                 :enter="{
                    opacity: 1,
                    x: 0,
                    transition: {
                        duration: 100,
                        ease: 'linear'
                    }
                }"
                 :class="{
                    'self-msg': props.qoNum === chatItem.user.qoNum
                }"
                 :key="chatItem.recordId">
                <div class="chat-row-time" v-if="isShowTime(chatItem.createTime, props.chatContent[index - 1]?.createTime)">
                    <!--相对时间时动态刷新-->
                    <span :key="isShowAbs(chatItem.createTime) ? undefined: chatTimeKey">
                        {{ handlerTime(chatItem.createTime) }}
                    </span>
                </div>
                <div class="chat-row-head">
                    <img class="chat-row-avatar hover-avatar" :src="chatItem.user.avatar" alt="">
                    <span class="chat-row-nickname">{{ chatItem.user.nickname }}</span>
                </div>
                <div class="chat-row-content">
                    <img :src="chatItem.user.avatar"
                         class="chat-row-avatar hover-avatar"
                         @click="emits('clickAvatar', { ...chatItem })"
                         alt="">
                    <img v-if="chatItem.recordType==='image'" :src="chatItem.recordContent" class="max-w-[200px]" alt="">
                    <p v-else-if="chatItem.recordType==='text'" class="chat-row-text">{{ chatItem.recordContent }}</p>
                    <span class="chat-row-avatar"/>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
* {
    @apply
    transition-all
    duration-500;
}

.chat-box-shadow {
    @apply relative h-full;

    &::before, &::after {
        content: '';
        @apply absolute inset-0 z-50 pointer-events-none transition-all duration-200;
    }

    .chat-box {
        @apply
        p-[40px]
        overflow-y-auto
        overflow-x-hidden
        border
        bg-gray-100
        h-full
        dark:bg-[#1A1A1B];

        .more {
            @apply
            text-center
            text-[15px]
            cursor-pointer
            text-blue-500;
        }

        .no-more {
            @apply
            text-center
            text-[15px]
            text-gray-500;
        }

        .chat-row {
            // 取消聊天开始第一个日期的内边距
            &:first-of-type .chat-row-time {
                @apply pt-0;
            }

            // 下行距离上行间距
            + .chat-row {
                @apply
                mt-[25px];
            }

            // 时间显示
            .chat-row-time {
                @apply
                text-center
                text-[15px]
                pt-[20px]
                text-[#999]
                pb-[15px]
                tracking-wider;
            }

            // 可点击头像
            .hover-avatar {
                @apply cursor-pointer;

                &:hover {
                    @apply
                    scale-110;
                }
            }

            // 消息头部
            .chat-row-head {
                @apply
                mb-[8px]
                flex
                gap-[10px]
                items-center
                text-gray-500;

                // 头像
                .chat-row-avatar {
                    @apply
                    w-[45px]
                    h-[45px]
                    rounded-[50%];

                    // 非移动端隐藏
                    @apply
                    md:hidden;
                }

                .chat-row-nickname {
                    @apply
                    dark:text-[#ccc]
                    text-[15px]
                    md:text-[16px]
                    text-[#555];
                }
            }

            .chat-row-content {
                @apply flex gap-[10px] ml-[10px] md:ml-0;
                // 头像框
                .chat-row-avatar {
                    // 移动端隐藏
                    @apply
                    hidden;

                    // 非移动端设置正方形，改变头像大小
                    @apply
                    md:w-[50px]
                    md:h-[50px]
                    xl:w-[60px]
                    xl:h-[60px]
                    md:rounded-[2px]
                    md:block;

                    // 尾部头像隐藏
                    ~ .chat-row-avatar {
                        @apply invisible flex-shrink-0;
                    }
                }

                // 文字框
                .chat-row-text {
                    @apply
                    break-all
                    justify-start
                    text-justify
                    text-[#333]
                    text-[17px]
                    dark:text-[#d6d6d6]
                    bg-white
                    dark:bg-[#414141]
                    p-[13px]
                    leading-normal
                    rounded-md
                    tracking-wide
                    relative
                    my-[10px];

                    box-shadow: 10px 10px 20px -10px #b8b8b8;

                    // 调整文字框和头像的间距
                    @apply
                    md:mt-0
                    md:mx-[10px];

                    // 消息框三角形
                    &::before {
                        content: '';
                        @apply
                        absolute
                        w-0
                        h-0
                        left-[10px]
                        -top-[10px]
                        border-[10px]
                        border-solid
                        border-transparent
                        border-t-0
                        transition-all
                        duration-500
                        border-b-white
                        dark:border-b-[#414141];

                        // 大屏显示位置在左
                        @apply
                        md:-left-[10px]
                        md:top-[15px]
                        md:border-[10px]
                        md:border-transparent
                        dark:md:border-transparent
                        md:border-l-0
                        md:border-r-white
                        dark:md:border-r-[#414141];
                    }
                }
            }
        }

        // 滚动条样式
        &::-webkit-scrollbar {
            @apply w-[4px];
        }

        &::-webkit-scrollbar-thumb {
            @apply
            bg-[#ccc]
            rounded-2xl;
        }

        &::-webkit-scrollbar-track-piece {
            @apply bg-transparent;
        }

        // 自己发的
        .self-msg {
            .chat-row-head {
                // 使顶部靠右
                @apply flex-row-reverse;
            }

            .chat-row-content {
                // 使消息靠右
                @apply flex-row-reverse mr-[10px] ml-0 md:mr-0;

                .chat-row-text {
                    @apply
                    bg-[#95EC69]
                    dark:bg-[#1f5929];

                    box-shadow: -10px 10px 20px -10px rgba(69,146,73,0.74);

                    // 消息框三角
                    &::before {
                        content: '';
                        // 大位置在右边显示
                        @apply
                        left-auto
                        border-b-[#95EC69]
                        dark:border-b-[#1f5929]
                        right-[10px];
                        // 大屏下基于消息框右边
                        @apply
                        md:left-auto
                        md:-right-[10px]
                        md:border-r-[#95EC69]
                        md:border-transparent
                        dark:md:border-transparent
                        dark:md:border-r-[#1f5929]
                        md:rotate-[180deg];
                    }
                }
            }
        }
    }
}

.top-shadow::before {
    box-shadow: inset 0 20px 25px -25px #ccc;
}

.bottom-shadow::after {
    box-shadow: inset 0 -20px 25px -25px #ccc;
}

.dark .chat-row-text {
    box-shadow: -6px 6px 20px -10px #222 !important;
}
</style>

<script setup lang='ts'>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { Icon } from '@iconify/vue';
import type { Transition } from '@vueuse/motion'

// 背景位置偏移，最多100
const bgOffsetY = ref(0)
const updateBgOffsetY = ({ target: { scrollingElement } }: any) => {
    bgOffsetY.value = scrollingElement.scrollTop / 2
}
onMounted(() => {
    // 监听窗口滚动
    window.addEventListener('scroll', updateBgOffsetY)
})
onUnmounted(() => {
    // 离开首页注销
    window.removeEventListener('scroll', updateBgOffsetY)
})

// 盒子
const bannerBgRef = ref()
// 动态框偏移
const textBgOffset = reactive({
    x: 0,
    y: 0
})
// 文字偏移
const textOffset = reactive({
    x: 0,
    y: 0
})
// 节流
let moved = false
// 速率
const __x = 20
// 监听鼠标移入
const mousemove = (e: any) => {
    if (!moved) {
        moved = true
        setTimeout(() => {
            moved = false
        }, 20)
        // 基准值：对于中心点，左边为负数，右边为正数 -5 ~ 5
        const fiducialValueX = (e.pageX / bannerBgRef.value.clientWidth - 0.5) * 10
        const fiducialValueY = (e.pageY / bannerBgRef.value.clientHeight - 0.5) * 10
        // 反正切函数计算，随着值越大 y的变化幅度越少
        const atanX = Math.atan(fiducialValueX)
        const atanY = Math.atan(fiducialValueY)
        // 方框移动速度为文字的三倍
        textOffset.x = atanX * __x
        textOffset.y = atanY * __x
        textBgOffset.x = atanX * __x * 3
        textBgOffset.y = atanY * __x * 3
    }
}
// 点击箭头
const scrollTo = () => {
    window.scrollTo({
        top: bannerBgRef.value.clientHeight - 60,
        behavior: 'smooth'
    })
}

// 封装为组件
const props = defineProps<{
    // 大屏幕下背景图
    lgBg: string;
    // 小屏幕下背景图
    smBg: string;
    // 标题文字
    title: string;
    // 副标题文字
    subTitle: string;
}>()
</script>

<template>
    <!-- 首页图片模块 -->
    <section
        class="banner-bg"
        @mousemove="mousemove"
        :style="{
            '--lg-bg': `url('${props.lgBg}')`,
            '--sm-bg': `url('${props.smBg}')`
        }"
        ref=bannerBgRef>
        <!-- 背景 -->
        <div
            class="banner"
            :style="{
                transform: `translate3d(0px, ${bgOffsetY}px, 0px)`
            }"
        />
        <!-- 文字背后动态框 -->
        <Transition
            appear
            enter-active-class="fade-in-up">
            <div
                class="banner-text-bg super-absolute"
                :style="{
                    '--text-bg-x': textBgOffset.x + 'px',
                    '--text-bg-y': textBgOffset.y + 'px'
                }"
            />
        </Transition>
        <!-- 文字 -->
        <div
            class="banner-text super-absolute"
            :style="{
                '--text-x': textOffset.x + 'px',
                '--text-y': textOffset.y + 'px'
            }">
            <Transition
                appear
                enter-active-class="title-in-up">
                <p class="banner-text-title">
                    {{ props.title }}
                    <!-- 圆点 -->
                    <span class="round-icon animate__bounce"/>
                </p>
            </Transition>
            <Transition
                appear
                enter-active-class="subtitle-in-up">
                <p class="banner-text-subtitle">
                    {{ props.subTitle }}
                </p>
            </Transition>
        </div>
        <!-- 箭头 -->
        <div
            class="arrow-bottom"
            @click="scrollTo"
            v-motion
            :initial="{
                opacity: 0,
                y: 100,
            }"
            :enter="{
                opacity: 1,
                y: 0,
                transition: {
                    type: 'spring',
                    stiffness: '100',
                    delay: 80,
                } as Transition,
            }">
            <Icon
                icon="material-symbols:arrow-downward-rounded"
                width="16"
                height="16"
                :horizontalFlip="true"
            />
        </div>
    </section>
</template>

<style lang="scss" scoped>
// 总盒子
.banner-bg {
    @apply
    relative
    h-screen
    overflow-hidden;

    // 鼠标移动触发偏移
    &:hover {
        .banner-text-bg {
            transform: translate3d(calc(-50% - var(--text-bg-x)), calc(-50% - var(--text-bg-y)), 0)
        }
        
        .banner-text {
            transform: translate3d(calc(-50% - var(--text-x)), calc(-50% - var(--text-y)), 0)
        }
    }

    // 大图片
    .banner {
        @apply
        relative
        -z-[1]
        h-full
        bg-center
        bg-no-repeat
        transition-[background]
        duration-500
        bg-cover;
        background-image: var(--sm-bg);
        
        // 大屏显示图片
        @media (min-width: 1024px) {
            background-image: var(--lg-bg);
        }

        // 图片的遮罩
        &::before {
            content:'';
            @apply
            absolute
            inset-0
          bg-[#4e30d181];
        }
    }

    // 动态方框
    .banner-text-bg {
        @apply
        w-[300px]
        h-[240px]
        transition-transform
        duration-1000
        pointer-events-none
        border-solid
        ease-main-banner
        border-[11px]
        border-[#ffffff4d];

        @apply
        sm:w-[500px]
        sm:h-[330px]
        lg:w-[600px]
        lg:h-[430px];
    }

    // 方框中的文字
    .banner-text {
        @apply
        transition-transform
        duration-1000
        ease-main-banner
        text-center
        w-full
        text-white
        pointer-events-none;

        // 大标题
        .banner-text-title {
            @apply
            relative
            text-[40px]
            font-bold
            leading-[60px]
            whitespace-nowrap;

            @apply
            sm:text-[70px]
            sm:leading-[90px]
            lg:text-[110px]
            lg:leading-[130px];

            // 标题文字后圆点
            .round-icon {
                @apply
                inline-block
                w-[10px]
                h-[10px]
                bg-[#f36b21]
                rounded-full;

                @apply
                sm:w-[15px]
                sm:h-[15px]
                lg:w-[20px]
                lg:h-[20px];
                
                animation-duration: 2s;
                animation-delay: 2.5s;
            }
        }

        // 大标题下的文字
        .banner-text-subtitle {
            @apply
            text-[15px];

            @apply
            sm:text-[25px]
            lg:text-[30px];
        }
    }

    .arrow-bottom {
        @apply
        flex
        justify-center
        items-center
        absolute
        w-[30px]
        h-[30px]
        bottom-[30px]
        rounded-full
        bg-white
        left-1/2
        transform
        cursor-pointer
        transition-all
        duration-700
        ease-in-out
        shadow-slate-600
        shadow-md
        -translate-x-1/2;

        svg {
            @apply
            mt-[3px]
            animate-bounce
            pointer-events-none;
        }
    }
}

.fade-in-up {
    animation: bgFadeInUp .5s ease-in-out;
}

.title-in-up {
    animation: fontLgFadeInUp 1.2s ease-in-out;
}

.subtitle-in-up {
    animation: fontSmFadeInUp .7s ease-in;
}

@keyframes bgFadeInUp {
    0% {
        opacity: 0;
        width: 50px;
        height: 50px;
    }

    100% {
        opacity: 1;
    }
}

@keyframes fontLgFadeInUp {
    0% {
        font-size: 30px;
        opacity: 0;
    }

    50% {
        opacity: 1;
    }
}

@keyframes fontSmFadeInUp {
    0% {
        font-size: 10px;
        opacity: 0;
    }

    50% {
        opacity: 1;
    }
}
</style>
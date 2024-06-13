<script setup lang='ts'>
import { ref } from 'vue'
import { Icon } from '@iconify/vue';
import logo from '@/assets/img/logo.png'

// 是否处于顶部50之内
const isTop = ref(true)
// 监听窗口滚动
window.addEventListener('scroll', ({ target: { scrollingElement } }: any) => {
    if (isTop.value) isTop.value = false
    if (scrollingElement.scrollTop <= 50) isTop.value = true
})

// 是否展开导航
const isOpenMenu = ref(false)

</script>
<template>
    <header class="header" :class="{
        'not-top': !isTop
    }">
        <div class="header-container">
            <div class="header-left">
                <img class="header-logo" :src="logo" alt="">
                <icon @click="isOpenMenu=!isOpenMenu" icon="material-symbols:menu" width="24" class="header-icon menu-icon" />
            </div>
            <nav class="header-nav" :class="{
                'close-nav': !isOpenMenu
            }">
                <ul>
                    <li class="active">Home</li>
                    <li>Home</li>
                    <li>Home</li>
                    <li>Home</li>
                    <li>Home</li>
                    <li>About Me</li>
                </ul>
            </nav>
            <div class="header-socials">
                <div class="header-socials-icon">
                    <icon icon="carbon:logo-github" class="header-icon" width="24" />
                    <icon icon="carbon:logo-github" class="header-icon" width="24" />
                    <icon icon="carbon:logo-github" class="header-icon" width="24" />
                </div>
            </div>
        </div>
    </header>
</template>
<style scoped lang='scss'>
.header {
    @apply 
    fixed 
    w-full 
    transition-all 
    duration-[600ms]
    ease-in-out
    min-h-[60px]
    z-10
    bg-opacity-sm;

    @apply
    lg:min-h-[100px]
    lg:bg-opacity-lg;

    .header-container {
        @apply 
        flex
        flex-col
        h-full
        overflow-hidden
        transition-[padding]
        px-[15px];

        @apply
        lg:px-[50px]
        lg:flex-row;

        .header-left {
            @apply
            flex-1
            flex
            items-center
            justify-between;

            .header-logo {
                @apply max-h-[60px];
            }

            .menu-icon {
                @apply block;
                @apply lg:hidden;
            }
        }

        .header-nav {
            @apply 
            flex-[4]
            text-center
            transition-[max-height]
            duration-[600ms]
            ease-in-out
            max-h-[450px];

            > ul {
                @apply
                flex
                flex-col
                text-white;

                @apply
                lg:flex-row
                lg:inline-flex;

                > li {
                    @apply 
                    px-[20px] 
                    cursor-pointer 
                    hover:text-[#f36b21]
                    transition-all 
                    text-[14px]
                    duration-300
                    ease-in-out
                    font-semibold
                    leading-[60px]
                    border-b-[1px]
                    border-b-[#333];

                    @apply
                    lg:leading-[100px]
                    lg:border-none;
                }

                .active {
                    @apply text-[#f36b21];
                }
            }
        }

        .close-nav {
            @apply max-h-0 lg:max-h-none;
        }

        .header-socials {
            @apply 
            flex-1
            items-center 
            justify-end
            hidden;

            @apply
            lg:flex;

            .header-socials-icon {
                @apply inline-flex gap-[10px];
            }
        }
    }

    .header-icon {
        @apply 
        text-white
        cursor-pointer
        hover:text-[#f36b21]
        transition-all 
        duration-300
        ease-in-out;
    }
}

// header不在顶部
.not-top {
    @apply bg-opacity-sm lg:min-h-[60px];

    .header-container .header-nav ul li {
        @apply lg:leading-[60px];
    }
}
</style>

<template>
    <router-view v-slot="{ Component }">
        <transition
            mode="out-in"
            enter-active-class="animate__animated animate__fadeIn"
            leave-active-class="animate__animated animate__fadeOut"
        >
            <component :is="Component"/>
        </transition>
    </router-view>
</template>

<style lang="scss">
body {
    font-family: Alibaba-PuHuiTi-Regular, sans-serif;
}

* {
    &::-webkit-scrollbar {
        @apply w-[4px] h-[4px];
    }

    &::-webkit-scrollbar-thumb {
        @apply
        bg-[#ccc]
        rounded-2xl;
    }

    &::-webkit-scrollbar-track-piece {
        @apply bg-transparent;
    }
}

@font-face {
    font-family: 'Alibaba-PuHuiTi-Regular';
    src: url("@/assets/font/Alibaba-PuHuiTi-Regular.ttf");
}

.super-absolute {
    @apply 
    absolute
    left-1/2
    top-1/2
    transform
    -translate-x-1/2
    -translate-y-1/2;
}

.ripple {
    position: absolute;
    left: 0;
    top: 0;
    display: none;
    transform: translate3d(-50%, -50%, 0) scale(0);
    border-radius: 50%;
    pointer-events: none;
    opacity: 0;
    animation: rippleBg 0.6s ease-in-out;
}

@keyframes rippleBg {
    0% {
        transform: translate3d(-50%, -50%, 0) scale(0);
        opacity: 0;
    }
    50% {
        opacity: 0.5;
    }
    100% {
        transform: translate3d(-50%, -50%, 0) scale(1);
        opacity: 0;
    }
}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 70px;
    height: 70px;
    text-align: center;
}
</style>

<script lang="ts" setup>
import LeftMenu from './left-menu.vue'
</script>

<template>
    <div class="h-full flex">
        <!-- 左侧菜单 -->
        <LeftMenu />
        <!-- 右侧撑满 -->
        <div class="chat-content">
            <router-view v-slot="{ Component }">
                <!-- 使用任何自定义过渡和回退到 `fade` -->
                <transition
                    mode="out-in"
                    enter-active-class="animate__animated animate__fadeInLeft animate__faster"
                    leave-active-class="animate__animated animate__fadeOutLeft animate__faster">
                    <component :is="Component" style="background-color: rgb(247, 247, 247)"/>
                </transition>
            </router-view>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.chat-content {
    @apply
    flex-1
    flex
    min-w-0;
}
</style>
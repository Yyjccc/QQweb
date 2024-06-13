<script setup lang="ts">
import BannerMoveBg from '@/pages/opening/HomePage/banner-move-bg.vue'
import MainSwiper from '@/pages/opening/HomePage/main-swiper.vue'
import smMainBg from '@/assets/img/main-sm-sfw.png'
import lgMainBg from '@/assets/img/main-sfw.png'

</script>

<template>
    <banner-move-bg title="The Flesh Is Weak" sub-title="and i moved on from caring" :lg-bg="lgMainBg" :sm-bg="smMainBg" />
    <div class="h-[900px] md:h-[600px]">
        <MainSwiper />
    </div>
    <div class="h-[300px]">内容待定</div>
    <div class="h-[300px]">内容待定</div>
    <div class="h-[300px]">内容待定</div>
    <div class="h-[300px]">内容待定</div>
</template>

<style lang="scss" scoped>
</style>
<script setup lang="ts">
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css";
import { Icon } from '@iconify/vue';
import bookCover from '@/assets/img/book-cover.png'
import bookImg from '@/assets/img/book-img.png'
import iphoneCover from '@/assets/img/iphone-cover.png'
import iphoneImg from '@/assets/img/iphone-img.png'
import { useRouter } from 'vue-router'

const router = useRouter()

const nextSlide = () => {
    // 获取swiper实例
    const swiper = (document.getElementsByClassName('swiper-1')[0] as any).swiper
    swiper.slideNext()
}

const prevSlide = () => {
    // 获取swiper实例
    const swiper = (document.getElementsByClassName('swiper-1')[0] as any).swiper
    swiper.slidePrev()
}

</script>

<template>
    <swiper
        class="swiper-1"
        :loop="true">
        <swiper-slide v-for="i in 2" :key="i">
            <div class="flex flex-1 flex-col md:flex-row">
                <div class="slide-left">
                    <div>
                        <p class="slide-left-title">
                            Let's get started
                            From here
                        </p>
                        <p class="slide-left-subtitle">
                            QO Chat Room is a real-time, including GPT intelligent chat system, if you have more ideas please contact me.
                        </p>
                        <button @click="router.push('/info')" class="slide-left-btn">To Login Now</button>
                        <div class="mt-[40px] mb-[30px]">
                        <button @click="prevSlide" class="slide-to-prev">
                            <Icon icon="material-symbols:arrow-left-rounded" width="31" height="31" />
                        </button>
                        <button @click="nextSlide" class="slide-to-next">
                            <Icon icon="material-symbols:arrow-right-rounded" width="31" height="31" />
                        </button>
                    </div>
                </div>
                </div>
                <div class="slide-right">
                    <div class="w-[500px] h-[300px] relative">
                        <div class="show-book">
                            <img class="absolute z-10 top-[13px] transform scale-x-[.75] scale-y-[.9]" :src="bookImg" alt="">
                            <img class="relative w-[500px] z-10" :src="bookCover" alt="">
                        </div>
                        <div class="show-iphone">
                            <img class="absolute z-20 -top-[163px] transform scale-x-[.29] scale-y-[.27]" :src="iphoneImg" alt="">
                            <img class="relative w-[250px] z-20" :src="iphoneCover" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </swiper-slide>
    </swiper>
</template>

<style scoped lang="scss">
.swiper {
  width: 100%;
  height: 100%;
}

.swiper-1 {
    .swiper-slide {
        @apply
        py-[130px]
        px-[15px]
        flex
        flex-col;

        .slide-left {
            @apply
            flex-1
            flex
            justify-center
            items-center;

            @apply
            md:justify-end;


            .slide-left-title {
                @apply
                font-semibold
                text-[29px]
                max-w-[250px]
                leading-[1.4]
                mb-[17px];
            }

            .slide-left-subtitle {
                @apply
                leading-[25px]
                text-[#7a7a7a]
                text-[16px]
                max-w-[400px]
                mb-[30px];
            }
            
            .slide-left-btn {
                @apply
                w-[200px]
                h-[45px]
                block
                text-center
                text-[13px]
                text-white
                font-bold
                tracking-[2px]
                bg-[#f36b21]
                transition-all
                duration-300
                leading-[45px];

                &:hover {
                    @apply
                    brightness-[1.2]
                    saturate-[1.1]
                    contrast-[1.1];
                }
            }


            [class^=slide-to] {
                @apply
                w-[31px]
                h-[31px]
                transition-all
                duration-500
                rounded-[4px]
                border;

                svg {
                    @apply
                    text-[#333]
                    transition-all
                    duration-500
                }

                &:hover {
                    @apply
                    bg-[#f36b21];
                    
                    svg {
                        @apply
                        text-white;
                    }
                }
                + [class^=slide-to] {
                    @apply
                    ml-[5px]
                }
            }
        }
        .slide-right {
            @apply
            flex
            flex-1
            justify-center
            items-center;

            @apply
            md:justify-start
            lg:ml-[100px];

            .show-book {
                @apply
                absolute;
            }

            .show-iphone {
                @apply
                absolute
                bottom-0
                -left-[60px];
            }
        }
    }
}
</style>
  
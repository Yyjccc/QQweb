<script setup lang="ts">
import PagesHead from '@/pages/opening/MainPage/pages-head.vue'
</script>

<template>
    <div>
        <pages-head />
        <main>
            <router-view />
        </main>
        <footer>footer</footer>
    </div>
</template>
<style scoped lang='scss'>

</style>

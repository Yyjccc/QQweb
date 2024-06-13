<script setup lang='ts'>
const push = (router: string) => {
    location.href = router
}
</script>
<template>
   <div>
        检测到当前账号未退出，是否继续？
        <el-button type="success" @click.stop="push('/using')">立刻进入</el-button>
   </div>
</template>
<style scoped lang='scss'>

</style>


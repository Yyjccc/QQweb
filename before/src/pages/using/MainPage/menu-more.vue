<script setup lang='ts'>
import { Icon } from '@iconify/vue';
import { Setting } from '@element-plus/icons-vue'

</script>
<template>
  <!-- <el-menu-item index="/using/chat-info">
        <el-icon>
            <Icon icon="mdi:information-variant-circle" />
        </el-icon>
        <template #title>网站信息</template>
    </el-menu-item> -->
    
    <el-menu-item index="/using/chat-setting">
        <el-icon>
            <Setting />
        </el-icon>
        <template #title>用户设置</template>
    </el-menu-item>
</template>
<style scoped lang='scss'>

</style>

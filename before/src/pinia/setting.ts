import { defineStore } from 'pinia'

export const useSettingStore = defineStore('settingStore', {
    state: () => ({
        isDark: false,
        isGrey: false
    })
})
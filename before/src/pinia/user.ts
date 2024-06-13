import { User } from '@/constant/types'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
    state: () => ({
        user: {} as User
    }),
    getters: {
        hasUser: state => Object.keys(state.user).length !== 0
    }
})
import { onUnmounted } from 'vue'

export const useInterval = (timeout: number, callback: any) => {
    let time: ReturnType<typeof setInterval> | undefined
    const startInterval = () => {
        if (time) return
        time = setInterval(callback, timeout)
        console.log('start')
    }
    const stopInterval = () => {
        if (!time) return
        clearInterval(time)
        time = undefined
        console.log('stop')
    }
    onUnmounted(stopInterval)
    return { stopInterval, startInterval }
}
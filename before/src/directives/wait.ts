import { Directive } from 'vue'

let times: ReturnType<typeof setTimeout>[] = []

const clearAllTimeout = () => {
    for (const timeKey of times) {
        clearTimeout(timeKey)
    }
    times = []
}
export default {
    updated (el, binding) {
        const { arg, modifiers, value, oldValue } = binding
        // 防止其他地方刷新导致再次执行
        if (oldValue === value) return
        const start = Object.entries(modifiers).at(0)?.at(0) || 0
        const speed = arg || 150
        // 先清空
        clearAllTimeout()
        el.innerHTML = ''
        // 多个定时器延迟赋值
        const valueArr = value.split('')
        for (const charKey in valueArr) {
            times.push(setTimeout(() => {
                el.innerHTML += valueArr[charKey]
            }, (+start + (+charKey * +speed))))
        }
    },
    unmounted () {
        clearAllTimeout()
    }
} as Directive

// v-wait:200.1000   时间间隔200ms，1000ms后开始
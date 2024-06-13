// 创建防抖
export const useDebounce = <T, >(cb: (arg: T) => void, delay: number = 50) => {
    let time: ReturnType<typeof setTimeout> | undefined;
    return (arg: T) => {
        time && clearTimeout(time)
        time = setTimeout(() => {
            time = undefined
            cb(arg)
        }, delay)
    }
}
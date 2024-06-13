import { Directive } from 'vue';

const ripple: Directive = {
    mounted (el, binding) {
        const { value } = binding
        el.style.position = 'relative'
        el.style.overflow = 'hidden'
        // 兼容iphone
        el.style['webkit-backface-visibility'] = 'hidden'
        el.style['-webkit-transform'] = 'translate3d(0, 0, 0)'
        // 添加一个涟漪元素
        const spanEl = document.createElement('span')
        spanEl.className = 'ripple'
        spanEl.style.background = `radial-gradient(ellipse 50% 50% at 50% 50%, ${value} 80%,rgba(0,0,0,0) 100%)`
        el.appendChild(spanEl)
        let time: ReturnType<typeof setTimeout> | undefined
        // 开始监听点击
        el.addEventListener('click', (e: any) => {
            if (time) return
            spanEl.style.left = e.offsetX + 'px'
            spanEl.style.top = e.offsetY + 'px'
            const maxLen = Math.max(e.target.clientHeight, e.target.clientWidth)
            spanEl.style.width = maxLen * 2 + 'px'
            spanEl.style.height = maxLen * 2 + 'px'
            spanEl.style.display = 'block'
            time = setTimeout(() => {
                spanEl.style.display = 'none'
                time = undefined
            }, 600)
        })
    }
}

export default ripple
import { Directive } from 'vue';
import Hammer from 'hammerjs'
    
const hammer: Directive = {
    async mounted(el, binding, vNode: any) {
        // 遍历修饰符，通过value映射到事件名称，添加事件
        const eventMap = binding.value;
        const that = vNode.ctx.setupState
        for (const key in eventMap) {
            const eventName = eventMap[key];
            // 获取变量类型
            const getType = (value: any) => Object.prototype.toString.call(value).slice(0, -1).split(' ')[1];
            // 注册函数
            const vueTouch = (el: HTMLElement, type: string, cb: () => void, isY: boolean) => {
                const hammer = new Hammer(el);
                hammer.on(type, cb);
                if (isY) hammer.get('swipe').set({ direction: Hammer.DIRECTION_VERTICAL });
            };
            // 注册事件
            vueTouch(el, key, that[getType(eventName) === 'String' ? eventName : key], key === 'swipeup' || key === 'swipedown');
        }
    }
}

//  v-hammer="eventMap"
// 映射的事件名称
// const eventMap = {
//     tap: 'onTap',
//     press: 'newPress',
//     swipeup: false,
//     swipedown: {}
// }

export default hammer
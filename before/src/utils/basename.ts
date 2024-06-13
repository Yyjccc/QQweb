/**
 * 获取路径中的文件名，仿 path.basename
 * @param {*} str 
 * @param {*} suffix 是否保留后缀，默认保留
 * @returns 
 */
const basename = (str: string, suffix = true) => {
    // 找 / 分隔
    let index = str.lastIndexOf('/')
    // 如果没有就找 \\ 分隔
    index = index !== -1 ? index : str.lastIndexOf('\\')
    // 返回后一位到最后
    let fileName = str.substring(index + 1)
    if (suffix) return fileName
    // 需要去除后缀
    const suffixIndex = fileName.lastIndexOf('.')
    if (suffixIndex !== -1) return fileName.substring(0, suffixIndex)
    return fileName
}

// 例：
// basename('c:\\static\\pages\\index.html') => index.html
// basename('c:/static/pages/index.html') => index.html
// basename('c:/static/pages/index') => index
// basename('../index.vue') => index.vue
// basename('./../index.text.vue') => index.text.vue
// basename('./../index.text.vue', false) => index.text

export default basename
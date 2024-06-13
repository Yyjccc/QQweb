import { encrypt, decrypt } from './fas-encrypt'

const getToken = () => {
    let tokenItem
    // 查询本地缓存中的token，正常来说只会有一个
    for (const item of Object.keys(localStorage) ) {
        if (item.slice(0, 4) === 'kU2F') {
            tokenItem = item
            break
        }
    }
    return tokenItem ? decrypt(localStorage.getItem(tokenItem) as string) : undefined
}

const clearToken = () => {
    for (const item of Object.keys(localStorage) ) {
        if (item.slice(0, 4) === 'kU2F') localStorage.removeItem(item)
    }
}

const setToken = (token: string) => {
    // 先清空之前的token
    clearToken()
    localStorage.setItem(encrypt('token'), encrypt(token))
}

export {
    getToken,
    setToken,
    clearToken
}
import CryptoJS from 'crypto-js'
import md5 from 'md5'

const KEY = 'secret key FAS'

// 用md5加密出aes的key，长度必须大于3
const encrypt = (str: string) => {
    if (str.length < 3) return ''
    // 取密码的第三位md5加盐加密
    const iv = md5(str[2] + KEY)
    // 拼接第三位，为了解密
    return str[2] + CryptoJS.AES.encrypt(str, iv).toString()
}

// 解密
const decrypt = (encryptStr: string) => {
    if (!encryptStr) return
    const md5Key = md5(encryptStr[0] + KEY)
    const bytes = CryptoJS.AES.decrypt(encryptStr.slice(1), md5Key);
    return bytes.toString(CryptoJS.enc.Utf8);
}

export {
    encrypt,
    decrypt
}
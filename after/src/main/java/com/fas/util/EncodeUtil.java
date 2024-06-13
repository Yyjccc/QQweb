package com.fas.util;

import org.springframework.util.DigestUtils;

public class EncodeUtil {
    public static String encode(String pwd){
        byte[] bytes = pwd.getBytes();
        // 加盐
//        byte[] bytes1 = "FAS".getBytes();
//        for (int i = 0; i < bytes1.length; i++) {
//            bytes[i] += bytes1[i];
//        }
        return DigestUtils.md5DigestAsHex(bytes);
    }
}

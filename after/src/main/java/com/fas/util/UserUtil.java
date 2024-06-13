package com.fas.util;

import com.fas.entity.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class UserUtil {

    private final HttpServletRequest httpServletRequest;

    public UserUtil(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    // 获取当前请求的用户qo
    public String getUserQo() {
        User user = (User) httpServletRequest.getAttribute("user");
        return user.getQoNum();
    }

    // 生成随机六位字符
    public static String getCode(){
        String s1 = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3);
        String s2 = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8))).substring(0, 3);
        return s1 + s2;
    }
}

package com.fas.controller;


import com.fas.dto.ForgetDto;
import com.fas.dto.LoginDto;
import com.fas.dto.RegisterDto;
import com.fas.my_interface.HavePack;
import com.fas.service.UserService;
import com.fas.util.ValidUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;

@HavePack
@RestController
public class PublicController {

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param loginDto      用户名，密码
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody LoginDto loginDto, BindingResult bindingResult) {

        return userService.login(loginDto);
    }

    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/captcha")
    public HashMap<String, String> getCode() {
        return userService.getCode();
    }

    /**
     * 注册用户
     * @param registerDto
     * @param bindingResult
     */
    @PostMapping("/register")
    public void register (@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {

        userService.register(registerDto);
    }



    /**
     * 更新密码
     * @param forgetDto
     * @param bindingResult
     */
    @PostMapping("/forget")
    public void forget (@Valid @RequestBody ForgetDto forgetDto, BindingResult bindingResult) {
        ValidUtil.handler(bindingResult);
        userService.forget(forgetDto);
    }
}

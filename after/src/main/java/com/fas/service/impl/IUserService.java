package com.fas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.fas.dto.*;
import com.fas.entity.Friend;
import com.fas.entity.Talk;
import com.fas.entity.User;
import com.fas.exception.ValidException;
import com.fas.mapper.FriendMapper;
import com.fas.mapper.UserMapper;
import com.fas.my_enum.ResultEnum;
import com.fas.service.UserService;
import com.fas.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IUserService implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private UserUtil userUtil;

    @Value("${upload.url}")
    private String saveUrl;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private FriendMapper friendMapper;


    /**
     * 校验验证码是否正确
     * @param code 用户输入的验证码
     * @param codeKey 产生的 key
     */
    private void validCode(String code, String codeKey) {
        // 校验验证码是否正确
        String codeRedisKey = "code:" + codeKey;
        String redisCode = (String) redisTemplate.opsForValue().get(codeRedisKey);
        // 验证码不存在：已过期
        if (!StringUtils.hasText(redisCode)) throw new ValidException(ResultEnum.CODE_TIMEOUT);
        // 验证码存在，错误
        if (!code.equals(redisCode)) {
            // 校验错误后删除验证码
            redisTemplate.delete(codeRedisKey);
            throw new ValidException(ResultEnum.CODE_FAIL);
        }
        // 校验通过后删除验证码
        redisTemplate.delete(codeRedisKey);
    }

    private HashMap<String, String> userLogin (User user) {
        // 存入redis
        String key = "user:" + user.getQoNum();
        redisTemplate.opsForValue().set(key, user);
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
        String numKey = "num:" + user.getQoNum();
        Integer userNum = (Integer) redisTemplate.opsForValue().get(numKey);
        // 不存在为1
        if (!Optional.ofNullable(userNum).isPresent()) {
            userNum = 1;
        } else {
            userNum += 1;
        }
        redisTemplate.opsForValue().set(numKey, userNum);
        redisTemplate.expire(numKey, 2, TimeUnit.HOURS);
        // 生成token
        String jwt = JwtUtil.createJWT(user.getQoNum() + "#" + userNum);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return map;
    }

    @Override
    public HashMap<String, String> login(LoginDto loginDto) {
        //validCode(loginDto.getCode(), loginDto.getCodeKey());
        // 获取加密后的对象
        User user = loginDto.getUser();
        // 使用用户名查询是否存在
        User one = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getQoNum, user.getQoNum())
                .one();
        if (!Optional.ofNullable(one).isPresent()) throw new ValidException(ResultEnum.USER_ERROR);
        // 对比密码
        if (!user.getPassword().equals(one.getPassword())) throw new ValidException(ResultEnum.USER_FAIL);
        return userLogin(one);
    }

    @Override
    public void logout() {
        String qoNum = userUtil.getUserQo();
        redisTemplate.delete("num:" + qoNum);
        redisTemplate.delete("user:" + qoNum);
    }

    @Override
    public HashMap<String, String> upload(MultipartFile file) {
        //获取文件后缀名
        String[] arrayString = file.getOriginalFilename().split("\\.");
        // 后缀名
        String fileSuffix = "." + arrayString[arrayString.length - 1];
        //新生成文件名
        String fileName = UUID.randomUUID().toString() + System.currentTimeMillis() + fileSuffix;
        File file1 = new File(saveUrl + fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 生成map返回
        HashMap<String, String> map = new HashMap<>();
        map.put("url", saveUrl + fileName);
        return map;
    }

    @Override
    public HashMap<String, String> getCode() {
        // 生成图片
        imgVerifyCode ivc = new imgVerifyCode();
        BufferedImage image = ivc.getImage();
        String imgBase64 = ivc.getBase64(image);
        // 获取code
        String text = ivc.getText();
        // 生成唯一密钥
        String codeKey = UserUtil.getCode();
        // 密钥和code存入redis，五分钟过期
        String key = "code:" + codeKey;
        redisTemplate.opsForValue().set(key, text);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        // 返回密钥和图片base64
        HashMap<String, String> map = new HashMap<>();
        map.put("image", imgBase64);
        map.put("codeKey", codeKey);
        return map;
    }

    @Override
    public User getInfo() {
        // 获取数据库中用户信息
        return userMapper.selectById(userUtil.getUserQo());
    }

    @Override
    public void register(RegisterDto registerDto) {
        // 查询邮箱验证码是否通过
        //String emailCode = (String) redisTemplate.opsForValue().get("email:" + registerDto.getEmail());
        //if(!Optional.ofNullable(emailCode).isPresent()) throw new ValidException(ResultEnum.EMAIL_CODE_NOT);
        //if (!registerDto.getEmailCode().equals(emailCode)) throw new ValidException(ResultEnum.EMAIL_CODE_FAIL);
        // 邮箱是否已有对应账号
       // LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(User::getEmail, registerDto.getEmail());
      //  User user1 = userMapper.selectOne(wrapper);
       // if (Optional.ofNullable(user1).isPresent()) throw new ValidException(ResultEnum.EMAIL_HAS_USER);
        // 用户名是否存在
        String qoNum = registerDto.getQoNum();
        User user = userMapper.selectById(qoNum);
        if (Optional.ofNullable(user).isPresent()) throw new ValidException(ResultEnum.HAS_USER);
        // 添加用户
        int insert = userMapper.insert(registerDto.getUser());
        if (insert==0)  throw new ValidException(ResultEnum.MYSQL_ERROR);
        // 删除邮箱验证码
       // redisTemplate.delete("email:" + registerDto.getEmail());
    }


    // 校验邮箱验证码正确和邮箱是否绑定账号，返回email绑定的账号
    private User validEmail (String email, String emailCode) {
        // 查询邮箱真实验证码
        String redisEmailCode = (String) redisTemplate.opsForValue().get("email:" + email);
        if (!Optional.ofNullable(redisEmailCode).isPresent()) throw new ValidException(ResultEnum.EMAIL_NOT_SEND);
        // 校验验证码
        if (!redisEmailCode.equals(emailCode)) throw new ValidException(ResultEnum.EMAIL_CODE_FAIL);
        // 删除邮箱验证码
        redisTemplate.delete("email:" + email);
        // 查询该邮箱是否有绑定账号
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        if (!Optional.ofNullable(user).isPresent()) throw new ValidException(ResultEnum.EMAIL_NOT_USER);
        return user;
    }



    @Override
    public void forget(ForgetDto forgetDto) {
        User one = validEmail(forgetDto.getEmail(), forgetDto.getEmailCode());
        // 获取用户更新密码
        User user = forgetDto.getUser();
        // 检查两次密码是否一致
        if (one.getPassword().equals(user.getPassword())) throw new ValidException(ResultEnum.FORGET_PWD_EQ);
        user.setQoNum(one.getQoNum());
        // 更新密码
        userMapper.updateById(user);
    }

    @Override
    public List<User> getFriendList() {
        return userMapper.getFriendList(userUtil.getUserQo());
    }

    @Override
    public void deleteFriend(String qoNum) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .and(i ->
                        i.eq(Friend::getFromUser, qoNum)
                        .eq(Friend::getToUser, userUtil.getUserQo())
                )
                .or(i ->
                        i.eq(Friend::getFromUser, userUtil.getUserQo())
                        .eq(Friend::getToUser, qoNum)
                );
        friendMapper.delete(wrapper);
    }

    @Override
    public List<User> getAddGroupUser(Long groupNum) {
        return userMapper.getAddGroupUser(groupNum, userUtil.getUserQo());
    }

    @Override
    public List<User> getAddUser(String nickname) {
        return userMapper.getAddUser(userUtil.getUserQo(), nickname);
    }

    @Override
    public void updateSelf(UpdateSelfDto updateSelfDto) {
        // 获取user
        User user = updateSelfDto.getUser();
        user.setQoNum(userUtil.getUserQo());
        userMapper.updateById(user);
        logout();
    }
}

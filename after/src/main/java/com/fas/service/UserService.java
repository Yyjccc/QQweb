package com.fas.service;

import com.fas.dto.*;
import com.fas.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    HashMap<String, String> login(LoginDto loginDto);

    void logout();

    HashMap<String, String> upload(MultipartFile file);

    HashMap<String, String> getCode();

    User getInfo();

    void register(RegisterDto registerDto);



    void forget(ForgetDto forgetDto);

    List<User> getFriendList();

    void deleteFriend(String qoNum);

    List<User> getAddGroupUser(Long groupNum);

    List<User> getAddUser(String nickname);

    void updateSelf(UpdateSelfDto updateSelfDto);
}

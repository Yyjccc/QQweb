package com.fas.controller;


import com.fas.dto.*;
import com.fas.entity.Group;
import com.fas.entity.User;
import com.fas.my_interface.HaveAuth;
import com.fas.my_interface.HavePack;
import com.fas.service.GroupService;
import com.fas.service.RecordService;
import com.fas.service.TalkService;
import com.fas.service.UserService;
import com.fas.util.ValidUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@HavePack
@HaveAuth
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @Resource
    private GroupService groupService;

    @Resource
    private RecordService recordService;

    /**
     * 退出
     */
    @DeleteMapping("/logout")
    public void logout() {
        userService.logout();
    }

    /**
     * 上传图片
     *
     * @return
     */
    @PostMapping("/upload")
    public HashMap<String, String> upload(@RequestBody MultipartFile file) {
        return userService.upload(file);
    }

    /**
     * 获取自己的信息
     * @return
     */
    @GetMapping("/info")
    public User getInfo () {
        return userService.getInfo();
    }




    /**
     * 获取两个人的消息记录，指定页数
     */
    @GetMapping("/record")
    public List<RecordListDto> getRecord (
        @RequestParam Integer current,
        @RequestParam String qoNum
    ) {
        return recordService.getRecord(current, qoNum);
    }

    /**
     * 获取好友列表
     * @return
     */
    @GetMapping("/friend")
    public List<User> getFriendList () {
        return userService.getFriendList();
    }

    /**
     * 获取消息列表中群组列表
     * @return
     */
    @GetMapping("/group/msg")
    public List<GroupMsgListDto> getMessageGroup () {
        return groupService.getMessageGroup();
    }



    /**
     * 获取群聊的聊天记录（指定）
     * @param current
     * @param groupNum
     * @return
     */
    @GetMapping("/group/record")
    public List<RecordListDto> getGroupRecord (
            @RequestParam Integer current,
            @RequestParam Long groupNum
    ) {
        return groupService.getGroupRecord(current, groupNum);
    }



    /**
     * 获取群组列表
     * @return
     */
    @GetMapping("/group/list")
    public List<Group> getUserGroup() {
        return groupService.getUserGroup();
    }

    /**
     * 获取一个群组里面所有用户
     * @return
     */
    @GetMapping("/group/user")
    public List<User> getGroupUserList (@RequestParam Long groupNum) {
        return groupService.getGroupUserList(groupNum);
    }

    /**
     * 邀请指定用户进群聊
     * @return
     */
    @PostMapping("/group/user/{qoNum}/{groupNum}")
    public void inviteUserToGroup (@PathVariable String qoNum, @PathVariable Long groupNum) {
        groupService.inviteUserToGroup(qoNum, groupNum);
    }

    /**
     * 请求好友
     * @param qoNum
     */
    @PostMapping("/to/{qoNum}")
    public void inviteUserToUser (@PathVariable String qoNum) {
        groupService.inviteUserToUser(qoNum);
    }




    /**
     * 用户处理群组请求
     * @param groupNum
     */
    @PutMapping("/agree/group/{groupNum}/{status}")
    public void agreeGroup (@PathVariable Long groupNum, @PathVariable String status) {
        groupService.agreeGroup(groupNum, status);
    }

    /**
     * 用户处理用户请求
     * @param qoNum
     */
    @PutMapping("/agree/user/{qoNum}/{status}")
    public void agreeUser (@PathVariable String qoNum, @PathVariable String status) {
        groupService.agreeUser(qoNum, status);
    }

    /**
     * 用户新建群聊
     * @param addGroupDto
     * @param bindingResult
     */
    @PostMapping("/group")
    public void addGroup (@Valid @RequestBody AddGroupDto addGroupDto, BindingResult bindingResult) {
        ValidUtil.handler(bindingResult);
        groupService.addGroup(addGroupDto);
    }

    /**
     * 退出群聊
     * @param groupNum
     */
    @DeleteMapping("/group/user/{groupNum}")
    public void outGroup(@PathVariable Long groupNum) {
        groupService.outGroup(groupNum);
    }

    /**
     * 删除好友
     * @param qoNum
     */
    @DeleteMapping("/del/{qoNum}")
    public void deleteFriend(@PathVariable String qoNum) {
        userService.deleteFriend(qoNum);
    }

    /**
     * 获取不在群组中的好友
     * @param groupNum
     */
    @GetMapping("/add/group/user")
    public List<User> getAddGroupUser (@RequestParam Long groupNum) {
        return userService.getAddGroupUser(groupNum);
    }

    /**
     * 模糊查询可添加用户
     * @return
     */
    @GetMapping("/add/user")
    public List<User> getAddUser (@RequestParam String nickname) {
        return userService.getAddUser(nickname);
    }

    /**
     * 修改个人信息
     * @param updateSelfDto
     * @param bindingResult
     */
    @PostMapping("/update")
    public void updateSelf (@Valid @RequestBody UpdateSelfDto updateSelfDto, BindingResult bindingResult) {
        ValidUtil.handler(bindingResult);
        userService.updateSelf(updateSelfDto);
    }
}

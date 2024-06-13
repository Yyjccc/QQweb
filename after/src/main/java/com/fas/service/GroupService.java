package com.fas.service;

import com.fas.dto.AddGroupDto;
import com.fas.dto.GroupMsgDto;
import com.fas.dto.GroupMsgListDto;
import com.fas.dto.RecordListDto;
import com.fas.entity.Group;
import com.fas.entity.Gu;
import com.fas.entity.User;

import java.util.List;

public interface GroupService {
    List<GroupMsgListDto> getMessageGroup();

    List<RecordListDto> getGroupRecordAll(Integer current, Long groupNum);

    List<RecordListDto> getGroupRecord(Integer current, Long groupNum);

    void send(GroupMsgDto groupMsgDto);

    List<Group> getUserGroup();

    List<User> getGroupUserList(Long groupNum);

    void inviteUserToGroup(String qoNum, Long groupNum);

    List<User> waitUserList();

    void inviteUserToUser(String qoNum);

    List<Group> waitGroupList();

    void agreeGroup(Long groupNum, String status);

    void agreeUser(String qoNum, String status);

    void addGroup(AddGroupDto addGroupDto);

    void outGroup(Long groupNum);
}

package com.fas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fas.dto.*;
import com.fas.entity.*;
import com.fas.exception.ValidException;
import com.fas.mapper.*;
import com.fas.my_enum.ResultEnum;
import com.fas.service.GroupService;
import com.fas.util.UserUtil;
import com.fas.util.UserWsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IGroupService implements GroupService {

    @Resource
    private GroupMapper groupMapper;

    @Autowired
    @Lazy
    private UserWsUtil userWsUtil;

    @Resource
    @Lazy
    private UserUtil userUtil;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private RgMapper rgMapper;

    @Resource
    private GuMapper guMapper;

    @Resource
    private FriendMapper friendMapper;

    @Override
    public List<GroupMsgListDto> getMessageGroup() {
        List<Group> messageGroup = groupMapper.getMessageGroup(userUtil.getUserQo());
        ArrayList<GroupMsgListDto> result = new ArrayList<>();
        for (Group group : messageGroup) {
            GroupMsgListDto groupMsgListDto = new GroupMsgListDto();
            groupMsgListDto.setCreateTime(group.getCreateTime());
            groupMsgListDto.setGroupAvatar(group.getGroupAvatar());
            groupMsgListDto.setGroupName(group.getGroupName());
            groupMsgListDto.setGroupRemark(group.getGroupRemark());
            groupMsgListDto.setGroupNum(group.getGroupNum());
            groupMsgListDto.setGroupUser(group.getGroupUser());
            groupMsgListDto.setRecord(groupMapper.getGroupEncRecord(group.getGroupNum()));
            result.add(groupMsgListDto);
        }
        return result;
    }

    @Override
    public List<RecordListDto> getGroupRecordAll(Integer current, Long groupNum) {
        return groupMapper.getGroupRecordAll(current * 10, groupNum);
    }

    @Override
    public List<RecordListDto> getGroupRecord(Integer current, Long groupNum) {
        return groupMapper.getGroupRecord((current - 1) * 10, groupNum);
    }

    @Override
    public void send(GroupMsgDto groupMsgDto) {
        // 获取消息内容
        String recordContent = groupMsgDto.getRecordContent();
        String recordType = groupMsgDto.getRecordType();
        Record record = new Record();
        record.setRecordContent(recordContent);
        record.setRecordType(recordType);
        record.setRecordUser(userWsUtil.getUserQo());
        String recordId = UUID.randomUUID().toString().replaceAll("[-]", "");
        record.setRecordId(recordId);
        // 新增
        if (recordMapper.insert(record)==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
        // 添加到关系表
        Long toGroup = groupMsgDto.getToGroup();
        Rg rg = new Rg();
        rg.setGNum(toGroup);
        rg.setRId(recordId);
        if (rgMapper.insert(rg)==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
    }

    @Override
    public List<Group> getUserGroup() {
        return groupMapper.getUserGroup(userUtil.getUserQo());
    }

    @Override
    public List<User> getGroupUserList(Long groupNum) {
        return guMapper.getGroupUserList(groupNum);
    }

    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }

    @Override
    public void inviteUserToGroup(String qoNum, Long groupNum) {
        Gu gu = new Gu();
        gu.setGroupNum(groupNum);
        gu.setUser(qoNum);
        gu.setGroupAgree("0");
        // 邀请前先查询是否已被拒绝过
        LambdaQueryWrapper<Gu> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Gu::getGroupNum, groupNum)
                .eq(Gu::getUser, qoNum)
                .eq(Gu::getUserAgree, "1");
        Gu gu1 = guMapper.selectOne(wrapper);
        if (Optional.ofNullable(gu1).isPresent()) throw new ValidException(ResultEnum.GROUP_USER_FAIL);
        if (guMapper.insert(gu)==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
    }

    @Override
    public List<User> waitUserList() {
        return guMapper.waitUserList(userWsUtil.getUserQo());
    }

    @Override
    public void inviteUserToUser(String qoNum) {
        Friend friend = new Friend();
        friend.setFromUser(userUtil.getUserQo());
        friend.setToUser(qoNum);
        // 邀请前先查询是否已被拒绝过
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Friend::getToUser, qoNum)
                .eq(Friend::getFromUser, userUtil.getUserQo())
                .eq(Friend::getIsAgree, "1");
        Friend friend1 = friendMapper.selectOne(wrapper);
        if (Optional.ofNullable(friend1).isPresent()) throw new ValidException(ResultEnum.GROUP_USER_FAIL);
        if (friendMapper.insert(friend)==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
    }

    @Override
    public List<Group> waitGroupList() {
        return guMapper.waitGroupList(userWsUtil.getUserQo());
    }

    @Override
    public void agreeGroup(Long groupNum, String status) {
        Gu gu = new Gu();
        gu.setUserAgree(status);
        LambdaQueryWrapper<Gu> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Gu::getGroupNum, groupNum)
                .eq(Gu::getUser, userUtil.getUserQo());
        if (guMapper.update(gu, wrapper)==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
    }

    @Override
    public void agreeUser(String qoNum, String status) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Friend::getFromUser, qoNum)
                .eq(Friend::getToUser, userUtil.getUserQo());
        Friend friend = new Friend();
        friend.setIsAgree(status);
        friendMapper.update(friend, wrapper);
    }

    @Override
    public void addGroup(AddGroupDto addGroupDto) {
        Group group = new Group();
        group.setGroupRemark(addGroupDto.getGroupRemark());
        group.setGroupAvatar(addGroupDto.getGroupAvatar());
        group.setGroupName(addGroupDto.getGroupName());
        long id = getID();
        group.setGroupNum(id);
        group.setGroupUser(userUtil.getUserQo());
        group.setCreateTime(LocalDateTime.now());
        groupMapper.insertGroup(group);
        Gu gu = new Gu();
        gu.setUserAgree("0");
        gu.setGroupAgree("0");
        gu.setUser(userUtil.getUserQo());
        gu.setGroupNum(id);
        guMapper.insert(gu);
        GroupMsgDto groupMsgDto = new GroupMsgDto();
        groupMsgDto.setToGroup(id);
        groupMsgDto.setRecordContent("欢迎加入" + addGroupDto.getGroupName());
        groupMsgDto.setRecordType("text");
        send(groupMsgDto);
    }

    @Override
    public void outGroup(Long groupNum) {
        LambdaQueryWrapper<Gu> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Gu::getGroupNum, groupNum)
                .eq(Gu::getUser, userUtil.getUserQo());
        guMapper.delete(wrapper);
        // 如果是最后一个人，删除群组
        LambdaQueryWrapper<Gu> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Gu::getGroupNum, groupNum);
        Gu gu = guMapper.selectOne(wrapper1);
        if (!Optional.ofNullable(gu).isPresent()) {
            groupMapper.delGroup(groupNum);
        }
    }
}

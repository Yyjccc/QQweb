package com.fas.service.impl;

import com.fas.dto.MessageListDto;
import com.fas.entity.Record;
import com.fas.entity.User;
import com.fas.mapper.RecordMapper;
import com.fas.mapper.TalkMapper;
import com.fas.mapper.UserMapper;
import com.fas.service.TalkService;
import com.fas.util.UserUtil;
import com.fas.util.UserWsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class ITalkService implements TalkService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserWsUtil userWsUtil;

    @Resource
    private RecordMapper recordMapper;

    @Override
    public List<MessageListDto> getMessage(String nickname) {
        String userQo = userWsUtil.getUserQo();
        ArrayList<MessageListDto> messageListDtos = new ArrayList<>();
        // 获取所有联系的用户
        List<User> msgUser = userMapper.getMsgUser(userQo, nickname);
        for (User user : msgUser) {
            MessageListDto messageListDto = new MessageListDto();
            messageListDto.setUser(user);
            // 获取最后聊天记录
            Record endRecord = recordMapper.getEndRecord(userQo, user.getQoNum());
            messageListDto.setRecord(endRecord);
            messageListDtos.add(messageListDto);
        }
        // 按照最后聊天排序
        Collections.sort(messageListDtos, (MessageListDto messageListDto, MessageListDto t1) ->
                (int) (t1.getRecord().getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() -
                messageListDto.getRecord().getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        return messageListDtos;
    }
}

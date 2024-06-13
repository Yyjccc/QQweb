package com.fas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fas.dto.RecordDto;
import com.fas.dto.RecordListDto;
import com.fas.entity.Record;
import com.fas.entity.Rt;
import com.fas.entity.Talk;
import com.fas.exception.ValidException;
import com.fas.mapper.RecordMapper;
import com.fas.mapper.RtMapper;
import com.fas.mapper.TalkMapper;
import com.fas.my_enum.ResultEnum;
import com.fas.service.RecordService;
import com.fas.util.UserWsUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IRecordService implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private TalkMapper talkMapper;

    @Resource
    private RtMapper rtMapper;

    @Resource
    private UserWsUtil userWsUtil;

    @Override
    public List<RecordListDto> getRecord(Integer current, String qoNum) {
        return recordMapper.getRecord((current - 1) * 10, qoNum, userWsUtil.getUserQo());
    }

    @Override
    public List<RecordListDto> getRecordAll(Integer current, String qoNum) {
        return recordMapper.getRecordAll(current * 10, qoNum, userWsUtil.getUserQo());
    }

    @Override
    public void send(RecordDto recordDto) {
        // 获取发送人
        String userQo = userWsUtil.getUserQo();
        // 获取对方qo
        String toUser = recordDto.getToUser();
        // 获取record
        Record record = new Record();
        record.setRecordContent(recordDto.getRecordContent());
        record.setRecordType(recordDto.getRecordType());
        record.setRecordUser(userQo);
        // 生成唯一标识
        String recordId = UUID.randomUUID().toString().replaceAll("[-]", "");
        record.setRecordId(recordId);
        // 添加到表
        int insert = recordMapper.insert(record);
        if (insert==0) throw new ValidException(ResultEnum.MYSQL_ERROR);
        // 查询 Talk id
        LambdaQueryWrapper<Talk> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .and(i ->
                    i.eq(Talk::getTalkUser1, userQo)
                    .eq(Talk::getTalkUser2, toUser)
                )
                .or(i ->
                    i.eq(Talk::getTalkUser2, userQo)
                    .eq(Talk::getTalkUser1, toUser)
                );
        Talk talk = talkMapper.selectOne(wrapper);
        String talkId;
        if (Optional.ofNullable(talk).isPresent()) {
            talkId = talk.getTalkId();
        } else {
            talkId = UUID.randomUUID().toString().replaceAll("[-]", "");
            Talk talk1 = new Talk();
            talk1.setTalkId(talkId);
            talk1.setTalkUser1(userQo);
            talk1.setTalkUser2(toUser);
            if (talkMapper.insert(talk1) == 0) throw new ValidException(ResultEnum.MYSQL_ERROR);
        }
        // rt 插入
        Rt rt = new Rt();
        rt.setRId(recordId);
        rt.setTId(talkId);
        if (rtMapper.insert(rt) == 0) throw new ValidException(ResultEnum.MYSQL_ERROR);
    }
}

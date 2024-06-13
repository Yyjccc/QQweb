package com.fas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fas.dto.AddGroupDto;
import com.fas.dto.GroupMsgListDto;
import com.fas.dto.RecordListDto;
import com.fas.entity.Group;
import com.fas.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMapper extends BaseMapper<Group> {
    List<RecordListDto> getGroupRecordAll(@Param("offset") Integer current, @Param("groupNum") Long groupNum);

    List<RecordListDto> getGroupRecord(@Param("offset") Integer current, @Param("groupNum") Long groupNum);

    List<Group> getUserGroup(String qoNum);

    void delGroup(Long groupNum);

    void insertGroup(Group group);

    List<Group> getMessageGroup(String qoNum);

    Record getGroupEncRecord(Long groupNum);
}

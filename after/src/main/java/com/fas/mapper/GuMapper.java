package com.fas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fas.entity.Group;
import com.fas.entity.Gu;
import com.fas.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuMapper extends BaseMapper<Gu> {
    List<User> waitUserList(String qoNum);

    List<Group> waitGroupList(String qoNum);

    List<User> getGroupUserList(Long groupNum);
}

package com.fas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fas.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    List<User> getMsgUser(@Param("qoNum") String qoNum, @Param("nickname") String nickname);

    List<User> getFriendList(String qoNum);

    List<User> getAddGroupUser(@Param("groupNum") Long groupNum, @Param("qoNum") String qoNum);

    List<User> getAddUser(@Param("qoNum") String qoNum, @Param("nickname") String nickname);
}

package com.jeff.teammate.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
//    int deleteTeamId(int id);
//    int setTeamId(int id,int teamId);
//    Integer getTeamId(int id);

    @Autowired
    UserMapper userMapper;

    @Test
    public void deleteTeamId() {
        userMapper.setTeamId(2,2);
        userMapper.setTeamId(3,3);
        userMapper.setTeamId(4,4);
        userMapper.deleteTeamId(3);
        Integer teamId1 = userMapper.getTeamId(3);
        Integer teamId2 = userMapper.getTeamId(4);
//        Assertions.assertNull(teamId1);
//        Assertions.assertEquals(teamId2,4);

    }

    @Test
    public void setTeamId() {
    }

    @Test
    public void getTeamId() {
    }
}
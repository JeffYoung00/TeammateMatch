package com.jeff.teammate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeff.teammate.mapper.UserMapper;
import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.User;
import com.jeff.teammate.service.TagService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceImplTest {

    @Autowired
    SqlSessionFactory factory;

    @Autowired
    TagService tagService;

    @Test
    public void gameInit() {
        //System.out.println(Game.games);
        SqlSession sqlSession = factory.openSession(ExecutorType.BATCH,true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        QueryWrapper<User> select=new QueryWrapper<>();
        UpdateWrapper<User> update=new UpdateWrapper<>();
        update.eq("avatarUrl","3").set("avatarUrl","1");
        mapper.update(null,update);
        sqlSession.commit();
    }

    @Test
    public void gameTags() {
    }

    @Test
    public void addTag() {
    }

    @Test
    public void deleteTag() {
    }
}
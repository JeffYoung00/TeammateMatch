package com.jeff.teammate.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeff.teammate.mapper.UserMapper;
import com.jeff.teammate.model.User;
import com.jeff.teammate.service.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Random;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    UserService userService;
    @Autowired
    SqlSessionFactory factory;
    @Resource
    UserMapper userMapper;

    @Test
    public void doMain(){
        SqlSession sqlSession=factory.openSession(ExecutorType.BATCH);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        //Jeff
        String encryption= DigestUtils.md5DigestAsHex( ("Jeff"+"testtest").getBytes() );
        String avatar1="src/assets/avatar1.png";
        String avatar2="src/assets/avatar2.png";
        String avatar3="src/assets/avatar3.png";
        Random random=new Random();

        int start=1;

        for(int i=0;i<100;i++){
            int id=start+i;
            User user=new User();
            user.setUserName("test_user_"+id);
            user.setUserPassword(encryption);
            //state
            user.setUserState(1);
            //email
            user.setEmail("xxx@qq.com");
            if(id%3==1){
                user.setAvatarUrl("src/assets/avatar1.png");
            }else if(id%3==2){
                user.setAvatarUrl("src/assets/avatar2.png");
            }else {
                user.setAvatarUrl("src/assets/avatar3.png");
            }
            if(id%2==0){
                user.setGender("male");
            }else{
                user.setGender("female");
            }
            //32
            StringBuilder vector=new StringBuilder();
            vector.append("{\"0\":[");
            for(int j=0;j<31;j++){
                int t=random.nextInt(256);
                vector.append(t-128);
                vector.append(",");
            }
            vector.append(random.nextInt(256)-128);
            vector.append("]}");
            user.setVector(new String(vector));
            mapper.insert(user);
        }
        sqlSession.commit();
        sqlSession.close();

    }
    @Test
    public void doUpdatePass(){
        User user=new User();
        user.setUserPassword( DigestUtils.md5DigestAsHex( ("Jeff"+"testtest").getBytes()));
        user.setUserState(1);
        userMapper.update(user,new UpdateWrapper<>());
    }

    @Test
    public void doUpdateVec(){
        User user=new User();
        user.setUserPassword( DigestUtils.md5DigestAsHex( ("Jeff"+"testtest").getBytes()));
        user.setUserState(1);
        userMapper.update(user,new UpdateWrapper<>());
    }

    @Test
    public void getMyTags() {
    }

    @Test
    public void updateMyTags() {
    }

    @Test
    public void searchUserByTags() {
    }
}
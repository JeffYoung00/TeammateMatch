package com.jeff.teammate.mapper;

import com.jeff.teammate.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 21029
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-03-06 22:22:20
* @Entity com.jeff.teammate.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 用userId删除teamId字段
     * @param id
     * @return
     */
    int deleteTeamId(int id);
    int setTeamId(int id,int teamId);
    Integer getTeamId(int id);
}





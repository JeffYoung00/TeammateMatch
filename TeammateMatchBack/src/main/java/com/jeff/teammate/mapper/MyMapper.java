package com.jeff.teammate.mapper;

import com.jeff.teammate.model.UserWithTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {
    List<UserWithTags> selectUserWithTags(@Param("gameName")String gameName);
    String getMyTags(@Param("gameName")String gameName,@Param("userId")Long id);
    Integer updateMyTags(@Param("gameName")String gameName,@Param("userId")Long id,@Param("tags")String tags,@Param("tagsCount")int tagsCount);
    Integer insertMyTags(@Param("gameName")String gameName,@Param("userId")Long id,@Param("tags")String tags,@Param("tagsCount")int tagsCount);
}

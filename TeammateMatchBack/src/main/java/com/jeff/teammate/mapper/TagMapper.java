package com.jeff.teammate.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.jeff.teammate.model.Tag;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> selectTags();
    int addTag(int gameId,String tagName,String groupName);
    int deleteTag(int gameId,String tagName);
}





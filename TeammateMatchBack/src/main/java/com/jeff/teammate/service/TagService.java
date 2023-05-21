package com.jeff.teammate.service;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.Tag;

/**
* @author 21029
* @description 针对表【tag】的数据库操作Service
* @createDate 2023-03-12 17:48:00
*/
public interface TagService {


    List<Tag> gameTags(int gameId);

//    List<String> getGroup(String gameName);
//
//    boolean addGroup(HttpServletRequest httpServletRequest, String gameName, String groupName);
//
//    boolean deleteGroup(HttpServletRequest httpServletRequest, String gameName, String groupName);

    boolean addTag(HttpServletRequest httpServletRequest,int gameId,String tagName,String groupName);

    boolean deleteTag(HttpServletRequest httpServletRequest,int gameId,String tagName);

//    String getMyTags(String gameName,int id);
//    boolean updateMyTags(String gameName,int id,String tags);

}

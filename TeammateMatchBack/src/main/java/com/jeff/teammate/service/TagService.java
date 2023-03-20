package com.jeff.teammate.service;

import com.jeff.teammate.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 21029
* @description 针对表【tag】的数据库操作Service
* @createDate 2023-03-12 17:48:00
*/
public interface TagService extends IService<Tag> {
    public List<Tag> gameTags(String gameName);

    public List<String> getGroup(String gameName);

    public boolean addGroup(HttpServletRequest httpServletRequest, String gameName, String groupName);

    public boolean deleteGroup(HttpServletRequest httpServletRequest, String gameName, String groupName);

    public boolean addTag(String gameName,String tagName,String groupName);

    public boolean deleteTag(String gameName,String tagName);


    public String getMyTags(String gameName,long id);
    public boolean updateMyTags(String gameName,long id,String tags);

}

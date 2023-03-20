package com.jeff.teammate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.Tag;
import com.jeff.teammate.mapper.MyMapper;
import com.jeff.teammate.service.TagService;
import com.jeff.teammate.mapper.TagMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 21029
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-03-12 17:48:00
 *
 * 用户可以修改tag,管理员可以添加group
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Resource
    TagMapper tagMapper;

    @Resource
    MyMapper myMapper;

    @Override
    public List<Tag> gameTags(String gameName){
        if(gameName==null||gameName.length()==0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Tag>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gameName",gameName);
        return tagMapper.selectList(queryWrapper);
    }

    @Override
    public List<String> getGroup(String gameName){
        QueryWrapper<Tag> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gameName",gameName);
        queryWrapper.eq("isGroup",1);
        return tagMapper.selectList(queryWrapper).stream().map(Tag::getTagName).collect(Collectors.toList());
    }

    @Override
    public boolean addGroup(HttpServletRequest httpServletRequest,String gameName,String groupName){
        //权限检测
        ImplUtils.isAdmin(httpServletRequest);
        //基本检测
        if(StringUtils.isAnyBlank(gameName,groupName)||groupName.length()<2||groupName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //正则检测
        ImplUtils.hasWrongChar(groupName);
        //已有
        List<String> groups=getGroup(gameName);
        if(groups.contains(groupName)){
            throw new BusinessException(ErrorCode.REGISTERED_TAG);
        }
        //新增
        Tag tag=new Tag();
        tag.setTagName(groupName);
        tag.setGameName(gameName);
        tag.setIsGroup(1);
        tagMapper.insert(tag);
        return true;
    }

    @Override
    public boolean deleteGroup(HttpServletRequest httpServletRequest, String gameName, String groupName){
        //权限检测
        ImplUtils.isAdmin(httpServletRequest);
        //基本检测
        if(StringUtils.isAnyBlank(gameName,groupName)||groupName.length()<2||groupName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //删除
        QueryWrapper<Tag>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gameName",gameName);
        queryWrapper.eq("tagName",groupName);
        return tagMapper.delete(queryWrapper)>0;
    }

    @Override
    public boolean addTag(String gameName,String tagName,String groupName){
        //基本检测
        if(StringUtils.isAnyBlank(gameName,tagName,groupName)||tagName.length()<2||tagName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //正则检测
        ImplUtils.hasWrongChar(groupName);
        //已有
        QueryWrapper<Tag> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gameName",gameName);
        queryWrapper.eq("tagName",gameName);
        if(tagMapper.selectCount(queryWrapper)>0){
            throw new BusinessException(ErrorCode.REGISTERED_TAG);
        }
        //新增
        Tag tag=new Tag();
        tag.setTagName(tagName);
        tag.setGameName(gameName);
        tag.setGroupName(groupName);
        tag.setIsGroup(0);
        tagMapper.insert(tag);
        return true;
    }

    @Override
    public boolean deleteTag(String gameName,String tagName){
        //基本检测
        if(StringUtils.isAnyBlank(gameName,tagName)||tagName.length()<2||tagName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //无
        QueryWrapper<Tag> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gameName",gameName);
        queryWrapper.eq("tagName",tagName);
        return tagMapper.delete(queryWrapper)>0;
    }

    //返回为null时
    @Override
    public String getMyTags(String gameName,long id){
        String ret=myMapper.getMyTags(gameName,id);
        if(ret==null){
            return "\"[]\"";
        }
        return ret;
    }

    //第一次是insert
    //检查json格式/去重/算长度
    @Override
    public boolean updateMyTags(String gameName,long id,String tags){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            Set<String> userTags = objectMapper.readValue(tags, new TypeReference<Set<String>>(){});
            int tagsCount=userTags.size();
            String ret=myMapper.getMyTags(gameName,id);
            if(ret==null){
                return myMapper.insertMyTags(gameName,id,tags,tagsCount)>0;
            }
            return myMapper.updateMyTags(gameName,id,tags,tagsCount)>0;
        }catch(JsonProcessingException je){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}





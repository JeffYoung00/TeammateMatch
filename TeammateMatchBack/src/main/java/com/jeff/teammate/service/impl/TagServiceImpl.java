package com.jeff.teammate.service.impl;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.mapper.GameMapper;
import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.Tag;
import com.jeff.teammate.service.TagService;
import com.jeff.teammate.mapper.TagMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 21029
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-03-12 17:48:00
 *
 * 用户可以修改tag,管理员可以添加group
*/
@Service
public class TagServiceImpl implements TagService{

    @Resource
    TagMapper tagMapper;

    @Resource
    GameMapper gameMapper;

    //初始化game的tag list和mask
    @PostConstruct
    public void gameInit(){
        List<Tag>tags=tagMapper.selectTags();
        Game.games=gameMapper.getGames();
        //根据tags[i]的gameId, 加入相应的games[id].vector
        //todo 需要保证gameId从0自增
        for(int i=0;i<tags.size();i++){
            Game.games.get(tags.get(i).getGameId()).getTags().add(tags.get(i));
        }
        for(Game game:Game.games){
            game.initMask();
        }
    }

    @Override
    public List<Tag> gameTags(int id) {
        //基本检测
        if(id>=Game.games.size()||id<0){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Game.games.get(id).getTags();
    }

    @Override
    public boolean addTag(HttpServletRequest httpServletRequest,int gameId, String tagName, String groupName){
        //基本检测
        if(StringUtils.isAnyBlank(tagName,groupName)||tagName.length()<2||tagName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //权限检测
        ImplUtils.isAdmin(httpServletRequest);
        //正则检测
        ImplUtils.hasWrongChar(groupName);
        ImplUtils.hasWrongChar(tagName);
        //
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        //已有同名tag
        List<Tag>tagList=Game.games.get(gameId).getTags();
        for(int i=0;i<tagList.size();i++){
            if(tagName.equals(tagList.get(i).getTagName())&&!tagList.get(i).isDelete()){
                throw new BusinessException(ErrorCode.REGISTERED_TAG);
            }
        }
        //新增
        Game.games.get(gameId).insertMask();
        Tag tag=new Tag(gameId,tagName,groupName);
        tagList.add(tag);
        //数据库同步
        tagMapper.addTag(gameId,tagName,groupName);
        return true;
    }

    @Override
    public boolean deleteTag(HttpServletRequest httpServletRequest,int gameId,String tagName){
        //基本检测
        if(StringUtils.isAnyBlank(tagName)||tagName.length()<2||tagName.length()>10){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //权限检测
        ImplUtils.isAdmin(httpServletRequest);
        //
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        //
        List<Tag>tagList=Game.games.get(gameId).getTags();
        int i=0;
        for(Tag tag:tagList){
            if(tagName.equals(tag.getTagName())&&!tag.isDelete()){
                Game.games.get(gameId).deleteMask(i);
                tag.setDelete(true);
                break;
            }
            i++;
        }
        //数据库同步
        if(i<tagList.size()){
            tagMapper.deleteTag(gameId,tagName);
            return true;
        }else{
            return false;
        }
    }
}
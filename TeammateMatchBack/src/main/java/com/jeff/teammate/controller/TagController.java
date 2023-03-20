package com.jeff.teammate.controller;

import com.jeff.teammate.model.Tag;
import com.jeff.teammate.model.User;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.request.TagRequest;
import com.jeff.teammate.model.response.ItemOne;
import com.jeff.teammate.model.response.Response;
import com.jeff.teammate.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tags")
@ResponseBody
public class TagController {
    @Resource
    TagService tagService;

    @PostMapping("/group")
    public Response<Boolean> addGroup(HttpServletRequest httpServletRequest, @RequestBody TagRequest tagRequest){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success(tagService.addGroup(httpServletRequest,gameName,tagRequest.getGroupName()));
    }

    @DeleteMapping("/group")
    public Response<Boolean> deleteGroup(HttpServletRequest httpServletRequest, @RequestBody TagRequest tagRequest){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success(tagService.deleteGroup(httpServletRequest,gameName,tagRequest.getGroupName()));
    }

    @PostMapping("/tag")
    public Response<Boolean> addTag(HttpServletRequest httpServletRequest, @RequestBody TagRequest tagRequest){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success( tagService.addTag(gameName,tagRequest.getTagName(),tagRequest.getGroupName()) );
    }

    @DeleteMapping("/tag")
    public Response<Boolean> deleteTag(HttpServletRequest httpServletRequest, @RequestBody TagRequest tagRequest){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success( tagService.deleteTag(gameName,tagRequest.getTagName()) );
    }

    @GetMapping("/my")
    public Response<String> getMyTags(HttpServletRequest httpServletRequest){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        long id=((User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE)).getId();
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success(tagService.getMyTags(gameName,id));
    }

    @PostMapping("/my")
    public Response<Boolean> updateMyTags(HttpServletRequest httpServletRequest, @RequestBody String tags){
        String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
        long id=((User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE)).getId();
        if(gameName==null){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        return Response.success(tagService.updateMyTags(gameName,id,tags));
    }

    @GetMapping("/game")
    public Response<List<ItemOne>> getGameTags(HttpServletRequest httpServletRequest, @RequestParam String gameName){
        if(httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE)==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        httpServletRequest.getSession().setAttribute(UserConstant.GAME_NAME,gameName);
        List<Tag>tags=tagService.gameTags(gameName);
        List<ItemOne>items=ItemOne.ItemOnes(tags);
        return Response.success(items);
    }

}

package com.jeff.teammate.controller;

import com.jeff.teammate.model.Tag;
import com.jeff.teammate.model.request.TagRequest;
import com.jeff.teammate.model.response.Response;
import com.jeff.teammate.model.response.TagListResponse;
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

    @PostMapping("/tag")
    public Response<Boolean> addTag(HttpServletRequest httpServletRequest,@RequestBody TagRequest tagRequest){
        return Response.success( tagService.addTag(httpServletRequest,tagRequest.getGameId(),tagRequest.getTagName(),tagRequest.getGroupName()) );
    }

    @DeleteMapping("/tag")
    public Response<Boolean> deleteTag(HttpServletRequest httpServletRequest,@RequestBody TagRequest tagRequest){
        return Response.success( tagService.deleteTag(httpServletRequest,tagRequest.getGameId(), tagRequest.getTagName()) );
    }

    @GetMapping("/game")
    public Response<TagListResponse[]> getGameTags( @RequestParam int gameId){
        List<Tag>tags=tagService.gameTags(gameId);
        TagListResponse[]items=TagListResponse.parse(tags);
        return Response.success(items);
    }

}

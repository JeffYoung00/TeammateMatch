package com.jeff.teammate.controller;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.User;
import com.jeff.teammate.model.request.TeamJoinRequest;
import com.jeff.teammate.model.request.TeamRequest;
import com.jeff.teammate.model.response.Response;
import com.jeff.teammate.model.response.SafeTeam;
import com.jeff.teammate.model.response.TeamAndMembers;
import com.jeff.teammate.service.TeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    TeamService teamService;

    @PostMapping("/create")
    public Response<Boolean> createMyTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest httpServletRequest){
        if(teamRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(teamRequest.getGameId()==null||teamRequest.getMaxSize()==null||teamRequest.getIsPublic()==null||
        teamRequest.getTeamName()==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return Response.success(teamService.createMyTeam(user, teamRequest.getGameId(), teamRequest.getTeamName(), teamRequest.getMaxSize(),
                teamRequest.getIsPublic(), teamRequest.getPassword(), teamRequest.getDescription(), teamRequest.getTags(),teamRequest.getLogo()));
    }
    @PostMapping("/disband")
    public Response<Boolean> disbandMyTeam(HttpServletRequest httpServletRequest){
        return Response.success(teamService.disbandMyTeam((User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE)));
    }
    @PostMapping("/join")
    public Response<Boolean> joinTeam(@RequestBody TeamJoinRequest request, HttpServletRequest httpServletRequest){
        if(request.getTeamId()==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return Response.success(teamService.joinTeam(user,request.getTeamId(),request.getPassword()));
    }

    @PostMapping("/leave")
    public Response<Boolean> leaveTeam(HttpServletRequest httpServletRequest){
        return Response.success(teamService.leaveTeam((User) httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE)));
    }

    @PostMapping("/kick")
    public Response<Boolean> kickOut(@RequestParam Integer userId,HttpServletRequest httpServletRequest){
        if(userId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Response.success(teamService.kickOut((User) httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE),userId));
    }

//    @PostMapping("/change")
//    public Response<Boolean> changeOwner(@RequestBody TeamRequest teamRequest){
//        return Response.success(teamService.changeOwner())
//
//    }
    @PostMapping("/recommend")
    public Response<List<SafeTeam>> recommendTeams(@RequestParam Integer gameId, HttpServletRequest httpServletRequest){
        if(gameId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        Map<Integer,byte[]>map=(Map<Integer, byte[]>) httpServletRequest.getSession().getAttribute(UserConstant.GAME_TAG_LIST);
        return Response.success(teamService.recommendTeams(user,gameId,map));

    }
    @PostMapping("/search")
    public Response<List<SafeTeam>> searchTeams(@RequestParam String teamName,@RequestParam Integer gameId){
        if(gameId==null||teamName==null||teamName.length()<2){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Response.success(teamService.searchTeamByName(gameId,teamName));
    }

    @PostMapping("/my")
    public Response<TeamAndMembers> myTeam(HttpServletRequest httpServletRequest){
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return Response.success(teamService.myTeamAndMembers(user));
    }
}

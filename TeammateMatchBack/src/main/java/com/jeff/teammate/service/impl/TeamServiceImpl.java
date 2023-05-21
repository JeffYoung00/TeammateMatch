package com.jeff.teammate.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.mapper.TeamMapper;
import com.jeff.teammate.mapper.UserMapper;
import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.Team;
import com.jeff.teammate.model.User;
import com.jeff.teammate.model.response.SafeTeam;
import com.jeff.teammate.model.response.SafeUser;
import com.jeff.teammate.model.response.TeamAndMembers;
import com.jeff.teammate.service.TeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Resource
    TeamMapper teamMapper;
    @Resource
    UserMapper userMapper;

    Gson gson=new Gson();

    private final int searchLen=10;

    public String addMemberJson(Team team,int userId){
        String oldJson=team.getUserIdList();
        List<Integer>userIdList=gson.fromJson(oldJson,new TypeToken<List<Integer>>(){}.getType());
        userIdList.add(userId);
        return gson.toJson(userIdList);
    }

    //不在队伍中会返回null
    public String deleteMemberJson(Team team,int userId){
        String oldJson=team.getUserIdList();
        List<Integer>userIdList=gson.fromJson(oldJson,new TypeToken<List<Integer>>(){}.getType());
        int index = userIdList.indexOf(userId);
        if(index==-1){
            throw new BusinessException(ErrorCode.NOT_MEMBER);
        }
        userIdList.remove(index);
        return gson.toJson(userIdList);
    }

    @Transactional
    @Override
    public boolean createMyTeam(User currentUser,int gameId, String teamName, int maxSize, boolean isPublic,
                                String password, String description, List<Integer> tags, String logo){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //参数检测
        if(teamName==null||teamName.length()<2||teamName.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(maxSize<2||maxSize>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //private必须要有password
        if(!isPublic&&password==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //用户已加入一个team
        if(userMapper.getTeamId(currentUser.getId())!=null){
            throw new BusinessException(ErrorCode.JOINED_TEAM);
        }
        //
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.GAME_NOT_FOUNT);
        }
        //Team对象
        Team team=new Team(gameId,currentUser.getId(), teamName,maxSize);
        team.setPublic(isPublic);
        team.setDescription(description);
        team.setLogo(logo);
        //md5加密
        team.setPassword(DigestUtils.md5DigestAsHex((UserConstant.salt+password).getBytes()));
        //如果选择了tags
        if(tags!=null&&tags.size()!=0){
            team.setTags(ImplUtils.idListToMaskByte(tags));
        }
        //执行逻辑
        teamMapper.addTeam(team);
        if(userMapper.setTeamId(currentUser.getId(),team.getId())==0){
            throw new BusinessException(ErrorCode.FAIL_TO_ADD);
        }
        //返回
        return true;
    }

    @Transactional
    @Override
    public boolean disbandMyTeam(User currentUser){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //权限检测
        Integer currentUserTeamId= userMapper.getTeamId(currentUser.getId());
        if(currentUserTeamId==null){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        Team team = teamMapper.getTeamById(currentUserTeamId);
        if(team.getOwnerId()!=currentUser.getId()){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        //成功执行逻辑
        String oldJson=team.getUserIdList();
        List<Integer>userIdList=gson.fromJson(oldJson,new TypeToken<List<Integer>>(){}.getType());
        for(Integer userId: userIdList){
            if(userMapper.deleteTeamId(userId)==0){
                throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
            }
        }
        //**ownerId不在userIdList中
        if(userMapper.deleteTeamId(team.getOwnerId())==0){
            throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
        }

        if(teamMapper.deleteTeamById(currentUserTeamId)==0){
            throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
        }
        return true;
    }

    @Override
    public boolean joinTeam(User currentUser,int teamId,String password){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        synchronized (this){
            //已经加入队伍
            if(userMapper.getTeamId(currentUser.getId())!=null){
                throw new BusinessException(ErrorCode.JOINED_TEAM);
            }
            //队伍人数已经满
            Team team = teamMapper.getTeamById(teamId);
            if(team==null){
                throw new BusinessException(ErrorCode.TEAM_NOT_FOUND);
            }
            if(team.getCurrentSize()==team.getMaxSize()){
                throw new BusinessException(ErrorCode.FULL_TEAM);
            }
            //密码错误
            String encryption=DigestUtils.md5DigestAsHex((UserConstant.salt+password).getBytes());
            if(!Objects.equals(team.getPassword(), encryption)){
                throw new BusinessException(ErrorCode.WRONG_PASSWORD);
            }
            //成功执行逻辑
            teamMapper.addOne(teamId,addMemberJson(team,currentUser.getId()));
                //...逻辑上不会set失败
            userMapper.setTeamId(currentUser.getId(),teamId);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean leaveTeam(User currentUser){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //未加入队伍
        Integer currentUserTeamId=userMapper.getTeamId(currentUser.getId());
        if(currentUserTeamId==null){
            throw new BusinessException(ErrorCode.NOT_MEMBER);
        }

        //成功执行逻辑
        Team team=teamMapper.getTeamById(currentUserTeamId);
        if(team==null){
            throw new BusinessException(ErrorCode.TEAM_NOT_FOUND);
        }
        if(team.getOwnerId()!=currentUser.getId()){
            //自己并非owner
            if(teamMapper.minusOne(team.getId(),deleteMemberJson(team,currentUser.getId()))==0){
                throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
            }
            if(userMapper.deleteTeamId(currentUser.getId())==0){
                throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
            }
        }else if(team.getCurrentSize()==1){
            //自己是owner,且当前队伍即将解散
            if(teamMapper.deleteTeamById(currentUserTeamId)==0){
                throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
            }
            if(userMapper.deleteTeamId(currentUser.getId())==0){
                throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
            }
        }else{
            //转移owner权限,owner换给第一个加入的人
            String oldJson=team.getUserIdList();
            List<Integer>userIdList=gson.fromJson(oldJson,new TypeToken<List<Integer>>(){}.getType());
            int newOwnerId=userIdList.remove(0);
            teamMapper.ownerLeft(currentUserTeamId,newOwnerId, gson.toJson(userIdList));
            userMapper.deleteTeamId(currentUser.getId());
        }
        return true;
    }

    @Transactional
    @Override
    public boolean kickOut(User currentUser,int userId){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //权限检测
        Integer userTeamId=userMapper.getTeamId(currentUser.getId());
        if(userTeamId==null){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        Team team = teamMapper.getTeamById(userTeamId);
        //...理论上这里不会为null
        if(team==null){
        }
        if(team.getOwnerId()!=currentUser.getId()){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        //该id不能是自己
        if(currentUser.getId()==userId){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //检测是否在队伍中
        String newJson=deleteMemberJson(team,userId);
        //成功执行逻辑
        if(userMapper.deleteTeamId(userId)==0){
            throw new BusinessException(ErrorCode.FAIL_TO_DELETE);
        }
        teamMapper.minusOne(userTeamId,newJson);
        return true;
    }


    @Override
    public boolean changeOwner(User currentUser){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //权限检测
        Integer currentUserTeamId= userMapper.getTeamId(currentUser.getId());
        if(currentUserTeamId==null){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        Team team = teamMapper.getTeamById(currentUserTeamId);
        if(team.getOwnerId()!=currentUser.getId()){
            throw new BusinessException(ErrorCode.NOT_OWNER);
        }
        //成功执行逻辑
        String oldJson=team.getUserIdList();
        List<Integer>userIdList=gson.fromJson(oldJson,new TypeToken<List<Integer>>(){}.getType());
        int newOwnerId=userIdList.remove(0);
        userIdList.add(team.getOwnerId());
        teamMapper.ownerChanged(currentUserTeamId,newOwnerId,gson.toJson(userIdList));
        return true;
    }

    @Override
    public TeamAndMembers myTeamAndMembers(User currentUser){
        //登录检测
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //未加入队伍
        Integer currentUserTeamId=userMapper.getTeamId(currentUser.getId());
        if(currentUserTeamId==null){
            throw new BusinessException(ErrorCode.NOT_MEMBER);
        }
        //
        Team team=teamMapper.getTeamById(currentUserTeamId);
        TeamAndMembers ret=new TeamAndMembers();
        ret.setTeam(new SafeTeam(team));
        //leader
        User leader = userMapper.selectById(team.getOwnerId());
        ret.setLeader(new SafeUser(leader));
        //members
        List<Integer>memberIdList=gson.fromJson(team.getUserIdList(),new TypeToken<List<Integer>>(){}.getType());
        if(!memberIdList.isEmpty()){
            List<User> members = userMapper.selectBatchIds(memberIdList);
            ret.setMembers(members.stream().map(SafeUser::new).collect(Collectors.toList()));
        }
        return ret;
    }

    @Override
    public List<SafeTeam> recommendTeams(User currentUser, int gameId, Map<Integer,byte[]> currentUserTags){
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        byte[]userTags= currentUserTags.get(gameId);
        if(userTags==null) {
            List<Team> teams = teamMapper.getLimitTeams(gameId,searchLen);
            return teams.stream().map(SafeTeam::new).collect(Collectors.toList());
        }
        List<Team>teams=teamMapper.getPublicTeams(gameId);
        List<SafeTeam>ret=new ArrayList<>(searchLen);

        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Game game=Game.games.get(gameId);
        int gameTagSize=(game.getTags().size()+7)/8;

        int i=0;
        for(Team team:teams){
            //找自己符合标签的队伍
            //队伍标签可能为null
            if(team.getTags()==null||
                    ImplUtils.matchRequirement(userTags,team.getTags(),game.getMask(),gameTagSize)){
                ret.add(new SafeTeam(team));
                i++;
                if(i==searchLen){
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public List<SafeTeam> searchTeamByName(int gameId,String teamName){
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return teamMapper.getTeamByNameLimit(gameId,teamName,searchLen).stream().map(SafeTeam::new).collect(Collectors.toList());
    }

}

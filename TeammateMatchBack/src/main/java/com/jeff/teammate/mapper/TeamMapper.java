package com.jeff.teammate.mapper;

import com.jeff.teammate.model.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {
    int addTeam(Team team);
    int deleteTeamById(int id);
    Team getTeamById(int id);
    List<Team> getTeamByNameLimit(int gameId,String teamName,int limit);
    List<Team> getAllTeams();

    List<Team> getLimitTeams(int gameId,int limit);
    List<Team> getPublicTeams(int gameId);

    int ownerChanged(int id,int newOwnerId,String userIdList);
    int ownerLeft(int id,int newOwnerId,String userIdList);

    int minusOne(int id,String userIdList);
    int addOne(int id,String userIdList);
}

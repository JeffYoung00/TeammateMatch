package com.jeff.teammate.mapper;

import com.jeff.teammate.model.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeamMapperTest {

//    int addTeam(Team team);
//    int deleteTeamById(int id);
//    Team getTeamById(int id);
//    List<Team> getTeamByNameLimit(String teamName,int limit);
//    List<Team> getAllTeams();
//
//    List<Team> getLimitTeams(int gameId,int limit);
//    List<Team> getPublicTeams(int gameId);
//
//    int ownerChanged(int id,int newOwnerId,String userIdList);
//    int ownerLeft(int id,int newOwnerId,String userIdList);
//
//    int minusOne(int id,String userIdList);
//    int addOne(int id,String userIdList);

    @Autowired
    TeamMapper mapper;
    @Test
    public void addTeam() {

        int maxSize=10;
        boolean isPublic=true;
        String password="testtest";
        String description="LPL最新银河战舰";
        byte[] tags=new byte[32];

        String teamLogo="logo1";
        int start=0;
        for(int i=0;i<50;i++){
            int id=start+i;
            int ownerId=id;
            String teamName="test_team_"+id;

            Team team=new Team(0,ownerId,teamName,maxSize);
            team.setPublic(isPublic);
            team.setPassword(password);
            team.setLogo(teamLogo);
            team.setDescription(description);
            if(i>5){
                tags[0]=16;
            }
            team.setTags(tags);
            mapper.addTeam(team);
        }

    }

    @Test
    public void deleteTeamById() {
        mapper.deleteTeamById(2);
    }

    @Test
    public void getAllTeams() {
        List<Team> allTeams = mapper.getAllTeams();
        System.out.println(allTeams);
    }

    @Test
    public void getTeamById() {
        Team team = mapper.getTeamById(5);
        System.out.println(team);

    }

    @Test
    public void getTeamByNameLimit() {
        mapper.addOne(51,"[20]");
    }

    @Test
    public void getLimitTeams() {
    }

    @Test
    public void getPublicTeams() {
    }

    @Test
    public void ownerChanged() {
    }

    @Test
    public void ownerLeft() {
    }

    @Test
    public void minusOne() {
    }

    @Test
    public void addOne() {
    }
}
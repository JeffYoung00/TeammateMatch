package com.jeff.teammate.model.response;

import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 去掉了password,members,tags
 */
@Getter
@Setter
public class SafeTeam {
    private int id;
    private int gameId;
    private int currentSize;
    private Date createTime;
    private String teamName;
    private int maxSize;


    private boolean isPublic;
    private String description;
    private String logo;

    private int ownerId;

    public SafeTeam(Team team){
        this.id=team.getId();
        this.gameId= team.getGameId();
        this.currentSize=team.getCurrentSize();
        this.createTime=team.getCreateTime();
        this.teamName=team.getTeamName();
        this.maxSize=team.getMaxSize();
        this.isPublic=team.isPublic();
        this.description=team.getDescription();
        this.logo=team.getLogo();
    }

}

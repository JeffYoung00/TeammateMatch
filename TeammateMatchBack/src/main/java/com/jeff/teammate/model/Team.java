package com.jeff.teammate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Team {
    //主键
    private Integer id;
    //不需要
    private int currentSize;
    private Date createTime;
    private String userIdList;
    //必要
    private int gameId;
    private int ownerId;
    private String teamName;
    private int maxSize;
    //可选
    private boolean isPublic;
    private String password;
    private String description;
    private byte[] tags;
    private String logo;

    public  Team(){}

    public Team(int gameId,int ownerId,String teamName,int maxSize){
        this.gameId=gameId;
        this.ownerId=ownerId;
        this.teamName=teamName;
        this.maxSize=maxSize;

        //不被int初始值为0影响
        this.currentSize=1;
    }
}

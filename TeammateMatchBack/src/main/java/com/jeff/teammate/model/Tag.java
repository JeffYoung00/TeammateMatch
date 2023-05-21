package com.jeff.teammate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Tag implements Serializable {
    private Integer gameId;
    private String tagName;
    private String groupName;
    private boolean isDelete;

    public Tag(){}

    public Tag(int gameId,String tagName,String groupName){
        this.gameId=gameId;
        this.tagName=tagName;
        this.groupName=groupName;
        this.isDelete=false;
    }

    private static final long serialVersionUID = 1L;
}

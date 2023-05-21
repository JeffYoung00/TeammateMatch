package com.jeff.teammate.model;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.exception.BusinessException;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class Game implements Serializable {

    public static List<Game> games;

    public final static int maxMask=256;

    private Integer id;

    private String name;

    transient private byte[] mask;

    //all tags
    transient private final List<Tag> tags;

    //group name to tag list
    //transient private final Map<String, List<Tag>> map;

    private static final long serialVersionUID = 0L;

    public Game(){
        tags=new ArrayList<>();
        //map=new HashMap<>();
        mask=new byte[maxMask/8];
    }

//    public void generateMap(){
//        for(int i=0;i<vector.size();i++){
//            Tag tag=vector.get(i);
//            if(!tag.isDelete()){
//                insertTag(tag);
//            }
//        }
//    }

    public void initMask(){
        for(int i=0;i<tags.size();i++){
            if(tags.get(i).isDelete()){
                continue;
            }
            int index=i/8;
            int mod=i%8;
            mask[index]|=1<<mod;
        }
    }

    public void insertMask() {
        if(tags.size()>=maxMask){
            throw new BusinessException(ErrorCode.FULL_TABLE);
        }
        int pos=tags.size();
        int index=pos/8;
        int mod=pos%8;
        mask[index]|=1<<mod;
    }

    public void deleteMask(int pos){
        int index=pos/8;
        int mod=pos%8;
        mask[index]&=~(1<<mod);
    }
}

package com.jeff.teammate.service.impl;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImplUtils {
    /**
     * 权限检测
     * @param httpServletRequest
     * @return
     */
    public static boolean isAdmin(HttpServletRequest httpServletRequest){
        Object obj=httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        //成功登录后才会将user信息存入Session
        if(obj==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User user=(User)obj;
        if(user.getUserRole()!=UserConstant.Administrator){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return true;
    }

    public static boolean hasWrongChar(String str){
        String invalid="[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern=Pattern.compile(invalid);
        Matcher matcherName=pattern.matcher(str);
        if(matcherName.find()){
            throw new BusinessException(ErrorCode.WRONG_CHAR);
        }
        return true;
    }

    public static byte[] idListToMaskByte(List<Integer> idList){
        byte[] mask=new byte[Game.maxMask/8];
        for(int i=0;i<idList.size();i++){
            int index=idList.get(i)/8;
            int mod=idList.get(i)%8;
            //在mask里加上每一个bit
            mask[index]|=1<<mod;
        }
        return mask;
    }

    public static List<Integer> maskByteToIdList(byte[]mask){
        List<Integer> tagIdList=new ArrayList<>();
        for(int i = 0; i< Game.maxMask; i++){
            int index=i/8;
            int mod=i%8;
            //此tag存在
            if((mask[index]&(1<<mod))!=0){
                tagIdList.add(i);
            }
        }
        return tagIdList;
    }

    //O(游戏tag长度)
    public static double calculateCorrelation(byte[]inputVec,byte[]userVec,byte[]mask,int gameTagSize){
        int count2=0,mul=0;
//        for(int i=0;i<gameTagSize;i++){
//            int b=vec1[i]&mask[i]&0xff;
//            for(int j=0;j<8;j++){
//                count1+=(b>>j)&0x01;
//            }
//        }
        for(int i=0;i<gameTagSize;i++){
            int b=userVec[i]&mask[i]&0xff;
            for(int j=0;j<8;j++){
                count2+=(b>>j)&0x01;
            }
        }
        //防止0作为分母
        if(count2==0){
            return 0;
        }
        for(int i=0;i<gameTagSize;i++){
            int b=inputVec[i]&userVec[i]&mask[i]&0xff;
            for(int j=0;j<8;j++){
                mul+=(b>>j)&0x01;
            }
        }
        //所有结果都需要 开根号 ÷count1
        return (double)mul*mul / count2;
    }

    public static boolean matchRequirement(byte[]userVec,byte[]teamVec,byte[]mask,int gameTagSize){
        for(int i=0;i<gameTagSize;i++){
            if(!((userVec[i]&teamVec[i]&mask[i])==(teamVec[i]&mask[i]))){
                return false;
            }
        }
        return true;
    }

    //需要vec1,vec2本身有序;mask?
    //O(用户tag长度)
//    public static double calculateCorrelation(List<Integer>vec1,List<Integer>vec2){
//        int len1=vec1.size();
//        int len2=vec2.size();
//        int i=0,j=0,count=0;
//        while(i<len1&&j<len2){
//            int num1=vec1.get(i);
//            int num2=vec2.get(j);
//            if(num1==num2){
//                i++;
//                j++;
//                count++;
//            }else if(num1<num2){
//                i++;
//            }else{
//                j++;
//            }
//        }
//        return (double)count*count/(len1*len2);
//    }
}

package com.jeff.teammate.service.impl;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.User;

import javax.servlet.http.HttpServletRequest;
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
        if(user.getUserRole()==null||user.getUserRole()!=UserConstant.Administrator){
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
}

package com.jeff.teammate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.User;
import com.jeff.teammate.mapper.MyMapper;
import com.jeff.teammate.model.UserWithTags;
import com.jeff.teammate.model.request.UserEditRequest;
import com.jeff.teammate.model.response.SafeUser;
import com.jeff.teammate.service.UserService;
import com.jeff.teammate.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
* @author 21029
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-06 22:22:20
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    UserMapper userMapper;
    @Resource
    MyMapper myMapper;

    final String salt="Jeff";
    @Override
    public long userRegister(String userName, String password, String checkPassword) {

        //基本判断
        if(StringUtils.isAnyBlank(userName,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(userName.length()<2||userName.length()>12){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(password.length()<8||password.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //特殊字符判断
        ImplUtils.hasWrongChar(userName);
        //username已注册判断
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userName",userName);
        if(this.count(queryWrapper)>0){
            throw new BusinessException(ErrorCode.REGISTERED);
        }
        //密码加密
        String encryption= DigestUtils.md5DigestAsHex( (salt+password).getBytes() );
        //注册用户//应该使用加密后的密码
        User newUser=new User();
        newUser.setUserName(userName);
        newUser.setUserPassword(encryption);
        boolean save = this.save(newUser);
        //防止返回null
        if(!save){
            throw new BusinessException(ErrorCode.OPERATION_FAILED);
        }
        return newUser.getId();
    }

    @Override
    public SafeUser userLogin(String userName, String password, HttpServletRequest httpServletRequest) {
        //基本判断
        if(StringUtils.isAnyBlank(userName,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(userName.length()<2||userName.length()>12){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(password.length()<8||password.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //特殊字符判断
        String invalid="[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern=Pattern.compile(invalid);
        Matcher matcherName=pattern.matcher(userName);
        if(matcherName.find()){
            throw new BusinessException(ErrorCode.WRONG_CHAR);
        }
        //密码加密
        String encryption= DigestUtils.md5DigestAsHex( (salt+password).getBytes() );
        //登录
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userName",userName);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }//密码错误
        else if(!user.getUserPassword().equals(encryption)){
            throw new BusinessException(ErrorCode.WRONG_PASSWORD);
        }
        //改变状态
        user.setUserState(1);
        userMapper.updateById(user);
        //存进Session,本地不需要脱敏
        httpServletRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,user);
        //信息脱敏
        SafeUser safeUser=new SafeUser(user);
        //返回给前端
        return safeUser;
    }

    @Override
    public boolean deleteUser(long id, HttpServletRequest httpServletRequest) {
        //权限检测
        if(!ImplUtils.isAdmin(httpServletRequest)){
            return false;
        }
        //改变状态
        User user=(User) httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        user.setUserState(2);
        userMapper.updateById(user);
        return true;
    }

    @Override
    public List<SafeUser> searchUsersNameLike(String userName, HttpServletRequest httpServletRequest) {
        if(StringUtils.isBlank(userName)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("userName",userName);
        return this.list(queryWrapper).stream().map(SafeUser::new).collect(Collectors.toList());
    }



    @Override
    public void userLogout(HttpServletRequest request){
        Object obj=request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(obj==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User user=(User)obj;
        user.setUserState(0);
        userMapper.updateById(user);
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
    }

    @Override
    public boolean userEdit(HttpServletRequest httpServletRequest,UserEditRequest user){
        User oldUser=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(oldUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //基本判断
        if(StringUtils.isBlank(user.getUserName())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(user.getUserName().length()<2||user.getUserName().length()>12){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(!(user.getGender().equals("")||user.getGender().equals("male")||user.getGender().equals("female"))){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(user.getEmail().length()>20||user.getPhone().length()>12){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //特殊字符判断
        String invalid="[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern=Pattern.compile(invalid);
        Matcher matcherName=pattern.matcher(user.getUserName());
        if(matcherName.find()){
            throw new BusinessException(ErrorCode.WRONG_CHAR);
        }
        //用户名被使用,注意不变的情况
        if(!user.getUserName().equals(oldUser.getUserName())){
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("userName",user.getUserName());
            if(this.count(queryWrapper)>0){
                throw new BusinessException(ErrorCode.REGISTERED);
            }
        }
        //修改
        oldUser.setUserName(user.getUserName());
        oldUser.setGender(user.getGender());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        return userMapper.updateById(oldUser)>0;
    }


    @Override
    public List<SafeUser> searchUserByTags(HttpServletRequest httpServletRequest,List<String>tags) {
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            if(CollectionUtils.isEmpty(tags)){
                throw new BusinessException(ErrorCode.EMPTY_REQUEST);
            }
            String gameName=(String)httpServletRequest.getSession().getAttribute(UserConstant.GAME_NAME);
            if(gameName==null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            List<UserWithTags>users=myMapper.selectUserWithTags(gameName).stream().
                    filter(user->user.getTagsCount()>tags.size()).
                    sorted( (user1,user2)->user1.getTagsCount()-user2.getTagsCount() ).collect(Collectors.toList());
            List<SafeUser>selectUser=new ArrayList<>();
            for(UserWithTags user :users){
                Set<String> userTags = objectMapper.readValue(user.getTags(), new TypeReference<Set<String>>(){});
                boolean flag=true;
                if(CollectionUtils.isEmpty(userTags)){
                    flag=false;
                }else {
                    for(String aTag:tags){
                        if(!userTags.contains(aTag)){
                            flag=false;
                            break;
                        }
                    }
                }
                if(flag){
                    selectUser.add(new SafeUser(user));
                }
            }
            return selectUser;
        }catch(JsonProcessingException je){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

//    private List<User> searchUserByTagsInSql(List<String>tags,int state,int gender){
//        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("userState",state);
//        queryWrapper.eq("gender",gender);
//        for(String tag:tags){
//            queryWrapper.like("tags",tag);
//        }
//        return userMapper.selectList(queryWrapper);
//    }
}





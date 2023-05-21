package com.jeff.teammate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.Game;
import com.jeff.teammate.model.User;
import com.jeff.teammate.model.request.UserEditRequest;
import com.jeff.teammate.model.response.SafeUser;
import com.jeff.teammate.service.UserService;
import com.jeff.teammate.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    Gson gson=new Gson();

    private final int searchLen=20;

    @Override
    public int userRegister(String userName, String password, String checkPassword) {

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
        String encryption= DigestUtils.md5DigestAsHex( (UserConstant.salt+password).getBytes() );
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
        String encryption= DigestUtils.md5DigestAsHex( (UserConstant.salt+password).getBytes() );
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
        //将tag存进Session
        Map<Integer,byte[]>gamesVec=gson.fromJson(user.getVector(),
                new TypeToken<Map<Integer,byte[]>>(){}.getType());
        httpServletRequest.getSession().setAttribute(UserConstant.GAME_TAG_LIST,gamesVec);
        //信息脱敏
        //返回给前端
        return new SafeUser(user);
    }

    @Override
    public boolean deleteUser(int id, HttpServletRequest httpServletRequest) {
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
        request.getSession().removeAttribute(UserConstant.GAME_TAG_LIST);
    }

    @Override
    public SafeUser userEdit(HttpServletRequest httpServletRequest,UserEditRequest user){
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
        if(!(user.getGender()==null||user.getGender().equals("male")||user.getGender().equals("female"))){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(user.getEmail()!=null&&user.getEmail().length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(user.getPhone()!=null&&user.getPhone().length()>12){
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
        userMapper.updateById(oldUser);
        return new SafeUser(oldUser);
    }


    @Override
    public List<Integer> getMyTags(HttpServletRequest httpServletRequest,int gameId){
        //登录状态检测
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //返回tagIdList
        Map<Integer,byte[]>map=(Map<Integer, byte[]>) httpServletRequest.getSession().getAttribute(UserConstant.GAME_TAG_LIST);
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        byte[]bytes=map.get(gameId);
        System.out.println(bytes.length);
        if(bytes==null){
            return new ArrayList<>();
        }
        byte[]mask=Game.games.get(gameId).getMask();
        for(int i=0;i<bytes.length;i++){
            bytes[i]&=mask[i];
        }
        return ImplUtils.maskByteToIdList(bytes);
    }

    @Override
    public boolean updateMyTags(HttpServletRequest httpServletRequest,int gameId,List<Integer>tagIdList){
        //登录状态检测
        User user=(User)httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //
        if(gameId>=Game.games.size()||gameId<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Map<Integer,byte[]>map=(Map<Integer, byte[]>) httpServletRequest.getSession().getAttribute(UserConstant.GAME_TAG_LIST);
        map.put(gameId,ImplUtils.idListToMaskByte(tagIdList));
        //数据库同步
        user.setVector(gson.toJson(map));
        userMapper.updateById(user);
        return true;
    }

    private class Pair{
        double correlation;
        User user;

        public double getCorrelation() {
            return correlation;
        }
    }

    @Override
    public List<SafeUser> searchUserByTags(int gameId,List<Integer>tagIdList){
        //检查搜索是否empty
        if(tagIdList.isEmpty()){
            throw new BusinessException(ErrorCode.EMPTY_REQUEST);
        }
        if(gameId<0||gameId>Game.games.size()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //找到所有在线的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("userState",1);
        List<User> userList = userMapper.selectList(queryWrapper);
        //计算用户的向量
        byte[]input=ImplUtils.idListToMaskByte(tagIdList);
        int gameTagSize=(Game.games.get(gameId).getTags().size()+7)/8;
        byte[]mask=Game.games.get(gameId).getMask();
        List<Pair> pairs=new ArrayList<>();
        for(User user:userList){
            Map<Integer,byte[]>gamesVec=gson.fromJson(user.getVector(),
                    new TypeToken<Map<Integer,byte[]>>(){}.getType());
            byte[]vector=gamesVec.get(gameId);
            if(vector==null){
                continue;
            }
            double correlation=ImplUtils.calculateCorrelation(input,vector,mask,gameTagSize);
            Pair p=new Pair();
            p.user=user;
            p.correlation=correlation;
            pairs.add(p);
        }
        //根据相关度排序并脱敏返回
        pairs.sort(Comparator.comparingDouble(Pair::getCorrelation).reversed());
        List<SafeUser>ret=new ArrayList<>();
        int i=0;
        for(Pair p:pairs){
            if(i>=searchLen||p.correlation==0){
                break;
            }
            ret.add( new SafeUser(p.user) );
            i++;
        }
        return ret;
    }
}





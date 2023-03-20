package com.jeff.teammate.controller;

import com.jeff.teammate.model.User;
import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.constant.UserConstant;
import com.jeff.teammate.exception.BusinessException;
import com.jeff.teammate.model.request.SearchTagRequest;
import com.jeff.teammate.model.request.UserEditRequest;
import com.jeff.teammate.model.request.UserLoginRequest;
import com.jeff.teammate.model.request.UserRegisterRequest;
import com.jeff.teammate.model.response.Response;
import com.jeff.teammate.model.response.SafeUser;
import com.jeff.teammate.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Resource
    UserService userService;////??

    @PostMapping("/register")
    public Response<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        return Response.success(userService.userRegister(userRegisterRequest.getUserName(),
                userRegisterRequest.getPassword(), userRegisterRequest.getCheckPassword()));
    }

    @PostMapping("/login")
    public Response<SafeUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        return Response.success(userService.userLogin(userLoginRequest.getUserName(), userLoginRequest.getPassword(),httpServletRequest));
    }

    @PostMapping("/current")
    public Response<SafeUser> userLogin(HttpServletRequest httpServletRequest){
        Object user=httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return Response.success( new SafeUser((User)user) );
    }

    @PostMapping("/name/{userName}")
    public Response<List<SafeUser>> searchUsers(@PathVariable String userName,HttpServletRequest httpServletRequest){
        if(userName==null){
            return null;
        }
        return Response.success(userService.searchUsersNameLike(userName,httpServletRequest));
    }

    @DeleteMapping("/id/{id}")
    public Response<Boolean> deleteUser(@PathVariable long id, HttpServletRequest httpServletRequest){
        return Response.success(userService.deleteUser(id,httpServletRequest));
    }

    @PostMapping("/logout")
    public Response<Object> userLogout(HttpServletRequest httpServletRequest){
        if(httpServletRequest==null){
            return Response.error(ErrorCode.NOT_LOGIN);
        }
        userService.userLogout(httpServletRequest);
        return Response.success(null);
    }

    @PostMapping(("/edit"))
    public Response<Boolean> userEdit(@RequestBody UserEditRequest user ,HttpServletRequest httpServletRequest){
        return Response.success(userService.userEdit(httpServletRequest,user));
    }

    @PostMapping("/tags")
    public Response<List<SafeUser>> searchUserByTags(@RequestBody SearchTagRequest tags, HttpServletRequest httpServletRequest){
        return Response.success(userService.searchUserByTags(httpServletRequest,tags.getTags()));
    }
}

package com.jeff.teammate.service;

import com.jeff.teammate.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeff.teammate.model.request.UserEditRequest;
import com.jeff.teammate.model.response.SafeUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 21029
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-06 22:22:20
*/
public interface UserService extends IService<User> {

    long userRegister(String userName,String password,String checkPassword);

    SafeUser userLogin(String userName, String password, HttpServletRequest httpServletRequest);

    List<SafeUser> searchUsersNameLike(String userName, HttpServletRequest httpServletRequest);

    boolean deleteUser(long id,HttpServletRequest httpServletRequest);

    void userLogout(HttpServletRequest request);

    boolean userEdit(HttpServletRequest httpServletRequest, UserEditRequest user);

    List<SafeUser> searchUserByTags(HttpServletRequest httpServletRequest,List<String> tags);
}

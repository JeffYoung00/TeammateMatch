package com.jeff.teammate.model.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String userName;
    private String password;
    private String checkPassword;
}

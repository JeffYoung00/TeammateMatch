package com.jeff.teammate.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userName;
    private String password;
}

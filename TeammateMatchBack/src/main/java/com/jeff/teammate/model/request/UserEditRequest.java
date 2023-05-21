package com.jeff.teammate.model.request;

import lombok.Data;

@Data
public class UserEditRequest {
    private String userName;
    private String gender;
    private String email;
    private String phone;
}

package com.jeff.teammate.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamJoinRequest {
    Integer teamId;
    String password;
}

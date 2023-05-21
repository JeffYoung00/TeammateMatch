package com.jeff.teammate.model.request;

import com.jeff.teammate.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TeamRequest {
    Integer gameId;
    String teamName;
    Integer maxSize;
    Boolean isPublic;
    String password;
    String description;
    List<Integer> tags;
    String logo;
}

package com.jeff.teammate.model.response;

import com.jeff.teammate.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamAndMembers {
    private SafeTeam team;
    private SafeUser leader;
    private List<SafeUser> members;
}

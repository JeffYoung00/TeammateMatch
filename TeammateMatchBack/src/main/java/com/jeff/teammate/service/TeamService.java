package com.jeff.teammate.service;

import com.jeff.teammate.model.User;
import com.jeff.teammate.model.response.SafeTeam;
import com.jeff.teammate.model.response.TeamAndMembers;

import java.util.List;
import java.util.Map;

public interface TeamService {
    boolean createMyTeam(User currentUser, int gameId, String teamName, int maxSize, boolean isPublic,
                         String password, String description, List<Integer> tags, String logo);

    boolean disbandMyTeam(User currentUser);

    boolean joinTeam(User currentUser, int teamId, String password);

    boolean leaveTeam(User currentUser);

    boolean kickOut(User currentUser, int userId);

    boolean changeOwner(User currentUser);

    TeamAndMembers myTeamAndMembers(User currentUser);

    List<SafeTeam> recommendTeams(User currentUser, int gameId, Map<Integer, byte[]> currentUserTags);

    List<SafeTeam> searchTeamByName(int gameId,String teamName);
}

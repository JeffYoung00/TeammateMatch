package com.jeff.teammate.mapper;

import com.jeff.teammate.model.Game;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameMapper {
    List<Game> getGames();
}

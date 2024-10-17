package com.example.boardgame.beans;

import lombok.Data;

import java.util.List;

@Data
public class BoardGame {
    private Long id;
    private String name;
    private int level;
    private int minPlayers;
    private String maxPlayers;
    private String gameType;
    private List<Review> reviews;
}

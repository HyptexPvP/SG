package me.hyptex.sg.game;

import lombok.Data;

import java.util.UUID;

@Data
public class Profile {

    private final UUID uuid;
    private int kills = 0;
    private boolean alive = true;
}

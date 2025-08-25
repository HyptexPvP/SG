package me.hyptex.sg.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ScoreboardListener implements Listener {

    @Getter
    private static final ConcurrentMap<UUID, FastBoard> boards = new ConcurrentHashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        boards.put(event.getPlayer().getUniqueId(), new FastBoard(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        FastBoard board = boards.get(event.getPlayer().getUniqueId());

        if (board != null) {
            board.delete();
            boards.remove(event.getPlayer().getUniqueId());
        }
    }

}

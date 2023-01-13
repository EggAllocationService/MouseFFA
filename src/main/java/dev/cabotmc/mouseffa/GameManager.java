package dev.cabotmc.mouseffa;

import dev.cabotmc.mouseffa.gamemodes.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GameManager {
    static HashMap<String, GameMode> gameModes = new HashMap<>();
    public static void addGamemode(GameMode g) {
        gameModes.put(g.getName(), g);
    }
    static {
        addGamemode(new DefaultGamemode());
        addGamemode(new InstagibGamemode());
        addGamemode(new InstagibMileHighGamemode());
        addGamemode(new HeadshotOnlyGamemode());
        addGamemode(new InstagibWallbangGamemode());
    }
    static GameMode currentGameMode = null;
    public static GameMode getCurrentGameMode() {
        return currentGameMode;
    }
    public static void changeGameMode(String newMode) {
        if (!gameModes.containsKey(newMode)) return;
        var g = gameModes.get(newMode);
        if (currentGameMode != null) {
           if (currentGameMode instanceof Listener) {
               HandlerList.unregisterAll((Listener) currentGameMode);
           }
           currentGameMode.cleanup();
        }
        currentGameMode = g;
        for (Player p : Bukkit.getOnlinePlayers()) {
            currentGameMode.resetPlayer(p);
        }
        if (currentGameMode instanceof  Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) currentGameMode, MouseFFA.instance);
        }
    }
}

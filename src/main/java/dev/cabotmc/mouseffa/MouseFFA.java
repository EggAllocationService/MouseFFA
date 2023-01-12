package dev.cabotmc.mouseffa;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.List;

public final class MouseFFA extends JavaPlugin {
    public static Team playersTeam = null;
    public static Objective scoreObjective = null;
    public static MouseFFA instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        var s = getServer().getScoreboardManager().getMainScoreboard();
        playersTeam = s.getTeam("players");
        if (playersTeam == null) {
            playersTeam = s.registerNewTeam("players");
            playersTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            playersTeam.setAllowFriendlyFire(true);
        }
        scoreObjective = s.getObjective("score");
        if (scoreObjective == null) {
            scoreObjective = s.registerNewObjective("score", Criteria.DUMMY, (Component) null);
            scoreObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        for (var e : s.getEntries()) {
            s.resetScores(e);
        }


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (tryGiveArrow(p)) {
                    p.playSound(p, Sound.ENTITY_ITEM_PICKUP, 0.4f, 1f);
                }
            }
        }, 0, 20 * 5);
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);
        GameManager.changeGameMode("Instagib");
    }
    public static boolean tryGiveArrow(Player p ) {
        var i = p.getInventory().getItem(8);
        if (i == null || i.getType() == Material.AIR || (i.getType() == Material.ARROW && i.getAmount() < 3)) {
            if (i == null) {
                p.getInventory().setItem(8, new ItemStack(Material.ARROW));
            } else {
                i.setAmount(i.getAmount() + 1);
            }
            return true;
        }
        return false;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

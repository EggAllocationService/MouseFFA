package dev.cabotmc.mouseffa;

import dev.cabotmc.mouseffa.spawn.SpawnLocationChooser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public abstract class GameMode {
    public abstract String getName();

    public abstract void resetPlayer(Player p);
    public abstract void cleanup();

    public void onKill(Player killer, Player k) {
        Bukkit.getServer().sendMessage(createKillMessage(killer, k));
        killer.playSound(killer, Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1f);
        SpawnLocationChooser.pickRandomSpawnAndTeleport(k);
        k.playSound(k.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.8f, 1f);
        k.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, true, false));

        k.showTitle(Title.title(
                Component.text(""),
                createDeathSymbol(killer),
                defaultTimes
        ));
        var score = MouseFFA.scoreObjective.getScore(killer);
        score.setScore(score.getScore() + 1);
        postKill(killer, k);
    }
    public abstract void postKill(Player killer, Player died);
    static Title.Times defaultTimes = Title.Times.times(Duration.of(200, ChronoUnit.MILLIS), Duration.of(600, ChronoUnit.MILLIS), Duration.of(200, ChronoUnit.MILLIS));
    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if (e.getHitBlock() != null) {
            e.getEntity().remove();
        }
    }
    public static Component createKillMessage(Player killer, Player killed) {
        var base = Component.text(killer.getName(), TextColor.color(0x2cde5c));
        base = base.append(createDeathSymbol(killed));
        var para = Component.text(" (", TextColor.color(0x505050));
        base = base.append(para);
        var dist = killer.getLocation().distance(killed.getLocation());

        base = base.append(Component.text(String.format("%.1f", dist) + "m", TextColor.color(0xd9ce34)));
        base = base.append(Component.text(")", TextColor.color(0x505050)));
        return base;
    }
    public static Component createDeathSymbol(Player killed) {
        return Component.text(" \u2620 " + killed.getName(), TextColor.color(0xe84141));
    }
}

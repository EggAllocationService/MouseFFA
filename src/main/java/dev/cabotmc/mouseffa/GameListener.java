package dev.cabotmc.mouseffa;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import dev.cabotmc.mouseffa.spawn.SpawnLocationChooser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

public class GameListener implements Listener {
    @EventHandler
    public void collide(ProjectileCollideEvent e) {
        var launcher = (Player) e.getEntity().getShooter();
        var killed = e.getCollidedWith();
        if (!(killed instanceof Player)) return;
        var k = (Player) killed;
        Bukkit.getServer().sendMessage(createKillMessage(launcher, k));
        launcher.playSound(launcher, Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1f);
        SpawnLocationChooser.pickRandomSpawnAndTeleport(k);
        k.playSound(k.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.8f, 1f);
        k.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1, true, false));

        k.showTitle(Title.title(
                Component.text(""),
                createDeathSymbol(launcher),
                defaultTimes
        ));
        var score = MouseFFA.scoreObjective.getScore(launcher);
        score.setScore(score.getScore() + 1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(MouseFFA.instance, () -> {
           e.getEntity().remove();
        });
        MouseFFA.tryGiveArrow(launcher);
        // give arrow to loser if none
        var i = k.getInventory().getItem(8);
        if (i == null) {
            k.getInventory().setItem(8, new ItemStack(Material.ARROW));
        }

    }
    static Title.Times defaultTimes = Title.Times.times(Duration.of(100, ChronoUnit.MILLIS), Duration.of(500, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS));
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

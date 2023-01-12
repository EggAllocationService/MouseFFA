package dev.cabotmc.mouseffa.gamemodes;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import dev.cabotmc.mouseffa.GameMode;
import dev.cabotmc.mouseffa.MouseFFA;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import static dev.cabotmc.mouseffa.GeneralListener.createCrossbow;

public class DefaultGamemode extends GameMode implements Listener {
    @EventHandler
    public void collide(ProjectileCollideEvent e) {
        var launcher = (Player) e.getEntity().getShooter();
        var killed = e.getCollidedWith();
        if (!(killed instanceof Player)) return;
        var k = (Player) killed;
        onKill(launcher, k);
        Bukkit.getScheduler().scheduleSyncDelayedTask(MouseFFA.instance, () -> {
            e.getEntity().remove();
        });
    }

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void resetPlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setItem(0, createCrossbow());
        p.getInventory().setItem(8, new ItemStack(Material.ARROW, 3));
    }

    @Override
    public void cleanup() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
        }
    }

    @Override
    public void postKill(Player killer, Player died) {
        MouseFFA.tryGiveArrow(killer);
        // give arrow to loser if none
        var i = died.getInventory().getItem(8);
        if (i == null) {
            died.getInventory().setItem(8, new ItemStack(Material.ARROW));
        }
    }
}

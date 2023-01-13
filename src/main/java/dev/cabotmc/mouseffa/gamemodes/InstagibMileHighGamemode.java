package dev.cabotmc.mouseffa.gamemodes;

import dev.cabotmc.mouseffa.MouseFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class InstagibMileHighGamemode extends InstagibGamemode implements Listener {
    @Override
    public String getName() {
        return "InstagibMileHigh";
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        if (e.getPlayer().isOnGround()) {
            e.getPlayer().setVelocity(new Vector(0, 2, 0));
        }
    }
    @Override
    @EventHandler
    public void fire(EntityShootBowEvent e) {
        super.fire(e);
        var p = (Player) e.getEntity();
        MouseFFA.tryGiveArrow(p);
    }
    @Override
    public void resetPlayer(Player p) {
        super.resetPlayer(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1000000 * 20, 0, true, false));
    }
}

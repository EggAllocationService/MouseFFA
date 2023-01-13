package dev.cabotmc.mouseffa.gamemodes;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import dev.cabotmc.mouseffa.MouseFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HeadshotOnlyGamemode extends DefaultGamemode {
    @Override
    public String getName() {
        return "HeadshotOnly";
    }

    @EventHandler
    @Override
    public void collide(ProjectileCollideEvent e) {
        var hit = e.getCollidedWith();
        if (hit instanceof Player) {
            var p = (Player) hit;
            if (Math.abs(e.getEntity().getLocation().getY() - p.getEyeLocation().getY()) > 0.4) {
                e.getEntity().remove();
                return;
            }
        }
        super.collide(e);

    }
}

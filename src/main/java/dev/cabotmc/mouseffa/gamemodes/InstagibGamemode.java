package dev.cabotmc.mouseffa.gamemodes;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class InstagibGamemode extends DefaultGamemode {
    @Override
    public String getName() {
        return "Instagib";
    }

    @Override
    public void cleanup() {
        super.cleanup();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getActivePotionEffects().forEach(e -> {
                p.removePotionEffect(e.getType());
            });
        }
    }

    @Override
    public void resetPlayer(Player p) {
        super.resetPlayer(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000 * 20, 0, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000 * 20, 0, true, false));
    }

    @EventHandler
    public void fire(EntityShootBowEvent e) {
        var p = (Player) e.getEntity();
        var look = p.getLocation().getDirection().clone();
        look.multiply(-1);
        look.normalize();
        p.setVelocity(look);
        e.getProjectile().remove();
        var lookVector = p.getLocation().getDirection().clone().normalize();
        var baseLoc = p.getEyeLocation().toVector();
        var lh = p.rayTraceBlocks(40.0d);
        Vector lookHit;
        if (lh == null) {
            lookHit = baseLoc.clone().add(
                    p.getLocation().getDirection().normalize().multiply(40)
            );
        } else {
            lookHit = lh.getHitPosition();
        }

        var max = lookHit.distance(p.getLocation().toVector());

        var opts = new Particle.DustOptions(Color.fromRGB(50, 157, 168), 0.5f);
        HashSet<Entity> hitTargets = new HashSet<>();
        for (double distanceMultipler = 0.0d; distanceMultipler < max; distanceMultipler += 0.1d) {
            var curr = baseLoc.clone().add(
                    lookVector.clone().multiply(distanceMultipler)
            );
            p.getWorld().spawnParticle(
                    Particle.REDSTONE,
                    curr.toLocation(p.getWorld()),
                    3,
                     0.05d,
                     0.05d,
                     0.05d,
                     0.05d,
                          opts
            );
            // check if theres a player there
            for (var possibleHit : p.getWorld().getEntities()) {
                if (possibleHit == p) continue;
                if (possibleHit.getBoundingBox().clone().expand(0.3d).contains(curr)) {
                    hitTargets.add(possibleHit);
                }
            }

        }
        for (var possibleHit: p.getWorld().getEntities()) {
            if (possibleHit == p) continue;
            if (possibleHit.getBoundingBox().clone().expand(0.8d).contains(lookHit)) {
                hitTargets.add(possibleHit);
            }
        }
        hitTargets.forEach(c -> {
            if (c instanceof Player) {
                onKill(p, (Player) c);
            } else {
                c.remove();
            }
        });
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.MASTER, 1f, 2f);
        p.getWorld().playSound(lookHit.toLocation(p.getWorld()), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 0.7f, 1f);
        p.getWorld().spawnParticle(Particle.FLASH,
                lookHit.toLocation(p.getWorld()),
                10,
                0.5d,
                0.5d,
                0.5d);

    }
}

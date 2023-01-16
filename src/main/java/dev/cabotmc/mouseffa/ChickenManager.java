package dev.cabotmc.mouseffa;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class ChickenManager {
  public static void spawnChicken() {
    var w = Bukkit.getWorld("world");
    while (true) {
      int targetX = ThreadLocalRandom.current().nextInt(-13, 22);
      int targetZ = ThreadLocalRandom.current().nextInt(-18, 26);
      // -60 is floor
      if (w.getBlockAt(targetX, -60, targetZ).getType() == Material.BRICK_SLAB) {
        var e = w.spawnEntity(new Location(w, targetX, -58, targetZ), EntityType.CHICKEN);
        e.setSilent(true);
        e.setPersistent(true);
        w.spawnParticle(Particle.CLOUD, e.getLocation(), 20);
      }
    }
  }
}

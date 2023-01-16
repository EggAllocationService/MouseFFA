package dev.cabotmc.mouseffa.gamemodes;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class InstagibWallbangGamemode extends InstagibGamemode {
    @Override
    public String getName() {
        return "InstagibWallbang";
    }

    @Override
    public boolean canPenetrateWalls() {
        return true;
    }
}

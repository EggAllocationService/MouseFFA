package dev.cabotmc.mouseffa;

import dev.cabotmc.mouseffa.spawn.SpawnLocationChooser;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;


public class GeneralListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e) {
        e.joinMessage(null);
        MouseFFA.playersTeam.addEntity(e.getPlayer());
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(0, createCrossbow());
        e.getPlayer().getInventory().setItem(8, new ItemStack(Material.ARROW, 3));
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        SpawnLocationChooser.pickRandomSpawnAndTeleport(e.getPlayer());
        MouseFFA.scoreObjective.getScore(e.getPlayer()).setScore(0);

    }
    @EventHandler
    public void damage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void interact(PlayerInteractEvent e) {
        e.setUseInteractedBlock(Event.Result.DENY);
    }


    static ItemStack createCrossbow() {
        var i = new ItemStack(Material.CROSSBOW);
        var meta = i.getItemMeta();
        meta.addEnchant(Enchantment.QUICK_CHARGE, 3, false);
        meta.setUnbreakable(true);
        i.setItemMeta(meta);
        return i;
    }
}

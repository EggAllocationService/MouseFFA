package dev.cabotmc.mouseffa.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class SpawnLocationChooser {
    public static final int[][] POSSIBLE_LOCATIONS = new int[][] {
            new int[]{-20, -59, 23},
            new int[]{-21, -54, -16},
            new int[]{10, -53, -14},
            new int[]{19, -59, -12},
            new int[]{18, -59, -6},
            new int[]{20, -59, 6},
            new int[]{21, -59, 23},
            new int[]{-6, -59, 24},
            new int[]{-1, -58, 1}

    };
    static ArrayList<Location> locations = new ArrayList<>();
    public static void pickRandomSpawnAndTeleport(Player p) {
        var o = new double[POSSIBLE_LOCATIONS.length];
        if (locations.size() == 0) {
            generateCache();
        }

        Collections.shuffle(locations);
        p.teleport(locations.get(0));
        /*int index = 0;
        for (Location l : locations) {
            o[index] = calculateClosestPlayerToPoint(p, l);
        }
        int best = minPos(o);
        p.teleport(locations.get(best));*/
    }
    static int minPos(double[] a) {
        int curr = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < a[curr]) {
                curr = i;
            }
        }
        return curr;
    }
    static double calculateClosestPlayerToPoint(Player exclude, Location l) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            return 0;
        }
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != exclude)
                .mapToDouble(p -> p.getLocation().distance(l))
                .min()
                .orElse(0);
    }
    static void generateCache() {
        var w = Bukkit.getWorld("world");
        for (int[] l : POSSIBLE_LOCATIONS) {
            locations.add(new Location(w, l[0] + 0.5, l[1] + 0.5, l[2] + 0.5));
        }
    }

}

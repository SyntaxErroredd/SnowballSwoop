package me.syntaxerror.snowballswoop;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Invincibility {

    public static HashMap<UUID, Double> invincibility;

    public static void setupCooldown() {
        invincibility = new HashMap<>();
    }

    public static void setCooldown(Player player, double seconds) {
        double delay = System.currentTimeMillis() + (seconds * 1000);
        invincibility.put(player.getUniqueId(), delay);
    }

    public static boolean checkCooldown(Player player) {
        if(!invincibility.containsKey(player.getUniqueId()) || invincibility.get(player.getUniqueId()) <= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }
}

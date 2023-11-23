package me.syntaxerror.snowballswoop.PowerUps;

import me.syntaxerror.snowballswoop.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PowerUpManager {

    private static ItemStack[] powerups = {
            ItemManager.ExplosiveSnowball,
            ItemManager.HealingPotion,
            ItemManager.TripleShot,
            ItemManager.SnowWall,
    };

    public static void createPowerUp(Player player){
        Random r = new Random();
        ItemStack item = powerups[r.nextInt(powerups.length + 0) - 0];
        player.getInventory().addItem(item);
        player.sendMessage(ChatColor.GOLD + item.getItemMeta().getDisplayName() + ChatColor.WHITE + " PowerUp has been added into your inventory!");
    }

    //resistance pot, speed pot, extra snowballs, updraft
}

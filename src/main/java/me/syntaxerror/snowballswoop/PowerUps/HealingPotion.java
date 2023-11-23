package me.syntaxerror.snowballswoop.PowerUps;

import me.syntaxerror.snowballswoop.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HealingPotion implements Listener {

    @EventHandler
    public void onPlace(PlayerInteractEvent event){
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        if(event.getItem() == null)
            return;
        if(event.getItem().getItemMeta() == null)
            return;
        if(event.getItem().getItemMeta().getLore() == null)
            return;
        if (!event.getItem().getItemMeta().getLore().contains(ItemManager.HealingPotion.getItemMeta().getLore().get(0)))
            return;

        if(event.getPlayer().getHealth() < 20) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            event.getPlayer().setHealth(event.getPlayer().getHealth() + 2);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Healing Potion " + ChatColor.WHITE + "gave you an extra life!");
        }
        else{
            event.getPlayer().sendMessage(ChatColor.RED + "You are already at maximum health!");
        }
        event.setCancelled(true);
    }
}

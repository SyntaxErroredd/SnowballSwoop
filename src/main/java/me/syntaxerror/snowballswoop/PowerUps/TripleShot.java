package me.syntaxerror.snowballswoop.PowerUps;

import me.syntaxerror.snowballswoop.ItemManager;
import me.syntaxerror.snowballswoop.SnowballSwoop;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TripleShot implements Listener {

    public static List<Player> tripleActivated = new ArrayList<>();

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        if(event.getItem() == null)
            return;
        if(event.getItem().getItemMeta() == null)
            return;
        if(event.getItem().getItemMeta().getLore() == null)
            return;
        if (!event.getItem().getItemMeta().getLore().contains(ItemManager.TripleShot.getItemMeta().getLore().get(0)))
            return;

        event.getItem().setAmount(event.getItem().getAmount() - 1);
        tripleActivated.add(event.getPlayer());
        event.getPlayer().sendMessage(ChatColor.GOLD + "Triple Shot " + ChatColor.WHITE + "has been activated!");
        new BukkitRunnable(){
            public void run(){
                tripleActivated.remove(event.getPlayer());
            }
        }.runTaskLater(SnowballSwoop.getInstance(), 100L);
        event.setCancelled(true);
    }
}

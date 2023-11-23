package me.syntaxerror.snowballswoop.PowerUps;

import me.syntaxerror.snowballswoop.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SnowWall implements Listener {

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
        if (!event.getItem().getItemMeta().getLore().contains(ItemManager.SnowWall.getItemMeta().getLore().get(0)))
            return;

        event.getItem().setAmount(event.getItem().getAmount() - 1);
        BlockFace direction = event.getPlayer().getFacing();

        if(direction.equals(BlockFace.EAST) || direction.equals(BlockFace.WEST)){
            Block central = event.getPlayer().getLocation().getBlock().getRelative(direction).getRelative(direction);
            Block right1 = central.getRelative(BlockFace.NORTH);
            Block right2 = right1.getRelative(BlockFace.NORTH);
            Block left1 = central.getRelative(BlockFace.SOUTH);
            Block left2 = left1.getRelative(BlockFace.SOUTH);

            for(int i = 0; i < 3; i++){
                central.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                right1.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                right2.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                left1.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                left2.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
            }
        }
        else{
            Block central = event.getPlayer().getLocation().getBlock().getRelative(direction);
            Block right1 = central.getRelative(BlockFace.WEST);
            Block right2 = right1.getRelative(BlockFace.WEST);
            Block left1 = central.getRelative(BlockFace.EAST);
            Block left2 = left1.getRelative(BlockFace.EAST);

            for(int i = 0; i < 3; i++){
                central.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                right1.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                right2.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                left1.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
                left2.getLocation().add(0, i, 0).getBlock().setType(Material.SNOW_BLOCK);
            }
        }

        event.getPlayer().sendMessage(ChatColor.GOLD + "Snow Wall " + ChatColor.WHITE + "spawned a wall in front of you!");
        event.setCancelled(true);
    }
}

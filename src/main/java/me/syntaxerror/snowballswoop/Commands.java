package me.syntaxerror.snowballswoop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.isOp()){
                if(cmd.getName().equalsIgnoreCase("snowballswoop")){
                    if(args.length == 0){
                        player.sendMessage("Please enter the Usernames of Players.");
                    }
                    /*else if(args.length == 1){
                        player.sendMessage("You need at least 2 Players to play Snowball Swoop.");
                    }*/
                    else {
                        List<Player> playerList = new ArrayList<>();
                        for (String name : args) {
                            playerList.add(Bukkit.getPlayer(name));
                        }
                        SnowballSwoop.getInstance().getServer().getPluginManager().registerEvents(new SnowballSwoopGame(playerList), SnowballSwoop.getInstance());
                    }
                }
                if(cmd.getName().equalsIgnoreCase("snowballswoopgivepowerups")){
                    player.getInventory().addItem(ItemManager.ExplosiveSnowball);
                    player.getInventory().addItem(ItemManager.HealingPotion);
                    player.getInventory().addItem(ItemManager.SnowWall);
                    player.getInventory().addItem(ItemManager.TripleShot);
                }
            }
            else{
                player.sendMessage("You are not allowed to use this command!");
            }
            return true;
        }
        else{
            sender.sendMessage("Only players can use this command.");
            return true;
        }
    }
}

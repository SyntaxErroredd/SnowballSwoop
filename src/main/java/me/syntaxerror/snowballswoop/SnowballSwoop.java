package me.syntaxerror.snowballswoop;

import me.syntaxerror.snowballswoop.PowerUps.ExplosiveSnowball;
import me.syntaxerror.snowballswoop.PowerUps.HealingPotion;
import me.syntaxerror.snowballswoop.PowerUps.SnowWall;
import me.syntaxerror.snowballswoop.PowerUps.TripleShot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SnowballSwoop extends JavaPlugin {

    static SnowballSwoop instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //system to check if player is already in game
        instance = this;

        ItemManager.init();

        Invincibility.setupCooldown();

        getCommand("snowballswoop").setExecutor(new Commands());
        getCommand("snowballswoopgivepowerups").setExecutor(new Commands());

        getServer().getPluginManager().registerEvents(new SnowWall(), this);
        getServer().getPluginManager().registerEvents(new HealingPotion(), this);
        getServer().getPluginManager().registerEvents(new TripleShot(), this);
        getServer().getPluginManager().registerEvents(new ExplosiveSnowball(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SnowballSwoop getInstance(){
        return instance;
    }

    public static void createSnowballSwoopGame(List<Player> playerList){
        new SnowballSwoopGame(playerList);
    }
}

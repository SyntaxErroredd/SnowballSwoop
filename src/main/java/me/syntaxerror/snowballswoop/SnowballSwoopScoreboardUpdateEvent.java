package me.syntaxerror.snowballswoop;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.UUID;

public class SnowballSwoopScoreboardUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    HashMap<UUID, Integer> playerPoints;

    public SnowballSwoopScoreboardUpdateEvent(HashMap<UUID, Integer> playerPoints){
        this.playerPoints = playerPoints;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public HashMap<UUID, Integer> getPlayerPoints(){
        return playerPoints;
    }
}

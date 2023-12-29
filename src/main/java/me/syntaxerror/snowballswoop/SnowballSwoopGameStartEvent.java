package me.syntaxerror.snowballswoop;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowballSwoopGameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public SnowballSwoopGameStartEvent(){

    }

    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}

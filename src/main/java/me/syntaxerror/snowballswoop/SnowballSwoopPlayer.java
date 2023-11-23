package me.syntaxerror.snowballswoop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SnowballSwoopPlayer{

    private UUID uuid;
    private boolean eliminated = false;
    private int points = 0;
    private int hits = 0;
    private int eliminations = 0;
    private Inventory originalInventory;
    private Location originalLocation;

    public SnowballSwoopPlayer(UUID uuid, Inventory originalInventory, Location originalLocation){
        this.uuid = uuid;
        this.originalInventory = originalInventory;
        this.originalLocation = originalLocation;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public int getPoints() {
        return points;
    }

    public int getHits() {
        return hits;
    }

    public int getEliminations() {
        return eliminations;
    }

    public Inventory getOriginalInventory() {
        return originalInventory;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setEliminations(int eliminations) {
        this.eliminations = eliminations;
    }

    public void setOriginalInventory(Inventory originalInventory) {
        this.originalInventory = originalInventory;
    }

    public void setOriginalLocation(Location originalLocation) {
        this.originalLocation = originalLocation;
    }
}

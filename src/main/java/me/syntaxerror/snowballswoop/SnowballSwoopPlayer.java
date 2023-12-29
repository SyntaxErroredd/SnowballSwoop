package me.syntaxerror.snowballswoop;

import me.syntaxerror.snowballswoop.scoreboard.GameScoreboard;
import me.syntaxerror.snowballswoop.scoreboard.ScoreboardSection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class SnowballSwoopPlayer{

    private UUID uuid;
    private SnowballSwoopGame snowballSwoopGame;
    private boolean eliminated = false;
    private int points = 0;
    private int hits = 0;
    private int eliminations = 0;
    //TODO inventory original Location
    private Inventory originalInventory;
    private Location originalLocation;
    private final GameScoreboard gameScoreboard;
    private final List<ScoreboardSection> scoreboardSections;

    public SnowballSwoopPlayer(UUID uuid, SnowballSwoopGame snowballSwoopGame, Inventory originalInventory, Location originalLocation){
        this.uuid = uuid;
        this.snowballSwoopGame = snowballSwoopGame;
        this.originalInventory = originalInventory;
        this.originalLocation = originalLocation;
        this.gameScoreboard = new GameScoreboard(this);
        this.scoreboardSections = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public SnowballSwoopGame getSnowballSwoopGame(){
        return snowballSwoopGame;
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

    public List<ScoreboardSection> getScoreboardSections(){
        return scoreboardSections;
    }

    public GameScoreboard getGameScoreboard(){
        return gameScoreboard;
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

    public void setActionbarTitle(String s) {
        getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(s));
    }

    public void givePoints(int points){
        this.points += points;
        setActionbarTitle("§3+" + points + " Points");
        updateScoreboard();
    }

    public void updateScoreboard() {
        gameScoreboard.updateScoreboard();
        HashMap<UUID, Integer> playerPoints = new HashMap<>();
        for(SnowballSwoopPlayer player : snowballSwoopGame.getRegisteredPlayers()){
            playerPoints.put(player.getUuid(), player.getPoints());
        }
        SnowballSwoopScoreboardUpdateEvent event = new SnowballSwoopScoreboardUpdateEvent(playerPoints);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}

package me.syntaxerror.snowballswoop;

import me.syntaxerror.snowballswoop.PowerUps.ExplosiveSnowball;
import me.syntaxerror.snowballswoop.PowerUps.PowerUpManager;
import me.syntaxerror.snowballswoop.PowerUps.TripleShot;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SnowballSwoopGame implements Listener {

    public SnowballSwoopGame INSTANCE;
    private static final String[] hitQuotes = {
            "<hit> forgot to dodge!",
            "Sigh, <hit> was hit!",
            "Smh, smh, <hit> was hit!",
            "<hit> forgot the objective of the game!",
            "<hit> fell asleep!"
    };
    private GameState gameState;
    private final HashMap<UUID, SnowballSwoopPlayer> playerLink = new HashMap<>();
    public int time = 8 * 60;

    public SnowballSwoopGame(List<Player> players){
        SnowballSwoop.getInstance().getServer().getPluginManager().registerEvents(this, SnowballSwoop.getInstance());
        INSTANCE = this;
        preparePlayers(players);
        onStart();
    }

    public Set<SnowballSwoopPlayer> getRegisteredPlayers() {
        return new HashSet<>(playerLink.values());
    }

    public void preparePlayers(List<Player> players){
        unloadWorld("snowballswooptest");
        copyWorld("snowballswoopbackup", "snowballswooptest", "uid.dat");
        World world = loadWorld("snowballswooptest");
        for(Player player : players){
            if (!player.isOnline()) continue;
            playerLink.put(player.getUniqueId(), new SnowballSwoopPlayer(player.getUniqueId(), this, player.getInventory(), player.getLocation()));
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(new Location(world, 4.5, 90, -0.5));
            player.getInventory().clear();
            ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
            ItemMeta elytraMeta = elytra.getItemMeta();
            elytraMeta.setUnbreakable(true);
            elytra.setItemMeta(elytraMeta);
            player.getEquipment().setChestplate(elytra);
            player.getInventory().setItem(8, new ItemStack(Material.FIREWORK_ROCKET, 3));
            player.getInventory().setItem(7, new ItemStack(Material.SNOW_BLOCK, 64));
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(ChatColor.GREEN + "Successfully added you into Snowball Swoop. Get ready for take off and good luck!");
        }
        gameState = GameState.WAITING;
    }

    public boolean unloadWorld(String worldName) {
        if (Bukkit.getWorld(worldName) != null) {
            Bukkit.getServer().unloadWorld(Bukkit.getWorld(worldName), true);
            return true;
        }
        return false;
    }
    public void copyWorld(String worldName, String newLocation, String... ignore) {
        ArrayList<String> ignored = new ArrayList<String>(Arrays.asList(ignore));
        for (File file : new File(worldName).listFiles()) {
            if (!ignored.contains(file.getName())) {
                if (file.isDirectory()) {
                    new File(newLocation + "\\" + file.getName()).mkdirs();
                    copyWorld(worldName + "\\" + file.getName(), newLocation + "\\" + file.getName() + "\\", ignore);
                } else {
                    try {
                        InputStream in = new FileInputStream(file.getAbsolutePath());
                        OutputStream out = new FileOutputStream(newLocation.endsWith("\\")
                                ? newLocation + file.getName() : newLocation + "\\" + file.getName());
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0)
                            out.write(buffer, 0, length);
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public World loadWorld(String worldName) {
        WorldCreator worldCreater = new WorldCreator(worldName);
        Bukkit.getServer().createWorld(worldCreater);
        return Bukkit.getWorld(worldName);
    }

    public void onStart(){
        SnowballSwoopGameStartEvent event = new SnowballSwoopGameStartEvent();
        Bukkit.getServer().getPluginManager().callEvent(event);
        new BukkitRunnable(){
            int i = 0;
            public void run(){

                if(i % 2 == 0){
                    if(gameState.equals(GameState.GRACE) || gameState.equals(GameState.STARTED)) {
                        for (SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()) {
                            if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                            Player player = snowballSwoopPlayer.getPlayer();
                            if(player == null)
                                continue;
                            if (!player.getInventory().containsAtLeast(ItemManager.Snowball, 80)) {
                                player.getInventory().addItem(ItemManager.Snowball);
                            } else {
                                player.sendMessage(ChatColor.RED + "You have reached the maximum storage of snowballs!");
                            }
                        }
                    }
                }

                if(i == 0){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()) {
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.RED + "Game Starts in:", "", 0, 20, 0);
                    }
                }
                if(i == 1){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.RED + "3", "", 0, 20, 0);
                    }
                }
                if(i == 2){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.GOLD + "2", "", 0, 20, 0);
                    }
                }
                if(i == 3){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.YELLOW + "1", "", 0, 20, 0);
                    }
                }
                if(i == 4){
                    gameState = GameState.GRACE;
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getGameScoreboard().createScoreboard();
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.GREEN + "Take off!", ChatColor.GREEN + "Grace Period: 20 seconds", 0, 40, 0);
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "Grace Period started! Find a good spot!");
                        int radius = 20;
                        int rSquared = radius * radius;
                        for (int x = -radius; x <= radius; x++) {
                            for (int y = -radius; y <= radius; y++) {
                                for (int z = -radius; z <= radius; z++) {
                                    if (x * x + y * y + z * z <= rSquared) {
                                        Location blockLoc = snowballSwoopPlayer.getPlayer().getLocation().clone().add(x, y, z);
                                        Block block = snowballSwoopPlayer.getPlayer().getWorld().getBlockAt(blockLoc);
                                        if (block.getType() == Material.BLUE_STAINED_GLASS) {
                                            block.setType(Material.AIR);
                                        }
                                    }
                                }
                            }
                        }
                        snowballSwoopPlayer.getPlayer().setGliding(true);
                    }
                }
                if(i == 14){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.YELLOW + "10" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 19){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.GOLD + "5" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 20){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.GOLD + "4" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 21){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.GOLD + "3" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 22){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.RED + "2" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 23){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.WHITE + "Grace Period ends in " + ChatColor.RED + "1" + ChatColor.WHITE + " seconds.");
                    }
                }
                if(i == 24){
                    gameState = GameState.STARTED;
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.RED + "Grace Period Over!", ChatColor.GOLD + "Good luck!", 0, 40, 0);
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.RED + "Grace Period over, snowballs will now deal damage. You have 8 minutes to eliminate as many players.");
                    }
                }
                if(i == 54){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 104){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 154){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 204){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 254){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 304){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 354){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 404){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 454){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        PowerUpManager.createPowerUp(snowballSwoopPlayer.getPlayer());
                    }
                }
                if(i == 264){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "4" + ChatColor.WHITE + " minutes remaining!");
                    }
                }
                if(i == 384){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "2" + ChatColor.WHITE + " minutes remaining!");
                    }
                }
                if(i == 444){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "1" + ChatColor.WHITE + " minute remaining!");
                    }
                }
                if(i == 474){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "30" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 484){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GREEN + "20" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 494){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.YELLOW + "10" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 499){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GOLD + "5" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 500){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GOLD + "4" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 501){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.RED + "3" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 502){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.RED + "2" + ChatColor.WHITE + " seconds remaining!");
                    }
                }
                if(i == 503){
                    for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                        if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.RED + "1" + ChatColor.WHITE + " second remaining!");
                    }
                }
                if(i == 504){
                    onEnd();
                    cancel();
                }
                if(gameState.equals(GameState.ENDED)){
                    cancel();
                }

                for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                    if (snowballSwoopPlayer == null || snowballSwoopPlayer.getPlayer() == null || !snowballSwoopPlayer.getPlayer().isOnline()) continue;
                    snowballSwoopPlayer.updateScoreboard();
                }

                i++;
                if(i > 24)
                    time--;
                //TODO Time start at correct time and 8 minutes
            }
        }.runTaskTimer(SnowballSwoop.getInstance(), 100L, 20L);
    }

    /*public void updateLeaderBoard(){
        for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
            displayMinigameScoreboard(snowballSwoopPlayer.getPlayer());
        }
    }

    public void displayMinigameScoreboard(Player player){
        String game = "Snowball Swoop";

        List<SnowballSwoopPlayer> snowballSwoopPlayers = playerLink.values().stream().sorted(Comparator.comparingInt(SnowballSwoopPlayer::getPoints).reversed()).collect(Collectors.toList());

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("minigame", "dummy", ChatColor.GOLD + "MCMT");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score scoreGame = objective.getScore(ChatColor.GREEN + "Game: " + game);
        scoreGame.setScore(6);

        Score scoreGamePlacements = objective.getScore(ChatColor.WHITE + "Minigame Placements:");
        scoreGamePlacements.setScore(5);

        if(player.getUniqueId().equals(snowballSwoopPlayers.get(0).getUuid()) || player.getUniqueId().equals(snowballSwoopPlayers.get(1).getUuid())){
            String firstPlayer = snowballSwoopPlayers.get(0).getPlayer().getPlayerListName();
            int firstPlayerScore = snowballSwoopPlayers.get(0).getPoints();
            Score firstDisplay = objective.getScore(ChatColor.WHITE + "1: " + firstPlayer + " " + ChatColor.AQUA + firstPlayerScore);
            firstDisplay.setScore(4);
            if(snowballSwoopPlayers.size() >= 2) {
                String secondPlayer = snowballSwoopPlayers.get(1).getPlayer().getPlayerListName();
                int secondPlayerScore = snowballSwoopPlayers.get(1).getPoints();
                Score secondDisplay = objective.getScore(ChatColor.WHITE + "2: " + secondPlayer + " " + ChatColor.AQUA + secondPlayerScore);
                secondDisplay.setScore(3);
            }
            if(snowballSwoopPlayers.size() >= 3) {
                String thirdPlayer = snowballSwoopPlayers.get(2).getPlayer().getPlayerListName();
                int thirdPlayerScore = snowballSwoopPlayers.get(2).getPoints();
                Score thirdDisplay = objective.getScore(ChatColor.WHITE + "3: " + thirdPlayer + " " + ChatColor.AQUA + thirdPlayerScore);
                thirdDisplay.setScore(2);
            }
            if(snowballSwoopPlayers.size() >= 4) {
                String fourthPlayer = snowballSwoopPlayers.get(3).getPlayer().getPlayerListName();
                int fourthPlayerScore = snowballSwoopPlayers.get(3).getPoints();
                Score fourthDisplay = objective.getScore(ChatColor.WHITE + "4: " + fourthPlayer + " " + ChatColor.AQUA + fourthPlayerScore);
                fourthDisplay.setScore(1);
            }
        }
        else if(player.getUniqueId().equals(snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 1).getUuid())){
            if(snowballSwoopPlayers.size() == 3){
                String firstPlayer = snowballSwoopPlayers.get(0).getPlayer().getPlayerListName();
                int firstPlayerScore = snowballSwoopPlayers.get(0).getPoints();
                Score firstDisplay = objective.getScore(ChatColor.WHITE + "1: " + firstPlayer + " " + ChatColor.AQUA + firstPlayerScore);
                firstDisplay.setScore(4);
                String secondPlayer = snowballSwoopPlayers.get(1).getPlayer().getPlayerListName();
                int secondPlayerScore = snowballSwoopPlayers.get(1).getPoints();
                Score secondDisplay = objective.getScore(ChatColor.WHITE + "2: " + secondPlayer + " " + ChatColor.AQUA + secondPlayerScore);
                secondDisplay.setScore(3);
                String thirdPlayer = snowballSwoopPlayers.get(2).getPlayer().getPlayerListName();
                int thirdPlayerScore = snowballSwoopPlayers.get(2).getPoints();
                Score thirdDisplay = objective.getScore(ChatColor.WHITE + "3: " + thirdPlayer + " " + ChatColor.AQUA + thirdPlayerScore);
                thirdDisplay.setScore(2);
            }
            else{
                String firstPlayer = snowballSwoopPlayers.get(0).getPlayer().getPlayerListName();
                int firstPlayerScore = snowballSwoopPlayers.get(0).getPoints();
                Score firstDisplay = objective.getScore(ChatColor.WHITE + "1: " + firstPlayer + " " + ChatColor.AQUA + firstPlayerScore);
                firstDisplay.setScore(4);
                String secondPlayer = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 3).getPlayer().getPlayerListName();
                int secondPlayerScore = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 3).getPoints();
                Score secondDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.size() - 2) + ": " + secondPlayer + " " + ChatColor.AQUA + secondPlayerScore);
                secondDisplay.setScore(3);
                String thirdPlayer = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 2).getPlayer().getPlayerListName();
                int thirdPlayerScore = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 2).getPoints();
                Score thirdDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.size() - 1) + ": " + thirdPlayer + " " + ChatColor.AQUA + thirdPlayerScore);
                thirdDisplay.setScore(2);
                String fourthPlayer = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 1).getPlayer().getPlayerListName();
                int fourthPlayerScore = snowballSwoopPlayers.get(snowballSwoopPlayers.size() - 1).getPoints();
                Score fourthDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.size()) + ": " + fourthPlayer + " " + ChatColor.AQUA + fourthPlayerScore);
                fourthDisplay.setScore(1);
            }
        }
        else{
            String firstPlayer = snowballSwoopPlayers.get(0).getPlayer().getPlayerListName();
            int firstPlayerScore = snowballSwoopPlayers.get(0).getPoints();
            Score firstDisplay = objective.getScore(ChatColor.WHITE + "1: " + firstPlayer + " " + ChatColor.AQUA + firstPlayerScore);
            firstDisplay.setScore(4);
            String secondPlayer = snowballSwoopPlayers.get(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) - 1).getPlayer().getPlayerListName();
            int secondPlayerScore = snowballSwoopPlayers.get(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) - 1).getPoints();
            Score secondDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId()))) + ": " + secondPlayer + " " + ChatColor.AQUA + secondPlayerScore);
            secondDisplay.setScore(3);
            String thirdPlayer = player.getPlayerListName();
            int thirdPlayerScore = playerLink.get(player.getUniqueId()).getPoints();
            Score thirdDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) + 1) + ": " + thirdPlayer + " " + ChatColor.AQUA + thirdPlayerScore);
            thirdDisplay.setScore(2);
            String fourthPlayer = snowballSwoopPlayers.get(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) + 1).getPlayer().getPlayerListName();
            int fourthPlayerScore = snowballSwoopPlayers.get(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) + 1).getPoints();
            Score fourthDisplay = objective.getScore(ChatColor.WHITE + String.valueOf(snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) + 2) + ": " + fourthPlayer + " " + ChatColor.AQUA + fourthPlayerScore);
            fourthDisplay.setScore(1);
        }

        player.setScoreboard(scoreboard);
    }*/

    public void onEnd(){
        HashMap<UUID, Integer> playerPoints = new HashMap<>();
        for (SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()) {
            if(snowballSwoopPlayer == null)
                continue;
            snowballSwoopPlayer.getPlayer().sendTitle(ChatColor.RED + "Minigame Over!", ChatColor.RED + "GG", 2, 90, 2);
            snowballSwoopPlayer.getPlayer().playSound(snowballSwoopPlayer.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
            if(!snowballSwoopPlayer.isEliminated()){
                snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GRAY + "You survived the entire minigame. " + ChatColor.AQUA + "(+150 points)");
                snowballSwoopPlayer.givePoints(150);
            }
            endMessage(snowballSwoopPlayer.getPlayer());
            snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GRAY + "Transporting you back to reality!");
            playerPoints.put(snowballSwoopPlayer.getUuid(), snowballSwoopPlayer.getPoints());
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                for(SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()){
                    snowballSwoopPlayer.getPlayer().teleport(snowballSwoopPlayer.getOriginalLocation());
                    snowballSwoopPlayer.getPlayer().getInventory().setContents(snowballSwoopPlayer.getOriginalInventory().getContents());
                    snowballSwoopPlayer.getPlayer().updateInventory();
                    snowballSwoopPlayer.getGameScoreboard().destroy();
                }
                SnowballSwoopGameEndEvent event = new SnowballSwoopGameEndEvent(playerPoints);
                Bukkit.getServer().getPluginManager().callEvent(event);
                playerLink.clear();
                gameState = GameState.ENDED;
            }
        }.runTaskLater(SnowballSwoop.getInstance(), 200L);
    }

    public void endMessage(Player player){
        List<SnowballSwoopPlayer> snowballSwoopPlayers = playerLink.values().stream().sorted(Comparator.comparingInt(SnowballSwoopPlayer::getPoints).reversed()).collect(Collectors.toList());
        player.sendMessage(ChatColor.GREEN + "You placed " + ChatColor.AQUA + (snowballSwoopPlayers.indexOf(playerLink.get(player.getUniqueId())) + 1) + ChatColor.GREEN + " with " + ChatColor.AQUA + playerLink.get(player.getUniqueId()).getPoints() + ChatColor.GREEN + " points.");
        player.sendMessage(ChatColor.WHITE + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        player.sendMessage(ChatColor.RED + "Top Players in this Minigame:");
        if(snowballSwoopPlayers.size() >= 1){
            player.sendMessage(ChatColor.GOLD + "1: " + ChatColor.WHITE + snowballSwoopPlayers.get(0).getPlayer().getPlayerListName() + ChatColor.AQUA + " " + snowballSwoopPlayers.get(0).getPoints());
        }
        if(snowballSwoopPlayers.size() >= 2){
            player.sendMessage(ChatColor.GRAY + "2: " + ChatColor.WHITE + snowballSwoopPlayers.get(1).getPlayer().getPlayerListName() + ChatColor.AQUA + " " + snowballSwoopPlayers.get(1).getPoints());
        }
        if(snowballSwoopPlayers.size() >= 3){
            player.sendMessage(ChatColor.DARK_RED + "3: " + ChatColor.WHITE + snowballSwoopPlayers.get(2).getPlayer().getPlayerListName() + ChatColor.AQUA + " " + snowballSwoopPlayers.get(2).getPoints());
        }
        if(snowballSwoopPlayers.size() >= 4){
            player.sendMessage(ChatColor.GREEN + "4: " + ChatColor.WHITE + snowballSwoopPlayers.get(3).getPlayer().getPlayerListName() + ChatColor.AQUA + " " + snowballSwoopPlayers.get(3).getPoints());
        }
        if(snowballSwoopPlayers.size() >= 5){
            player.sendMessage(ChatColor.GREEN + "5: " + ChatColor.WHITE + snowballSwoopPlayers.get(4).getPlayer().getPlayerListName() + ChatColor.AQUA + " " + snowballSwoopPlayers.get(4).getPoints());
        }
        player.sendMessage(ChatColor.WHITE + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        if(!(event.getEntity() instanceof Snowball))
            return;
        if(!(event.getEntity().getShooter() instanceof Player))
            return;
        Player player = (Player) event.getEntity().getShooter();
        if(!playerLink.containsKey(player.getUniqueId()))
            return;
        if(player.getInventory().getItemInMainHand().getItemMeta() == null)
            return;
        if(player.getInventory().getItemInMainHand().getItemMeta().getLore() == null)
            return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ItemManager.Snowball.getItemMeta().getLore().get(0)))
            return;
        if (ExplosiveSnowball.explosiveActivated.contains(player)) {
            event.getEntity().setCustomName("Explosive Snowball");
        } else if (TripleShot.tripleActivated.contains(player)) {
            event.getEntity().setCustomName("Shot Snowball");
            Snowball snowball1 = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Snowball.class);
            snowball1.setCustomName("Shot Snowball");
            snowball1.setShooter(player);
            snowball1.setVelocity(event.getEntity().getVelocity().rotateAroundY(Math.toRadians(10)));
            Snowball snowball2 = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Snowball.class);
            snowball2.setCustomName("Shot Snowball");
            snowball2.setShooter(player);
            snowball2.setVelocity(event.getEntity().getVelocity().rotateAroundY(Math.toRadians(-10)));
        } else {
            event.getEntity().setCustomName("Shot Snowball");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!playerLink.containsKey(player.getUniqueId()))
            return;
        if (gameState.equals(GameState.WAITING) || gameState.equals(GameState.GRACE) || gameState.equals(GameState.ENDED))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event){
        if(event.getEntity().getCustomName() == null)
            return;
        if(!(event.getEntity().getCustomName().equals("Shot Snowball") || event.getEntity().getCustomName().equals("Explosive Snowball")))
            return;
        if (event.getEntity().getCustomName().equals("Explosive Snowball")) {
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 3, false, true);
        }
        if (event.getHitBlock() != null && event.getHitBlock().getType() != Material.BEDROCK && event.getHitBlock().getType() != Material.COMMAND_BLOCK && event.getHitBlock().getType() != Material.CHAIN_COMMAND_BLOCK && event.getHitBlock().getType() != Material.REPEATING_COMMAND_BLOCK) {
            event.getHitBlock().setType(Material.AIR);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_SNOW_BREAK, 5, 5);
        }
        if (event.getHitEntity() != null && event.getHitEntity() instanceof Player hitPlayer) {
            Player shootPlayer = (Player) event.getEntity().getShooter();
            if(hitPlayer.equals(shootPlayer))
                return;
            if(!playerLink.containsKey(hitPlayer.getUniqueId()))
                return;
            if (Invincibility.checkCooldown(hitPlayer)) {
                if (hitPlayer.getHealth() == 2) {
                    if (hitPlayer.getPlayer() == null || !hitPlayer.getPlayer().isOnline()) return;
                    hitPlayer.setGameMode(GameMode.SPECTATOR);
                    playerLink.get(hitPlayer.getUniqueId()).setEliminated(true);
                    shootPlayer.sendMessage(ChatColor.RED + "You have successfully eliminated " + hitPlayer.getPlayerListName() + ". " + ChatColor.AQUA + "(+50 points)");
                    playerLink.get(shootPlayer.getUniqueId()).givePoints(50);
                    playerLink.get(hitPlayer.getUniqueId()).setEliminations(playerLink.get(hitPlayer.getUniqueId()).getEliminations() + 1);
                    hitPlayer.sendMessage(ChatColor.RED + "You were eliminated by " + shootPlayer.getPlayerListName());

                    Firework firework1 = hitPlayer.getWorld().spawn(hitPlayer.getLocation().add(0, 1, 0), Firework.class);
                    FireworkMeta data1 = (FireworkMeta) firework1.getFireworkMeta();
                    data1.addEffects(FireworkEffect.builder().withColor(Color.RED).withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                    data1.setPower(1);
                    firework1.setFireworkMeta(data1);
                    firework1.detonate();

                    for (SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()) {
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.GOLD + hitPlayer.getPlayerListName() + ChatColor.RED + " has been eliminated by " + ChatColor.GOLD + shootPlayer.getPlayerListName());
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.RED + String.valueOf((int) playerLink.values().stream().filter(player -> !player.isEliminated()).count()) + " players remain!");
                    }
                    Optional<SnowballSwoopPlayer> lastPlayer = playerLink.values().stream()
                            .filter(player -> !player.isEliminated())
                            .reduce((first, second) -> {
                                throw new IllegalStateException("Multiple players still remain");
                            });
                    lastPlayer.ifPresent(player -> onEnd());
                } else {
                    hitPlayer.setHealth(hitPlayer.getHealth() - 2);
                    shootPlayer.sendMessage(ChatColor.GRAY + "You successfully hit " + ChatColor.GOLD + hitPlayer.getPlayerListName() + ChatColor.GRAY + ". " + ChatColor.AQUA + "(+25 points)");
                    playerLink.get(shootPlayer.getUniqueId()).givePoints(25);
                    Invincibility.setCooldown(hitPlayer, 2);
                    hitPlayer.sendMessage(ChatColor.RED + "You were hit by " + shootPlayer.getPlayerListName());

                    Firework firework1 = hitPlayer.getWorld().spawn(hitPlayer.getLocation().add(0, 1, 0), Firework.class);
                    FireworkMeta data1 = (FireworkMeta) firework1.getFireworkMeta();
                    data1.addEffects(FireworkEffect.builder().withColor(Color.RED).withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                    data1.setPower(1);
                    firework1.setFireworkMeta(data1);
                    firework1.detonate();

                    Random r = new Random();
                    for (SnowballSwoopPlayer snowballSwoopPlayer : playerLink.values()) {
                        String quote = hitQuotes[r.nextInt(hitQuotes.length)];
                        quote = quote.replaceAll("<hit>", hitPlayer.getPlayerListName());
                        snowballSwoopPlayer.getPlayer().sendMessage(ChatColor.YELLOW + quote + ChatColor.RED + " They now have " + hitPlayer.getHealth() / 2 + " lives left");
                    }
                }
            } else {
                shootPlayer.sendMessage("This player is currently invincible!");
            }
        }
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        if(!playerLink.containsKey(event.getEntity().getUniqueId()))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        if(!playerLink.containsKey(event.getEntity().getUniqueId()))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        if(!playerLink.containsKey(event.getEntity().getUniqueId()))
            return;
        event.setDamage(0);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(!playerLink.containsKey(event.getPlayer().getUniqueId()))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(!gameState.equals(GameState.STARTED))
            return;
        if(!playerLink.containsKey(event.getPlayer().getUniqueId()))
            return;
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        playerLink.get(event.getPlayer().getUniqueId()).setEliminated(true);

        Optional<SnowballSwoopPlayer> lastPlayer = playerLink.values().stream()
                .filter(player -> !player.isEliminated())
                .reduce((first, second) -> {
                    throw new IllegalStateException("Multiple players still remain");
                });
        lastPlayer.ifPresent(player -> onEnd());

        if(playerLink.values().stream().allMatch(SnowballSwoopPlayer::isEliminated))
            onEnd();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!(gameState.equals(GameState.STARTED) || gameState.equals(GameState.GRACE)))
            return;
        if(!playerLink.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }
        playerLink.get(event.getPlayer().getUniqueId()).getGameScoreboard().displayScoreboard();
    }
}

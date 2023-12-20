package me.syntaxerror.snowballswoop.scoreboard;

import me.syntaxerror.snowballswoop.SnowballSwoopPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class ScoreboardSection {
    private final HashMap<SnowballSwoopPlayer, Integer> index = new HashMap<>();
    private final Set<SnowballSwoopPlayer> players = new HashSet<>();
    /**
     *
     * @return the index in the scoreboard
     * @throws IllegalStateException exception if it's not in the list
     */
    public final int getIndex(SnowballSwoopPlayer player) {
        if (!index.containsKey(player)) throw new IllegalStateException("Not in list!");
        return index.get(player);
    }
    public void suspendAll() {
        for (SnowballSwoopPlayer player : new HashSet<>(players))
            suspend(player);
    }
    public void suspend(SnowballSwoopPlayer player) {
        int index = getIndex(player);
        this.index.remove(player);
        players.remove(player);
        player.getScoreboardSections().remove(index);
    }

    public static void addToScoreboard(int index, ScoreboardSection scoreboardSection, SnowballSwoopPlayer player) {
        player.getScoreboardSections().add(index, scoreboardSection);
        scoreboardSection.players.add(player);
        scoreboardSection.index.put(player, index);
        player.getGameScoreboard().updateScoreboard();
    }
    public static void addToScoreboard(ScoreboardSection scoreboardSection, SnowballSwoopPlayer player) {
        addToScoreboard(player.getScoreboardSections().size(), scoreboardSection, player);
    }
    public void addToScoreboard(SnowballSwoopPlayer player) {
        addToScoreboard(player.getScoreboardSections().size(), this, player);
    }
    public abstract String[] score(SnowballSwoopPlayer player);
}

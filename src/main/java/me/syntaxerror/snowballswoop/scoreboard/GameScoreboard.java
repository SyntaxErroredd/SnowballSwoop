package me.syntaxerror.snowballswoop.scoreboard;

import me.syntaxerror.snowballswoop.SnowballSwoopPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public class GameScoreboard {

    private final SnowballSwoopPlayer player;

    public GameScoreboard(SnowballSwoopPlayer player) {
        this.player = player;
    }

    public void updateScoreboard() {
        int score = 0;
        for (ScoreboardSection scoreboardSection : player.getScoreboardSections()) {
            for (String s : scoreboardSection.score(player)) {
                ScoreboardDisplayer.setScore(player.getPlayer(), s, score + 14);
                score--;
            }
        }
        if (Math.abs(score) < player.getPlayer().getScoreboard().getEntries().size()) {
            for (int i = Math.abs(score); i <= player.getPlayer().getScoreboard().getEntries().size(); i++) {
                Team t = player.getPlayer().getScoreboard().getTeam("score-" + ((-i) + 14));
                if(t == null)
                    continue;
                t.removeEntry(ScoreboardDisplayer.buildString(Math.abs((-i) + 14)));
                t.unregister();
                player.getPlayer().getScoreboard().resetScores("score-" + ((-i) + 14));
                player.getPlayer().getScoreboard().resetScores(ScoreboardDisplayer.buildString(Math.abs((-i) + 14)));
            }
        }
    }

    public void createScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("minigame", "dummy", ChatColor.GOLD + "MCMT");
        player.getPlayer().setScoreboard(board);
        obj.setRenderType(RenderType.INTEGER);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (ScoreboardImpl i : ScoreboardImpl.values())
            ScoreboardSection.addToScoreboard(i.getSection(), player);
    }

    public void displayScoreboard(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("minigame", "dummy", ChatColor.GOLD + "MCMT");
        player.getPlayer().setScoreboard(board);
        obj.setRenderType(RenderType.INTEGER);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void destroy() {
        player.getPlayer().getScoreboard().getObjectives().forEach(Objective::unregister);
    }
}

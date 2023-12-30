package me.syntaxerror.snowballswoop.scoreboard;

import me.syntaxerror.snowballswoop.SnowballSwoopGame;
import me.syntaxerror.snowballswoop.SnowballSwoopPlayer;
import me.syntaxerror.snowballswoop.util.Factory;
import me.syntaxerror.snowballswoop.util.Tools;

import java.util.*;
import java.util.stream.Collectors;

public enum ScoreboardImpl implements Factory<SnowballSwoopPlayer, String[]> {
    Header {
        @Override
        public String[] factor(SnowballSwoopPlayer player) {
            return new String[]{"§aGame: Snowball Swoop", "§§§§", "§fMinigame Placements:"};
        }
    },
    Placements {
        @Override
        public String[] factor(SnowballSwoopPlayer player) {
            List<SnowballSwoopPlayer> players = player.getSnowballSwoopGame().getRegisteredPlayers().stream().sorted(Comparator.comparingInt(SnowballSwoopPlayer::getPoints)).collect(Collectors.toList());
            Collections.reverse(players);
            int yourPosition = 0;
            for (SnowballSwoopPlayer p : players) {
                yourPosition++;
                if (p == player) break;
            }
            String[] out;
            if (yourPosition > 3 && players.size() > 3)
                out = new String[]{makeLine(players, 1, player), makeLine(players, 2, player), makeLine(players, 3, player), makeLine(players, yourPosition, player)};
            else {
                out = switch (players.size()) {
                    case 1 -> new String[]{makeLine(players, 1, player)};
                    case 2 -> new String[]{makeLine(players, 1, player), makeLine(players, 2, player)};
                    default -> new String[]{makeLine(players, 1, player), makeLine(players, 2, player), makeLine(players, 3, player)};
                };
            }
            return out;

        }
        private String makeLine(List<SnowballSwoopPlayer> players, int place, SnowballSwoopPlayer owner) {
            SnowballSwoopPlayer player = players.get(place - 1);
            return place + ": " + ((player == owner) ? "§6You" : player.getName()) + " §b" + player.getPoints();
        }
    },
    EmptyLine {
        @Override
        public String[] factor(SnowballSwoopPlayer player) {
            return new String[]{"§7§7§7§7"};
        }
    },
    Time{
        @Override
        public String[] factor(SnowballSwoopPlayer player) {
            return new String[]{"§aTime Left: " + Tools.formatSeconds(player.getSnowballSwoopGame().INSTANCE.time)};
        }
    };

    public ScoreboardSectionFactory getSection() {
        ScoreboardSectionFactory factory = ScoreboardSectionFactory.factorys.get(this);
        return (factory == null) ? new ScoreboardSectionFactory(this) : factory;
    }
    public static class ScoreboardSectionFactory extends ScoreboardSection {
        private static final HashMap<ScoreboardImpl, ScoreboardSectionFactory> factorys = new HashMap<>();
        private final ScoreboardImpl impl;
        public ScoreboardSectionFactory(ScoreboardImpl impl) {
            this.impl = impl;
            factorys.put(impl, this);
        }

        @Override
        public String[] score(SnowballSwoopPlayer player) {
            return impl.factor(player);
        }
    }
}
package me.syntaxerror.snowballswoop.util;

@SuppressWarnings("unused")
public class Tools {

    public static String formatSeconds(long seconds) {
        int minutes = (int) (seconds / 60d);
        int sec = (int) (seconds - (minutes * 60));
        return minutes + ":" + ((sec < 10) ? "0" : "") + (sec);
    }
}

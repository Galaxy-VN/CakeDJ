package me.JustAPie.CakeDJ.Utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class Commons {
    public static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static String getConfig(String key) {
        key = key.toUpperCase();
        return Dotenv.load().get(key);
    }

    public static boolean isNaN(String s) {
        try {
            Long.parseLong(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static String getProgressBar(AudioTrack track) {
        float percentage = (100f / track.getDuration() * track.getPosition());
        return "`" + TimeUtils.formatTime(track.getPosition()) + "` [" + StringUtils.repeat("▬", (int) Math.round((double) percentage / 10)) +
                "](https://github.com/JustAPieOP/JustADJ-JDA)" +
                StringUtils.repeat("▬", 10 - (int) Math.round((double) percentage / 10)) +
                " `" + TimeUtils.formatTime(track.getDuration()) + "`";
    }
}

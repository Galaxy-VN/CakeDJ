package me.JustAPie.CakeDJ.Utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class Commons {
    public static final String mentionRegex = Pattern.compile("(<@)([1-9])\\d\\w+(>)").pattern();
    public static final MessageEmbed welcomeEmbed = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle("Thanks for using the bot!")
            .setDescription(
                    "Before using the bot, make sure you have configured the bot properly"
                            + "\nMy default prefix is `cd!`. You can change it using `cd!prefix (prefix)`"
                            + "\nIf you are going to restrict the bot to operate on specified channels"
                            + ", turn on the channel restrict mode using cd!channelrestrict and make sure you have added a channel for the bot to work"
                            + "\nFor other things, please check cd!help for more commands. Happy listening!"
            )
            .setFooter("For more support. Contact <@" + Commons.getConfig("ownerid") + ">")
            .build();

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

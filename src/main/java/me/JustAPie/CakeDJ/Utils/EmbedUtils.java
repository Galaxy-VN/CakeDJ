package me.JustAPie.CakeDJ.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class EmbedUtils {
    public static void errorMessage(TextChannel channel, String msg) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription(msg)
                        .build()
        ).queue();
    }

    public static void errorMessage(TextChannel channel, String msg, String title) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(title)
                        .setDescription(msg)
                        .build()
        ).queue();
    }

    public static void successMessage(TextChannel channel, String msg) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setDescription(msg)
                        .build()
        ).queue();
    }

    public static void successMessage(TextChannel channel, String title, String msg) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle(title)
                        .setDescription(msg)
                        .build()
        ).queue();
    }
}

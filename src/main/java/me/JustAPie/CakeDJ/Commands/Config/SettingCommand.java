package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Models.GuildConfig;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SettingCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildConfig config = DatabaseUtils.getGuildSetting(ctx.getGuild());
        List<String> channels = new ArrayList<>();
        for (String s : config.djOnlyChannels()) {
            String c = "#" + ctx.getGuild().getGuildChannelById(s).getName();
            channels.add(c);
        }
        ctx.getChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.YELLOW)
                        .setAuthor(ctx.getGuild().getName(), null, ctx.getGuild().getIconUrl())
                        .setTitle("Setting for " + ctx.getGuild().getName())
                        .addField("Prefix", config.prefix(), true)
                        .addField("Default Volume", String.valueOf(config.defaultVolume()), true)
                        .addField("Maximum Queue Length", String.valueOf(config.maxQueueLength()), true)
                        .addField("Maximum Song per Member", String.valueOf(config.maxSongsPerUser()), true)
                        .addField("Restrict Mode", (config.channelRestrict() ? "Enabled" : "Disabled"), true)
                        .addField("DJ-Only Channels",
                                (config.djOnlyChannels().isEmpty() ? "Empty" : "`" + String.join(", ", channels) + "`"),
                                true)
                        .addField(
                                "Voice leaving timeout",
                                config.leaveTimeout() / 1000
                                        + (config.leaveTimeout() / 1000 > 1 ? " seconds" : " second"),
                                true)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "setting";
    }

    @Override
    public String getHelp() {
        return "Show the bot setting for this guild";
    }

    @Override
    public String getCategory() {
        return "Config";
    }

    @Override
    public String getUsage() {
        return "setting";
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}

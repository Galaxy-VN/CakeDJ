package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;

import java.awt.*;
import java.util.List;

public class ChannelManageCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String operation = ctx.getArgs().get(0);
        switch (operation) {
            case "add":
                if (ctx.getArgs().size() < 2) {
                    EmbedUtils.errorMessage(ctx.getChannel(), "Please enter channel ID");
                    return;
                }
                String toAdd = ctx.getArgs().get(1);
                if (
                        Commons.isNaN(toAdd)
                        || ctx.getGuild().getGuildChannelById(toAdd) == null
                ) {
                    EmbedUtils.errorMessage(ctx.getChannel(), "Invalid channel");
                    return;
                }
                List<String> list = DatabaseUtils.getGuildSetting(ctx.getGuild()).djOnlyChannels();
                list.add(toAdd);
                DatabaseUtils.updateGuildSetting(ctx.getGuild(), "djOnlyChannels", list);
                EmbedUtils.successMessage(ctx.getChannel(), "Channel added");
                break;
            case "remove":
                List<String> channels = DatabaseUtils.getGuildSetting(ctx.getGuild()).djOnlyChannels();
                if (channels.isEmpty()) {
                    EmbedUtils.errorMessage(ctx.getChannel(), "There's nothing for you to remove");
                    return;
                }
                if (ctx.getArgs().size() < 2) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Channels List");
                    for (String s : channels) {
                        GuildChannel channel;
                        if (ctx.getGuild().getGuildChannelById(s) == null) continue;
                        else channel = ctx.getGuild().getGuildChannelById(s);
                        builder.addField(
                                "`" + channels.indexOf(s) + "` `" + channel.getName() + "`",
                                "ID: " + channel.getId(),
                                false
                        );
                    }
                    ctx.getChannel().sendMessage(builder.build()).queue();
                }
                String toRemove = ctx.getArgs().get(1);
                if (Commons.isNaN(toRemove)) {
                    EmbedUtils.errorMessage(ctx.getChannel(), "Invalid ID");
                    return;
                }
                int toRM = Integer.parseInt(toRemove);
                channels.remove(toRM);
                DatabaseUtils.updateGuildSetting(ctx.getGuild(), "djOnlyChannels", channels);
                EmbedUtils.successMessage(ctx.getChannel(), "Successfully remove channel");
                break;
            default:
                EmbedUtils.errorMessage(ctx.getChannel(), "Available options are `add, remove`");
                break;
        }
    }

    @Override
    public String getName() {
        return "channelmanage";
    }

    @Override
    public String getHelp() {
        return "Add or remove channel that the bot will work";
    }

    @Override
    public String getCategory() {
        return "Config";
    }

    @Override
    public String getUsage() {
        return "channelmanage (add/remove) (channel id) [channel index]";
    }

    @Override
    public int argsLength() {
        return 1;
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}

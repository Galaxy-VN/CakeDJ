package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class ChannelRestrictCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        boolean channelRestrict = !DatabaseUtils.getGuildSetting(ctx.getGuild()).channelRestrict();
        List<String> channels = DatabaseUtils.getGuildSetting(ctx.getGuild()).djOnlyChannels();
        if (channels.isEmpty()) {
            EmbedUtils.errorMessage(ctx.getChannel(),
                    "Please add at least one channel to the unrestricted channels before enabling channel restrict function",
                    "WARNING!");
            return;
        }
        String status = channelRestrict ? "on" : "off";
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "channelRestrict", channelRestrict);
        EmbedUtils.successMessage(ctx.getChannel(), "Turned " + status + " restrict mode");
    }

    @Override
    public String getName() {
        return "channelrestrict";
    }

    @Override
    public String getHelp() {
        return "Decide whether restrict mode is enabled";
    }

    @Override
    public String getCategory() {
        return "Config";
    }

    @Override
    public String getUsage() {
        return "channelRestrict";
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}

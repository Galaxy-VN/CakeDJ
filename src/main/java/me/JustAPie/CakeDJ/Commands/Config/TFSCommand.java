package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class TFSCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        boolean is247 = !DatabaseUtils.getGuildSetting(ctx.getGuild()).is247();
        String status = is247 ? "on" : "off";
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "is247", is247);
        EmbedUtils.successMessage(ctx.getChannel(), "Turned " + status + " 24/7 function");
    }

    @Override
    public String getName() {
        return "tfs";
    }

    @Override
    public String getHelp() {
        return "Set the bot to play music 24/7";
    }

    @Override
    public String getCategory() {
        return "Config";
    }

    @Override
    public String getUsage() {
        return "tfs";
    }

    @Override
    public List<String> getAliases() {
        return List.of("247", "24/7");
    }
}

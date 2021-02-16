package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class DefaultVolumeCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toSetStr = ctx.getArgs().get(0);
        if (Commons.isNaN(toSetStr)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid volume");
            return;
        }
        int toSet = Integer.parseInt(toSetStr);
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "defaultVolume", toSet);
        EmbedUtils.successMessage(ctx.getChannel(), "Successfully set the default volume to `" + toSetStr + "%`");
    }

    @Override
    public String getName() {
        return "defaultvolume";
    }

    @Override
    public String getHelp() {
        return "Change the default volume of the playing song";
    }

    @Override
    public String getCategory() { return "Config"; }

    @Override
    public String getUsage() {
        return "volume (volume)";
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }

    @Override
    public int argsLength() {
        return 1;
    }
}

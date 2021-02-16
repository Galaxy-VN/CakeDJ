package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class MaxSongPerUserCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toSetStr = ctx.getArgs().get(0);
        if (Commons.isNaN(toSetStr)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid length");
            return;
        }
        int toSet = Integer.parseInt(toSetStr);
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "maxSongsPerUser", toSet);
        EmbedUtils.successMessage(ctx.getChannel(), "Successfully set the max song per user to `" + toSetStr + "`");
    }

    @Override
    public String getName() {
        return "maxsongperuser";
    }

    @Override
    public String getHelp() {
        return "Change the maximum song per user of your server";
    }

    @Override
    public String getCategory() { return "Config"; }

    @Override
    public String getUsage() {
        return "maxsongperuser (length)";
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

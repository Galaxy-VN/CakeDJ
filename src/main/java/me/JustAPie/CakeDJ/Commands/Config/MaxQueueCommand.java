package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class MaxQueueCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toSetStr = ctx.getArgs().get(0);
        if (Commons.isNaN(toSetStr)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid length");
            return;
        }
        int toSet = Integer.parseInt(toSetStr);
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "maxQueueLength", toSet);
        EmbedUtils.successMessage(ctx.getChannel(), "Successfully set the max queue length to `" + toSetStr + "`");
    }

    @Override
    public String getName() {
        return "maxqueue";
    }

    @Override
    public String getHelp() {
        return "Change the maximum queue length of your server";
    }

    @Override
    public String getCategory() { return "Config"; }

    @Override
    public String getUsage() {
        return "maxqueue (length)";
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

package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class LeaveTimeout implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String r = ctx.getArgs().get(0);
        if (Commons.isNaN(r)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid number");
            return;
        }
        long s = Long.parseLong(r) * 1000;
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "leaveTimeout", s);
        EmbedUtils.successMessage(ctx.getChannel(), "Updated leave timeout to `" + r + (s > 1000 ? " seconds" : "second") + "`");
    }

    @Override
    public String getName() {
        return "leavetimeout";
    }

    @Override
    public String getHelp() {
        return "Set the leave timeout for the bot";
    }

    @Override
    public String getCategory() {
        return "config";
    }

    @Override
    public String getUsage() {
        return "leavetimeout (second)";
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}

package me.JustAPie.CakeDJ.Commands.Config;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class PrefixCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        DatabaseUtils.updateGuildSetting(ctx.getGuild(), "prefix", ctx.getArgs().get(0));
        EmbedUtils.successMessage(ctx.getChannel(), "Updated prefix to `" + ctx.getArgs().get(0) + "`");
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getHelp() {
        return "Set the prefix for your server";
    }

    @Override
    public String getCategory() {
        return "Config";
    }

    @Override
    public String getUsage() {
        return "prefix (prefix)";
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

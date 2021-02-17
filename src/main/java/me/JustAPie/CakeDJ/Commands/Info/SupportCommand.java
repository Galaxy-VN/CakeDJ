package me.JustAPie.CakeDJ.Commands.Info;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class SupportCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String invitation = "[Support Server](" + "https://discord.gg/v53RR4kz6Z" + ")\n";
        String owner = "Owner: " + ctx.getJDA().getUserById(Commons.getConfig("ownerid")).getAsTag();
        EmbedUtils.successMessage(ctx.getChannel(), "Support Information", invitation + owner);
    }

    @Override
    public String getName() {
        return "support";
    }

    @Override
    public String getHelp() {
        return "Information about getting support";
    }

    @Override
    public String getCategory() {
        return "Info";
    }

    @Override
    public String getUsage() {
        return "support";
    }
}

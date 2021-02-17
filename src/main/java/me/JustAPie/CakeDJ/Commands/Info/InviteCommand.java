package me.JustAPie.CakeDJ.Commands.Info;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;

public class InviteCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String link = ctx.getJDA().getInviteUrl(
                Permission.VOICE_CONNECT,
                Permission.VOICE_SPEAK,
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_HISTORY
        );
        EmbedUtils.successMessage(ctx.getChannel(), "[Click to invite your bot to your server]" + "(" + link + ")");
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return "Invite this bot to your server";
    }

    @Override
    public String getCategory() {
        return "Info";
    }

    @Override
    public String getUsage() {
        return "invite";
    }
}

package me.JustAPie.CakeDJ.Commands.Info;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class PingCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        ctx.getJDA().getRestPing().queue((ping) -> {
            ctx.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setDescription(
                                    String.format("🏓 Ping: %dms\n⚜ WS Ping: %dms", ping, ctx.getJDA().getGatewayPing())
                            ).build()
            ).queue();
        });
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Show the ping between the bot and the server";
    }

    @Override
    public String getCategory() {
        return "Info";
    }

    @Override
    public String getUsage() {
        return "ping";
    }
}

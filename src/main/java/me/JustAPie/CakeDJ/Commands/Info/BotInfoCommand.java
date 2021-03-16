package me.JustAPie.CakeDJ.Commands.Info;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;

public class BotInfoCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        SelfUser self = ctx.getJDA().getSelfUser();
        ctx.getChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle("Statistics")
                        .setThumbnail(self.getAvatarUrl())
                        .addField("OS", System.getProperty("os.name"), true)
                        .addField("Java version", System.getProperty("java.version"), true)
                        .addField("Owner", "<@" + Commons.getConfig("owner") + ">", true)
                        .addField("Bot created in", TimeUtils.formatDate(self.getTimeCreated()), true)
                        .addField("Total servers", String.valueOf(ctx.getJDA().getGuilds().size()), true)
                        .addField("Total shards", String.valueOf(ctx.getShardManager().getShardsTotal()), true)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public String getHelp() {
        return "Bot Information";
    }

    @Override
    public String getCategory() {
        return "Info";
    }

    @Override
    public String getUsage() {
        return "botinfo";
    }
}

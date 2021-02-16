package me.JustAPie.CakeDJ.Commands.Info;

import com.jagrosh.jdautilities.command.Command;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.CommandManager;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements ICommand {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void exec(CommandContext ctx)  {
        String prefix = DatabaseUtils.getGuildSetting(ctx.getGuild()).prefix;
        if (ctx.getArgs().size() == 0) {
            StringBuilder str = new StringBuilder();
            List<String> cat = this.manager.commands.stream().map(ICommand::getCategory).distinct().collect(Collectors.toList());
            cat.forEach(
                    (ct) -> {
                        if (Collections.singletonList(cat).indexOf(ct) != 0) str.append("\n");
                        str.append("**__").append(ct).append("__**\n");
                        this.manager.commands.stream().filter(
                                (cmd) -> cmd.getCategory().equalsIgnoreCase(ct)
                        ).map(ICommand::getName).collect(Collectors.toList()).forEach(
                                (name) -> str.append('`').append(name).append("` ")
                        );
                    }
            );
            ctx.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.YELLOW)
                            .setTitle("List of commands")
                            .setDescription(str)
                            .setFooter("Prefix: " + prefix)
                            .build()
            ).queue();
            return;
        }

        ICommand cmd = this.manager.getCommand(ctx.getArgs().get(0));
        assert cmd != null;
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.yellow)
                .setTitle("Help for " + ctx.getArgs().get(0))
                .addField("Name", cmd.getName(), false)
                .addField("Description", cmd.getHelp(), false)
                .addField("Category", cmd.getCategory(), false)
                .addField("Usage", "`" + prefix + cmd.getUsage() + "`", false);
        if (isOwnerCommand())
            builder.addField("Owner permission", isOwnerCommand() ? "Yes" : "No", false);
        if (cmd.getAliases().size() != 0) {
            StringBuilder aliasesBuilder = new StringBuilder();
            cmd.getAliases().forEach(
                    (alias) -> aliasesBuilder.append("`").append(alias).append("` ")
            );
            builder.addField("Aliases", aliasesBuilder.toString(), false);
        }
        ctx.getChannel().sendMessage(builder.build()).queue();
    }


    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Show information of commands";
    }

    @Override
    public String getCategory() {
        return "Info";
    }

    @Override
    public String getUsage() {
        return "help [command]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("halp");
    }
}


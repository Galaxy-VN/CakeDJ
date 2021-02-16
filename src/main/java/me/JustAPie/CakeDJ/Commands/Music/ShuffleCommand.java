package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class ShuffleCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler.shuffle();
        EmbedUtils.successMessage(ctx.getChannel(), "Shuffled the queue");
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp() {
        return "shuffle";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "shuffle";
    }

    @Override
    public List<String> getAliases() {
        return List.of("random", "randomize");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }
}

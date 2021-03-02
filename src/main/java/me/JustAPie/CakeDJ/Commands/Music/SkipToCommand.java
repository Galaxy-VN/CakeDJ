package me.JustAPie.CakeDJ.Commands.Music;

import java.util.List;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class SkipToCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toSetStr = ctx.getArgs().get(0);
        if (Commons.isNaN(toSetStr)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid volume");
            return;
        }
        int toRemove = Integer.parseInt(toSetStr);
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        for (int i = 0; i < toRemove; ++i) manager.scheduler.queue.removeFirst();
        EmbedUtils.successMessage(ctx.getChannel(), "Remove `" + toRemove + "` " + (toRemove > 1 ? "songs" : "song"));
    }

    @Override
    public String getName() {
        return "skipto";
    }

    @Override
    public String getHelp() {
        return "Skip songs recursively until the specified song is met";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "skipto";
    }

    @Override
    public int argsLength() {
        return 1;
    }

    @Override
    public List<String> getAliases() {
        return List.of("st");
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

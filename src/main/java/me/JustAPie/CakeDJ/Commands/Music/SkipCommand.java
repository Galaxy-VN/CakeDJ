package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class SkipCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager gm = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        if (gm.scheduler.queueLoop) {
            EmbedUtils.successMessage(ctx.getChannel(), "Turning loop mode off");
            gm.scheduler.queueLoop = false;
        }
        gm.scheduler.nextTrack();
        EmbedUtils.successMessage(ctx.getChannel(), "Skipped");
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Head to the next song immediately";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "skip";
    }

    @Override
    public List<String> getAliases() {
        return List.of("s");
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

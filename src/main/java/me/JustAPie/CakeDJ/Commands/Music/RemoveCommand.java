package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Audio.TrackScheduler;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class RemoveCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toChooseStr = ctx.getArgs().get(0);
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = manager.scheduler;
        if (
                Commons.isNaN(toChooseStr)
                || Integer.parseInt(toChooseStr) > scheduler.queue.size()
                || Integer.parseInt(toChooseStr) <= 0
        ) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid song");
            return;
        }
        int toRemove = Integer.parseInt(toChooseStr) - 1;
        scheduler.queue.remove(toRemove);
        EmbedUtils.successMessage(ctx.getChannel(), "The selected song has been removed");
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getHelp() {
        return "Remove selected song";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "remove (song index)";
    }

    @Override
    public int argsLength() {
        return 1;
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

package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Audio.TrackScheduler;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class RepeatCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).scheduler;
        scheduler.queueLoop = !scheduler.queueLoop;
        String status = scheduler.queueLoop ? "on" : "off";
        EmbedUtils.successMessage(ctx.getChannel(), "Turned " + status + " the loop");
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Toggle repeat";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "repeat";
    }

    @Override
    public List<String> getAliases() {
        return List.of("loop");
    }

    @Override
    public int argsLength() {
        return 0;
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

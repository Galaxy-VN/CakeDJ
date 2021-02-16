package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class ClearCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        manager.audioPlayer.destroy();
        EmbedUtils.successMessage(ctx.getChannel(), "Queue cleared");
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Clear the queue";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "clear";
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }
}

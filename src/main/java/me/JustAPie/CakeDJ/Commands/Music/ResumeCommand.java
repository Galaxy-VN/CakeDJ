package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class ResumeCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager gm = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer player = gm.audioPlayer;
        if (!player.isPaused()) {
            EmbedUtils.errorMessage(ctx.getChannel(), "The player is already playing");
            return;
        }
        player.setPaused(false);
        EmbedUtils.successMessage(ctx.getChannel(), "The player has been resumed");
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Resume the player";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "resume";
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

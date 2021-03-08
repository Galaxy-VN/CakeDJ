package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class RestartCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer player = manager.audioPlayer;
        AudioTrack track = player.getPlayingTrack().makeClone();
        manager.scheduler.forceStart(track);
        EmbedUtils.successMessage(ctx.getChannel(), "Restarted the current track");
    }

    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public String getHelp() {
        return "Restart the current track";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "restart";
    }

    @Override
    public List<String> getAliases() {
        return List.of("replay");
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

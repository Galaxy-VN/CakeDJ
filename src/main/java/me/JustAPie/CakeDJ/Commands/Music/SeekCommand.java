package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import me.JustAPie.CakeDJ.Utils.TimeUtils;

public class SeekCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        long toSeek = TimeUtils.parseTime(ctx.getArgs().get(0));
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer player = manager.audioPlayer;
        if (
                toSeek <= 0
                || toSeek >= player.getPlayingTrack().getDuration()
        ) {
            EmbedUtils.errorMessage(
                    ctx.getChannel(),
                    "Invalid time. Time must not longer than the song's duration and must be by format hms. Example `3h4m5s`"
            );
            return;
        }
        player.getPlayingTrack().setPosition(toSeek);
        EmbedUtils.successMessage(
                ctx.getChannel(),
                "The song will now play from `" + TimeUtils.parseLong(toSeek) + "`"
        );
    }

    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public String getHelp() {
        return "Seek to a specified time.";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "seek (time)";
    }

    @Override
    public int argsLength() { return 1; }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }
}

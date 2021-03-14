package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer;
        AudioTrack track = player.getPlayingTrack();
        ctx.getChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.YELLOW)
                        .setAuthor(player.getPlayingTrack().getUserData().toString(),
                                null,
                                ctx.getGuild().getMemberByTag
                                        (track.getUserData().toString()).getUser().getAvatarUrl())
                        .setTitle("Now playing")
                        .addField("Title", Commons.createHyperlink(track.getInfo().uri, track.getInfo().title), false)
                        .addField("Duration", TimeUtils.formatTime(track.getDuration()), false)
                        .addField("Progress", Commons.getProgressBar(track), false)
                        .build()
        ).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Show the playing song";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "nowplaying";
    }

    @Override
    public List<String> getAliases() {
        return List.of("np");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activePlayer() { return true; }
}

package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class PreviousCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        if (guildMusicManager.scheduler.previousTrack.isEmpty()) {
            ctx.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setDescription("There are no previous song")
                            .build()
            ).queue();
            return;
        }
        AudioTrack toAdd = guildMusicManager.scheduler.previousTrack.removeFirst();
        AudioTrack current = guildMusicManager.audioPlayer.getPlayingTrack();
        if (current != null) {
            guildMusicManager.scheduler.queue.addFirst(current.makeClone());
        }
        guildMusicManager.scheduler.queue.addFirst(toAdd);
        guildMusicManager.scheduler.nextTrack();
        EmbedUtils.successMessage(ctx.getChannel(), "Now playing the previous track");
    }

    @Override
    public String getName() {
        return "prev";
    }

    @Override
    public String getHelp() {
        return "Play the previous song";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "previous";
    }

    @Override
    public List<String> getAliases() {
        return List.of("prev");
    }
}

package me.JustAPie.CakeDJ.Commands.Music;

import com.google.common.collect.Lists;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class QueueCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager gm = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        List<AudioTrack> queue = gm.scheduler.queue;
        if (queue.isEmpty()) {
            AudioPlayer player = gm.audioPlayer;
            if (player.getPlayingTrack() == null) {
                EmbedUtils.errorMessage(ctx.getChannel(), "There is no song playing");
            } else new NowPlayingCommand().exec(ctx);
            return;
        }
        if (queue.size() <= 10) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setTitle("Queue")
                    .setDescription("**__Up next__**:");
            for (AudioTrack t : queue) {
                embed.addField("`"+ (queue.indexOf(t) + 1) + " " + t.getInfo().title + "`", "Requested by " + t.getUserData().toString(), false);
            }
            ctx.getChannel().sendMessage(embed.build()).queue();
        } else {
            int i = 1;
            List<List<AudioTrack>> paginatedQueue = Lists.partition(queue, 10);
            paginatedQueue.add(0, List.of());
            if (ctx.getArgs().size() >= 1) {
                String toChoose = ctx.getArgs().get(0);
                if (!Commons.isNaN(toChoose)) i = Integer.parseInt(toChoose);
            }
            List<AudioTrack> chose = paginatedQueue.get(i);
            String prefix = DatabaseUtils.getGuildSetting(ctx.getGuild()).prefix();
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setTitle("Queue")
                    .setDescription("Page " + i + "/" + (paginatedQueue.size() - 1))
                    .setFooter("Type " + prefix + getUsage() + " to show other songs");
            for (AudioTrack tr : chose)
            {
                embed.addField("`"+ (queue.indexOf(tr) + 1) + " " + tr.getInfo().title + "`", "Requested by " + tr.getUserData().toString(), false);
            }
            ctx.getChannel().sendMessage(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "View the queue";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "queue [page num]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("q");
    }

    @Override
    public boolean needConnectedVoice() {
        return true;
    }

    @Override
    public boolean sameVoice() {
        return true;
    }
}

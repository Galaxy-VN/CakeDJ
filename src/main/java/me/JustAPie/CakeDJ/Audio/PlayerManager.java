package me.JustAPie.CakeDJ.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Models.GuildConfig;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import me.JustAPie.CakeDJ.Utils.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, User requester) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            private final GuildConfig config = DatabaseUtils.getGuildSetting(channel.getGuild());
            private final String errorMsg = "Maximum songs exceeded";

            private boolean check(int playlistSize) {
                long userSongs = musicManager.scheduler.queue.stream().filter(
                        (c) -> c.getUserData().equals(requester.getAsTag())
                ).count() + playlistSize;
                long queueLength = musicManager.scheduler.queue.size() + playlistSize;
                if (userSongs > config.maxSongsPerUser()) {
                    EmbedUtils.errorMessage(channel, errorMsg);
                    return false;
                }
                if (queueLength > config.maxQueueLength()) {
                    EmbedUtils.errorMessage(channel, errorMsg);
                    return false;
                }
                return true;
            }

            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(requester.getAsTag());
                if (!check(1)) return;
                musicManager.scheduler.queue(track);
                channel.sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.GREEN)
                                .setAuthor(track.getUserData().toString(), null, channel.getGuild().getMemberByTag(track.getUserData().toString()).getUser().getAvatarUrl())
                                .setTitle("Song enqueued")
                                .addField("Title", track.getInfo().title, false)
                                .addField("Duration", TimeUtils.formatTime(track.getDuration()), false)
                                .build()
                ).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (trackUrl.contains("ytsearch:")) {
                    if (!check(1)) return;
                    AudioTrack track = playlist.getTracks().get(0);
                    track.setUserData(requester.getAsTag());
                    musicManager.scheduler.queue(track);
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.GREEN)
                                    .setTitle("Song enqueued")
                                    .setAuthor(track.getUserData().toString(), null, channel.getGuild().getMemberByTag(track.getUserData().toString()).getUser().getAvatarUrl())
                                    .addField("Title", track.getInfo().title, false)
                                    .addField("Duration", TimeUtils.formatTime(track.getDuration()), false)
                                    .build()
                    ).queue();
                    return;
                }
                final List<AudioTrack> tracks = playlist.getTracks();
                if (!check(tracks.size())) return;
                for (final AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                    track.setUserData(requester.getAsTag());
                }
                channel.sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.GREEN)
                                .setTitle("Playlist enqueued")
                                .setAuthor(playlist.getTracks().get(0).getUserData().toString(), null, channel.getGuild().getMemberByTag(playlist.getTracks().get(0).getUserData().toString()).getUser().getAvatarUrl())
                                .addField("Title", playlist.getName(), false)
                                .addField("Duration", TimeUtils.formatTime(playlist.getTracks().stream().map(AudioTrack::getDuration).reduce(
                                        (long) 0, Long::sum
                                )), false)
                                .build()
                ).queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription("❌ No results found")
                                .build()
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Commons.sendOwner(exception.getMessage(), exception.getStackTrace().toString(), channel.getJDA());
                channel.sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription("❌ Failed when loading song! Please try again")
                                .build()
                ).queue();
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

}

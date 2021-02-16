package me.JustAPie.CakeDJ.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
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
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(requester.getAsTag());
                long userSongs = musicManager.scheduler.queue.stream().filter((song) -> song.getUserData().equals(requester.getAsTag())).count();
                if (userSongs > DatabaseUtils.getGuildSetting(channel.getGuild()).maxSongsPerUser) {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setDescription("❌ Maximum songs per user exceeded")
                                    .build()
                    ).queue();
                    return;
                }
                long queueLength = musicManager.scheduler.queue.size();
                if (queueLength > DatabaseUtils.getGuildSetting(channel.getGuild()).maxQueueLength) {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setDescription("❌ Maximum songs in queue exceeded")
                                    .build()
                    ).queue();
                    return;
                }
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
                    AudioTrack track = playlist.getTracks().get(0);
                    track.setUserData(requester.getAsTag());
                    long userSongs = musicManager.scheduler.queue.stream().filter((song) -> song.getUserData().equals(requester.getAsTag())).count() + 1;
                    if (userSongs > DatabaseUtils.getGuildSetting(channel.getGuild()).maxSongsPerUser) {
                        channel.sendMessage(
                                new EmbedBuilder()
                                        .setColor(Color.RED)
                                        .setDescription("❌ Maximum songs per user exceeded")
                                        .build()
                        ).queue();
                        return;
                    }
                    long queueLength = musicManager.scheduler.queue.size() + 1;
                    if (queueLength > DatabaseUtils.getGuildSetting(channel.getGuild()).maxQueueLength) {
                        channel.sendMessage(
                                new EmbedBuilder()
                                        .setColor(Color.RED)
                                        .setDescription("❌ Maximum songs in queue exceeded")
                                        .build()
                        ).queue();
                        return;
                    }
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
                long userSongs = musicManager.scheduler.queue.stream().filter((song) -> song.getUserData().equals(requester.getAsTag())).count() + tracks.size();
                if (userSongs > DatabaseUtils.getGuildSetting(channel.getGuild()).maxSongsPerUser) {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setDescription("❌ Maximum songs per user exceeded")
                                    .build()
                    ).queue();
                    return;
                }
                long queueLength = musicManager.scheduler.queue.size() + tracks.size();
                if (queueLength > DatabaseUtils.getGuildSetting(channel.getGuild()).maxQueueLength) {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setDescription("❌ Maximum songs in queue exceeded")
                                    .build()
                    ).queue();
                    return;
                }
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

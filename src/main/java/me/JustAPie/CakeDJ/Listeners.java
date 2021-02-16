package me.JustAPie.CakeDJ;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Models.GuildConfig;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import me.JustAPie.CakeDJ.Utils.TaskUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class Listeners extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Listeners.class);
    public static final CommandManager manager =  new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        log.info(String.format("Logged in as %s", event.getJDA().getSelfUser().getAsTag()));
        event.getJDA().getPresence().setPresence(Activity.playing(
                "Handling " + manager.commands.size() + " commands"
        ), true);
        TaskUtils.setInterval(new TimerTask() {
            @Override
            public void run() {
                updateStats(event.getGuildTotalCount());
            }
        }, (long) 1e4);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        if (event.getMessage().isWebhookMessage()) return;
        if (event.getMessage().getAuthor().isBot()) return;
        GuildConfig guildConfig = DatabaseUtils.getGuildSetting(event.getGuild());
        if (event.getMessage().getContentRaw().startsWith(guildConfig.prefix)) {
            if (guildConfig.channelRestrict) {
                if (!guildConfig.djOnlyChannels.contains(event.getChannel().getId())) return;
            }
            manager.execute(event, guildConfig.prefix);
        } else {
            if (
                    event.getMessage().getContentRaw().contains("<@" + Commons.getConfig("clientid") + ">")
            ) {
                EmbedUtils.successMessage(event.getChannel(), "My prefix here is `" + guildConfig.prefix + "`");
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        if (event.getMember().equals(event.getGuild().getSelfMember())) {
            DatabaseUtils.updateGuildSetting(event.getGuild(), "is247", false);
            PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.destroy();
            PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
            PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.previousTrack.clear();
            event.getGuild().getAudioManager().closeAudioConnection();
            return;
        }
        if (event.getMember().getUser().isBot()) return;
        if (DatabaseUtils.getGuildSetting(event.getGuild()).is247) return;
        VoiceChannel botVC = event.getGuild().getAudioManager().getConnectedChannel();
        if (botVC == null) return;
        VoiceChannel leftVC = event.getGuild().getVoiceChannelById(event.getChannelLeft().getId());
        if (leftVC == null) return;
        if (botVC.getId().equalsIgnoreCase(leftVC.getId())) {
            TaskUtils.setTimeout(() -> {
                long count = event.getChannelLeft().getMembers().stream().filter(
                        member -> !member.getUser().isBot()
                ).count();
                if (count == 0) {
                    PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.destroy();
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }, 30000);
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);
        DatabaseUtils.createGuild(event.getGuild());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        super.onGuildLeave(event);
        DatabaseUtils.deleteGuild(event.getGuild());
    }

    @Override
    public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event) {
        super.onGuildUpdateName(event);
        DatabaseUtils.updateGuildSetting(event.getGuild(), "guildName", event.getGuild().getName());
    }

    private void updateStats(int serverCount) {
        DiscordBotListAPI api;
        if (!Commons.getConfig("dbltoken").isEmpty()) {
            api = new DiscordBotListAPI.Builder()
                    .token(Commons.getConfig("dbltoken"))
                    .botId(Commons.getConfig("clientid"))
                    .build();
            api.setStats(serverCount);
        }
    }
}

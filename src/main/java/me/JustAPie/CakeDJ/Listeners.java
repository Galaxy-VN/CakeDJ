package me.JustAPie.CakeDJ;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.TaskUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
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
        if (event.getMessage().getContentRaw().startsWith(DatabaseUtils.getGuildSetting(event.getGuild()).prefix)) {
            if (DatabaseUtils.getGuildSetting(event.getGuild()).channelRestrict) {
                if (!DatabaseUtils.getGuildSetting(event.getGuild()).djOnlyChannels.contains(event.getChannel().getId())) return;
            }
            manager.execute(event, DatabaseUtils.getGuildSetting(event.getGuild()).prefix);
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        VoiceChannel botVC = event.getGuild().getAudioManager().getConnectedChannel();
        if (botVC == null) return;
        AudioPlayer audio = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        if (audio == null) return;
        if (event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            audio.destroy();
            return;
        }
        if (event.getChannelLeft().equals(botVC)) {
            long members = botVC.getMembers().stream().filter((user) -> !user.getUser().isBot()).count();
            if (members == 0) {
                audio.destroy();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
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

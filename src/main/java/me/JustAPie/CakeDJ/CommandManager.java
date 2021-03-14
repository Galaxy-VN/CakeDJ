package me.JustAPie.CakeDJ;

import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.Commands.Config.*;
import me.JustAPie.CakeDJ.Commands.Info.HelpCommand;
import me.JustAPie.CakeDJ.Commands.Info.InviteCommand;
import me.JustAPie.CakeDJ.Commands.Info.PingCommand;
import me.JustAPie.CakeDJ.Commands.Info.SupportCommand;
import me.JustAPie.CakeDJ.Commands.Music.*;
import me.JustAPie.CakeDJ.Models.GuildConfig;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CommandManager {
    public final List<ICommand> commands = new ArrayList<>();
    public CommandManager() {
        addCommand(new ChannelManageCommand());
        addCommand(new ChannelRestrictCommand());
        addCommand(new DefaultVolumeCommand());
        addCommand(new LeaveTimeoutCommand());
        addCommand(new MaxQueueCommand());
        addCommand(new MaxSongPerUserCommand());
        addCommand(new PrefixCommand());
        addCommand(new SettingCommand());
        addCommand(new TFSCommand());
        addCommand(new HelpCommand(this));
        addCommand(new InviteCommand());
        addCommand(new PingCommand());
        addCommand(new SupportCommand());
        addCommand(new ClearCommand());
        addCommand(new EarrapeCommand());
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new MoveCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PauseCommand());
        addCommand(new PlayCommand());
        addCommand(new PreviousCommand());
        addCommand(new QueueCommand());
        addCommand(new RemoveCommand());
        addCommand(new RepeatCommand());
        addCommand(new RestartCommand());
        addCommand(new ResumeCommand());
        addCommand(new SeekCommand());
        addCommand(new ShuffleCommand());
        addCommand(new SkipCommand());
        addCommand(new SkipToCommand());
        addCommand(new VolumeCommand());
    }

    private void addCommand(ICommand cmd) {
        commands.add(cmd);
    }

    @Nullable
    public ICommand getCommand(String command) {
        for (ICommand cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(command) || cmd.getAliases().contains(command)) {
                return cmd;
            }
        }
        return null;
    }

    public void execute(GuildMessageReceivedEvent event, String prefix) {
        String raw = event.getMessage().getContentRaw();
        if (prefix.matches(Commons.mentionRegex)) {
            if (raw.length() == prefix.length()) {
                GuildConfig guildConfig = DatabaseUtils.getGuildSetting(event.getGuild());
                EmbedUtils.successMessage(event.getChannel(), "My prefix here is `" + guildConfig.prefix() + "`");
                return;
            }
            char[] ch = raw.toCharArray();
            while (ch[prefix.length()] == ' ') {
                raw = raw.replaceFirst("(?i)" + Pattern.quote(" "), "");
                ch = raw.toCharArray();
            }
        }
        String[] split = raw.replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);
        List<String> args = Arrays.asList(split).subList(1, split.length);
        boolean chk = check(event, args, cmd);
        if (!chk) return;
        CommandContext ctx = new CommandContext(event, args);
        try {
            cmd.exec(ctx);
        } catch (Exception e) {
            Commons.sendOwner(e.getMessage(), Arrays.toString(e.getStackTrace()), event.getJDA());
        }
    }

    private boolean check(GuildMessageReceivedEvent event, List<String> args, ICommand command) {
        if (command == null) return false;
        if (command.argsLength() != 0) {
            if (args.size() < command.argsLength()) {
                EmbedUtils.errorMessage(event.getChannel(), "Proper usage: `" + command.getUsage() + "`");
                return false;
            }
        }

        if (command.isOwnerCommand()) {
            if (!event.getAuthor().getId().equalsIgnoreCase(Commons.getConfig("owner"))) {
                EmbedUtils.errorMessage(event.getChannel(), "You must be the owner of the bot to use this command");
                return false;
            }
        }

        if (command.getBotPermission().size() != 0) {
            for (Permission perms : command.getBotPermission()) {
                if (!event.getGuild().getSelfMember().hasPermission(perms)) {
                    EmbedUtils.errorMessage(event.getChannel(), "Missing permission(s): `" + perms.getName() + "`");
                    return false;
                }
            }
        }

        if (command.getUserPermissions().size() != 0) {
            for (Permission perms : command.getUserPermissions()) {
                if (!Objects.requireNonNull(event.getMember()).hasPermission(perms)) {
                    EmbedUtils.errorMessage(event.getChannel(), "Missing permission(s): `" + perms.getName() + "`");
                    return false;
                }
            }
        }
        if (command.needConnectedVoice()) {
            if (!event.getMember().getVoiceState().inVoiceChannel()) {
                EmbedUtils.errorMessage(event.getChannel(), "You need to connect to a voice channel in order to use this command");
                return false;
            }
        }

        if (command.sameVoice()) {
            if (
                    event.getGuild().getAudioManager().getConnectedChannel() == null
                    || !event.getMember().getVoiceState().inVoiceChannel()
                    || !event.getMember().getVoiceState().getChannel().equals(
                            event.getGuild().getAudioManager().getConnectedChannel()
                    )
            ) {
                EmbedUtils.errorMessage(event.getChannel(), "You need to be in the same voice channel with the bot");
                return false;
            }
        }

        GuildMusicManager gm = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (command.activePlayer()) {
            if (gm.audioPlayer.getPlayingTrack() == null) {
                EmbedUtils.errorMessage(event.getChannel(), "There aren't any songs playing");
                return false;
            }
        }

        if (command.activeQueue()) {
            if (gm.scheduler.queue.size() == 0) {
                if (gm.audioPlayer.getPlayingTrack() == null) {
                    EmbedUtils.errorMessage(event.getChannel(), "There aren't any tracks going on");
                    return false;
                } return true;
            }
        }
        return true;
    }
}

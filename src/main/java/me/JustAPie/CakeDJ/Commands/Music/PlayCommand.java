package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;

public class PlayCommand implements ICommand {
    private static final List<String> formats = List.of("mp3", "ogg", "wav", "flac", "webm", "mp4", "m4a");
    @Override
    public void exec(CommandContext ctx) {
        VoiceChannel memberVoice = ctx.getMember().getVoiceState().getChannel();
        VoiceChannel botVoice = ctx.getGuild().getAudioManager().getConnectedChannel();
        if (botVoice == null) {
            ctx.getGuild().getAudioManager().openAudioConnection(memberVoice);
            ctx.getGuild().getAudioManager().setSelfDeafened(true);
        } else if (memberVoice != botVoice) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Please connect to the same voice channel");
            return;
        }
        String toPlay;
        if (ctx.getArgs().isEmpty()) {
            List<Message.Attachment> attachments = ctx.getMessage().getAttachments();
            if (attachments.isEmpty()) {
                EmbedUtils.errorMessage(ctx.getChannel(), "Please enter song's keyword or url or a file to play");
                return;
            }
            if (!formats.contains(attachments.get(0).getFileExtension())) {
                EmbedUtils.errorMessage(ctx.getChannel(), "Sorry, we currently not support this file extension");
                return;
            }
            toPlay = attachments.get(0).getUrl();
        } else {
            toPlay = String.join(" ", ctx.getArgs());
            if (!Commons.isUrl(toPlay)) toPlay = "ytsearch:" + toPlay;
        }
        PlayerManager.getInstance().loadAndPlay(ctx.getChannel(), toPlay, ctx.getMember().getUser());
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.setVolume(
                DatabaseUtils.getGuildSetting(ctx.getGuild()).defaultVolume
        );
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Play music";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "play (link/keyword)";
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.VOICE_CONNECT);
    }

    @Override
    public List<Permission> getBotPermission() {
        return List.of(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }

    @Override
    public boolean needConnectedVoice() {
        return true;
    }
}

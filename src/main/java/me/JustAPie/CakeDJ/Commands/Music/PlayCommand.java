package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.DatabaseUtils;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        VoiceChannel memberVoice = ctx.getMember().getVoiceState().getChannel();
        VoiceChannel botVoice = ctx.getGuild().getAudioManager().getConnectedChannel();
        if (botVoice == null) {
            ctx.getGuild().getAudioManager().openAudioConnection(memberVoice);
        } else if (memberVoice != botVoice) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Please connect to the same voice channel");
            return;
        }
        if (ctx.getArgs().isEmpty()) {
            List<Message.Attachment> attachments = ctx.getMessage().getAttachments();
            if (
                    attachments.isEmpty()
                    || !attachments.get(0).getFileExtension().equals("mp3")
            ) {
            }
        } else {
            String toPlay = String.join(" ", ctx.getArgs());
            PlayerManager.getInstance().loadAndPlay(ctx.getChannel(), toPlay, ctx.getMember().getUser());
            PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.setVolume(
                    DatabaseUtils.getGuildSetting(ctx.getGuild()).defaultVolume
            );
        }
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
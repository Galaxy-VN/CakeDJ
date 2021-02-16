package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        VoiceChannel memberVoice = ctx.getMember().getVoiceState().getChannel();
        VoiceChannel botVoice = ctx.getGuild().getAudioManager().getConnectedChannel();
        if (botVoice == null) {
            ctx.getGuild().getAudioManager().openAudioConnection(memberVoice);
            ctx.getGuild().getAudioManager().setSelfDeafened(true);
        } else if (memberVoice != botVoice) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Please connect to the same voice channel");
        }
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Join a voice channel";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "join";
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
        return List.of("summon");
    }

    @Override
    public boolean needConnectedVoice() {
        return true;
    }
}

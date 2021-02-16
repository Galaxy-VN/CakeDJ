package me.JustAPie.CakeDJ.Commands.Music;

import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer.destroy();
        String voice = ctx.getGuild().getAudioManager().getConnectedChannel().getName();
        ctx.getGuild().getAudioManager().closeAudioConnection();
        EmbedUtils.successMessage(ctx.getChannel(), "Disconnected from `" + voice + "`");
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Leave the voice channel";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "leave";
    }

    @Override
    public List<String> getAliases() {
        return List.of("disconnect");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }
}

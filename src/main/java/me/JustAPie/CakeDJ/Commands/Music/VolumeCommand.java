package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toSetStr = ctx.getArgs().get(0);
        if (Commons.isNaN(toSetStr)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid volume");
            return;
        }
        int toSet = Integer.parseInt(toSetStr);
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).audioPlayer;
        player.setVolume(toSet);
        EmbedUtils.successMessage(ctx.getChannel(), "Successfully set the volume to `" + toSetStr + "%`");
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getHelp() {
        return "Change the volume of the playing song";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "volume (volume)";
    }

    @Override
    public int argsLength() {
        return 1;
    }

    @Override
    public List<String> getAliases() {
        return List.of("vol");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }
}

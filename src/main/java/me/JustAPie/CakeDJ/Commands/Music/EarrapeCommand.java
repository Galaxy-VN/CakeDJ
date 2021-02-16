package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

public class EarrapeCommand implements ICommand {
    private static final float[] bassboost = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f };
    @Override
    public void exec(CommandContext ctx) {
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        boolean status = !manager.scheduler.earrape;
        AudioPlayer player = manager.audioPlayer;
        if (status) {
            EqualizerFactory eq = new EqualizerFactory();
            for (int i = 0; i < bassboost.length; i++) {
                eq.setGain(i, bassboost[i] + 5);
            }
            player.setFilterFactory(eq);
        } else player.setFilterFactory(null);
        EmbedUtils.successMessage(ctx.getChannel(), "Turned " + status + " earrape");
    }

    @Override
    public String getName() {
        return "earrape";
    }

    @Override
    public String getHelp() {
        return "Rape your ears";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "earrape";
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }
}

package me.JustAPie.CakeDJ.Commands.Music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.JustAPie.CakeDJ.Audio.GuildMusicManager;
import me.JustAPie.CakeDJ.Audio.PlayerManager;
import me.JustAPie.CakeDJ.CommandContext;
import me.JustAPie.CakeDJ.ICommand;
import me.JustAPie.CakeDJ.Utils.Commons;
import me.JustAPie.CakeDJ.Utils.EmbedUtils;

import java.util.List;

public class MoveCommand implements ICommand {
    @Override
    public void exec(CommandContext ctx) {
        String toMove = ctx.getArgs().get(0), dest = ctx.getArgs().get(1);
        if (Commons.isNaN(toMove) || Commons.isNaN(dest)) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid position");
            return;
        }
        GuildMusicManager gm = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        int from = Integer.parseInt(toMove), to = Integer.parseInt(dest);
        if (
                to > gm.scheduler.queue.size()
                || from < 0
        ) {
            EmbedUtils.errorMessage(ctx.getChannel(), "Invalid position");
            return;
        }
        AudioTrack a = gm.scheduler.queue.get(from), b = gm.scheduler.queue.get(to);
        gm.scheduler.swap(from, to);
        EmbedUtils.successMessage(ctx.getChannel(), "Swapped `" + a.getInfo().title  + "` with `" + b.getInfo().title + "`");
    }

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public String getHelp() {
        return "Move a song to a specified position";
    }

    @Override
    public String getCategory() {
        return "Music";
    }

    @Override
    public String getUsage() {
        return "move (song index) (position)";
    }

    @Override
    public int argsLength() {
        return 2;
    }

    @Override
    public List<String> getAliases() {
        return List.of("mv");
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

package me.JustAPie.CakeDJ.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {
    public final AudioPlayer player;
    public LinkedList<AudioTrack> queue;
    public final LinkedList<AudioTrack> previousTrack;

    public boolean queueLoop = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
        this.previousTrack = new LinkedList<>();
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void nextTrack() {
        AudioTrack clone = this.player.getPlayingTrack().makeClone();
        if (queueLoop) queue.offerLast(clone);
        else previousTrack.offerLast(clone);
        this.player.startTrack(this.queue.poll(), false);
    }

    public void shuffle() {
        Collections.shuffle(this.queue);
    }

    public void swap(int index, int pos) {
        Collections.swap(queue, index, pos);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
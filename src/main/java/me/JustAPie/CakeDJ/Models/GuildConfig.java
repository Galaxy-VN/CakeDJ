package me.JustAPie.CakeDJ.Models;

import java.util.List;

public interface GuildConfig {
    String guildName();
    String guildID();
    String prefix();
    boolean is247();
    boolean channelRestrict();
    long maxQueueLength();
    long maxSongsPerUser();
    int defaultVolume();
    List<String> djOnlyChannels();
}

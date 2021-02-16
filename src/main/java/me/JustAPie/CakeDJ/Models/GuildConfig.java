package me.JustAPie.CakeDJ.Models;

import java.util.List;

public class GuildConfig {
    public String guildName;
    public String guildID;
    public String prefix;
    public boolean is247;
    public boolean channelRestrict;
    public long maxQueueLength;
    public long maxSongsPerUser;
    public int defaultVolume;
    public List<String> djOnlyChannels;

    public GuildConfig setGuildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public GuildConfig setGuildID(String guildID) {
        this.guildID = guildID;
        return this;
    }

    public GuildConfig setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public GuildConfig setIs247(boolean is247) {
        this.is247 = is247;
        return this;
    }

    public GuildConfig setMaxQueueLength(long maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
        return this;
    }

    public GuildConfig setMaxSongsPerUser(long maxSongsPerUser) {
        this.maxSongsPerUser = maxSongsPerUser;
        return this;
    }

    public GuildConfig setChannelRestrict(boolean channelRestrict) {
        this.channelRestrict = channelRestrict;
        return this;
    }

    public GuildConfig setDjOnlyChannels(List<String> djOnlyChannels) {
        this.djOnlyChannels = djOnlyChannels;
        return this;
    }

    public GuildConfig setDefaultVolume(int defaultVolume) {
        this.defaultVolume = defaultVolume;
        return this;
    }
}

package me.JustAPie.CakeDJ.Utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.JustAPie.CakeDJ.Models.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.List;

public class DatabaseUtils {
    private static final ConnectionString connString = new ConnectionString(Commons.getConfig("database"));
    private static final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();
    private static final MongoClient client = MongoClients.create(settings);
    private static final MongoDatabase database = client.getDatabase("CakeDJ");
    private static final MongoCollection<Document> serverCollection = database.getCollection("servers");

    private static boolean isCreated(Guild guild) {
        return serverCollection.find(Filters.eq("guildID", guild.getId())).first() != null;
    }

    public static void createGuild(Guild guild) {
        if (isCreated(guild)) return;
        Document document = new Document()
                .append("guildName", guild.getName())
                .append("guildID", guild.getId())
                .append("prefix", Commons.getConfig("prefix"))
                .append("is247", false)
                .append("channelRestrict", false)
                .append("maxQueueLength", 1000)
                .append("maxSongsPerUser", 1000)
                .append("defaultVolume", 100)
                .append("djOnlyChannels", List.of())
                .append("leaveTimeout", 30000L);
        serverCollection.insertOne(document);
    }

    public static void bulkUpdate(String key, Object value) {
        serverCollection.find().forEach((doc) -> {
            String guildID = doc.getString("guildID");
            serverCollection.findOneAndUpdate(Filters.eq(new Document(guildID, guildID)), new Document("$set", new Document(key, value)));
        });
    }

    @Nullable
    public static GuildConfig getGuildSetting(Guild guild) {
        if (!isCreated(guild)) return null;
        Document document = serverCollection.find(Filters.eq("guildID", guild.getId())).first();
        return new GuildConfig() {
            @Override
            public String guildName() {
                return document.getString("guildName");
            }

            @Override
            public String guildID() {
                return document.getString("guildID");
            }

            @Override
            public String prefix() {
                return document.getString("prefix");
            }

            @Override
            public boolean is247() {
                return document.getBoolean("is247");
            }

            @Override
            public boolean channelRestrict() {
                return document.getBoolean("channelRestrict");
            }

            @Override
            public int maxQueueLength() {
                return document.getInteger("maxQueueLength");
            }

            @Override
            public int maxSongsPerUser() {
                return document.getInteger("maxSongsPerUser");
            }

            @Override
            public int defaultVolume() {
                return document.getInteger("defaultVolume");
            }

            @Override
            public long leaveTimeout() {
                return document.getLong("leaveTimeout");
            }

            @Override
            public List<String> djOnlyChannels() {
                return document.getList("djOnlyChannels", String.class);
            }
        };
    }

    public static void updateGuildSetting(Guild guild, String key, Object value) {
        if (!isCreated(guild)) return;
        Document document = new Document("$set", new Document(key, value));
        serverCollection.findOneAndUpdate(Filters.eq("guildID", guild.getId()), document);
    }

    public static void deleteGuild(Guild guild) {
        if (!isCreated(guild)) return;
        serverCollection.findOneAndDelete(Filters.eq("guildID", guild.getId()));
    }
}

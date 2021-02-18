package me.JustAPie.CakeDJ.Utils;

import org.discordbots.api.client.DiscordBotListAPI;

public class DSLUtils {
    private static final DiscordBotListAPI api = new DiscordBotListAPI.Builder()
            .token(Commons.getConfig("dbltoken"))
            .botId(Commons.getConfig("clientid"))
            .build();

    public static void updateServerCount(int serverCount) {
        api.setStats(serverCount);
    }
}

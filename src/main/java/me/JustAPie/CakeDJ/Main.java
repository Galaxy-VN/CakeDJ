package me.JustAPie.CakeDJ;

import me.JustAPie.CakeDJ.Utils.Commons;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        DefaultShardManagerBuilder
                .create(
                        Commons.getConfig("token"),
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES
                )
                .addEventListeners(new Listeners())
                .build();
    }
}

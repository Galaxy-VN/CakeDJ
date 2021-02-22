package me.JustAPie.CakeDJ;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
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
                .setAudioSendFactory(new NativeAudioSendFactory())
                .addEventListeners(new Listeners())
                .build();
    }
}

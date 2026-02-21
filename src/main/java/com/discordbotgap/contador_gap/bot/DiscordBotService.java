package com.discordbotgap.contador_gap.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class DiscordBotService {

    @Value("${discord.token}")
    private String token;

    private final GapListener gapListener;

    public DiscordBotService(GapListener gapListener) {
        this.gapListener = gapListener;
    }

    @PostConstruct
    public void startBot() {
        JDABuilder.createDefault(token)
            
            .enableIntents(GatewayIntent.MESSAGE_CONTENT) 
            .addEventListeners(gapListener)
            .build();
    }
}
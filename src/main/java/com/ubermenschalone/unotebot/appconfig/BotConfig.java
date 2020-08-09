package com.ubermenschalone.unotebot.appconfig;

import com.ubermenschalone.unotebot.MyUnotebot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String botUserName;
    private String botToken;
    private String webHookPath;

    @Bean
    public MyUnotebot myUnotebot(){
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        MyUnotebot myUnotebot = new MyUnotebot(options);
        myUnotebot.setBotUserName(botUserName);
        myUnotebot.setBotToken(botToken);
        myUnotebot.setWebHookPath(webHookPath);

        return myUnotebot;
    }
}
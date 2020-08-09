package com.ubermenschalone.unotebot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyUnotebot extends TelegramWebhookBot {

    private String botUserName;
    private String botToken;
    private String webHookPath;

    public MyUnotebot(DefaultBotOptions botOptions){super(botOptions);}


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if(update.getMessage() != null && update.getMessage().hasText()){
            long chat_id = update.getMessage().getChatId();

            try{
                execute(new SendMessage(chat_id, "Hello, " + update.getMessage().getText()));
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }
}
package com.ubermenschalone.unotebot.botapi;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SendCustomMessages extends TelegramWebhookBot {

    private String botUserName = "@Unotebot";
    private String botToken = "1389969502:AAHyLIBByS_5swN9-c2kA56j87iz1A5PBtw";
    private String webHookPath = "https://bb1971d16100.ngrok.io";

    public SendCustomMessages(){
        super(ApiContext.getInstance(DefaultBotOptions.class));
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        return null;
    }

    public void sendEmptyMessage(long chat_id, int message_id)
    {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chat_id);
        deleteMessage.setMessageId(message_id);
        try {
            execute(deleteMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void sendEvent(long chatId, String text, boolean markup){
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setParseMode(ParseMode.MARKDOWN);
        message.setChatId(chatId);
        if(markup){
            message.setReplyMarkup(getDeleteButtonsMarkup());
        }
        try {
           execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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



    private InlineKeyboardMarkup getDeleteButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonDelete = new InlineKeyboardButton().setText("\uD83D\uDDD1️   Удалить");

        buttonDelete.setCallbackData("buttonDelete");

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(buttonDelete);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

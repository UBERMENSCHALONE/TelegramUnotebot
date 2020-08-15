package com.ubermenschalone.unotebot.botapi.handlers.start;

import com.ubermenschalone.unotebot.botapi.BotState;
import com.ubermenschalone.unotebot.botapi.InputMessageHandler;
import com.ubermenschalone.unotebot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StartHandler implements InputMessageHandler {

    private ReplyMessagesService messagesService;

    public StartHandler(ReplyMessagesService messagesService){
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FIRST_START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.firstStart");
        replyToUser.setReplyMarkup(getInLineMessageButtons());

        return replyToUser;
    }

    private InlineKeyboardMarkup getInLineMessageButtons()  {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Продолжить");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("Нет, спасибо");

        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
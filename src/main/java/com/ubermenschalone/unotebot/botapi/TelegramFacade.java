package com.ubermenschalone.unotebot.botapi;

import com.ubermenschalone.unotebot.cache.UserDataCache;
import com.ubermenschalone.unotebot.service.MainMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService){
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }

    public BotApiMethod<?> handleUpdate(Update update){
        SendMessage replyMessage = null;


        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if(message != null && message.hasText()){
            log.info("New message from User:{}, chatId: {},  with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.FIRST_START;
                log.info("FIRST_START");
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                log.info("SHOW_HELP_MENU");
                break;
            case "\uD83D\uDDD2   Мои заметки":
                botState = BotState.ALL_NOTES;
                log.info("ALL_NOTES");
                break;
            case "✏️   Редактировать":
                botState = BotState.EDIT_NOTE;
                log.info("EDIT_NOTE");
                break;
            case "Нет, спасибо":
                botState = BotState.DONT_USE;
                log.info("DONT_USE");
                break;
            default:
                botState = BotState.ADD_NOTE;
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Чтобы эффективно пользоваться ботом, воспользуйтесь кнопками меню ↘️");

        if(buttonQuery.getData().equals("buttonYes")){
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_HELP_MENU);
        } else if(buttonQuery.getData().equals("buttonNo")){
            callBackAnswer = sendAnswerCallbackQuery("Возвращайтесь, когда будете готовы", false, buttonQuery);
            userDataCache.setUsersCurrentBotState(userId, BotState.DONT_USE);
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_HELP_MENU);
        }
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}

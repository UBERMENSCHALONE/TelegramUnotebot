package com.ubermenschalone.unotebot.botapi.handlers.data;

import com.ubermenschalone.unotebot.botapi.BotState;
import com.ubermenschalone.unotebot.botapi.InputMessageHandler;
import com.ubermenschalone.unotebot.cache.UserDataCache;
import com.ubermenschalone.unotebot.model.Note;
import com.ubermenschalone.unotebot.model.User;
import com.ubermenschalone.unotebot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Component
public class AddNoteHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public AddNoteHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ADD_NOTE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ADD_NOTE);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADD_NOTE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        User userData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        log.info("Время = " + inputMsg.getDate());

        if (botState.equals(BotState.ADD_NOTE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.noteAdded");
            userData.addNote(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ADD_NOTE);
        }

        if (botState.equals(BotState.ALL_NOTES)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ADD_NOTE);

            LinkedHashMap<String, Note> notes = userData.getNotes();

            String text = "";
            int index = 0;
            for(Map.Entry<String, Note> pair : notes.entrySet()){
                Note value = pair.getValue();
                text = text + (++index + ". " + value.getmNote() + "\nДата создания: " + value.getmDate()+ "\n\n");
            }

            replyToUser = new SendMessage(chatId, "\uD83D\uDDD2   ВСЕ ЗАМЕТКИ\n\n" + text);
        }

        if (botState.equals(BotState.EDIT_NOTE)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ADD_NOTE);
            replyToUser = new SendMessage(chatId, "✏️   РЕДАКТИРОВАНИЕ ЗАМЕТОК\n\n");
        }

        if (botState.equals(BotState.SHOW_HELP_MENU)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_HELP_MENU);
            replyToUser = new SendMessage(chatId, "Помощь в разработке");
        }

        userDataCache.saveUserProfileData(userId, userData);

        return replyToUser;
    }


}
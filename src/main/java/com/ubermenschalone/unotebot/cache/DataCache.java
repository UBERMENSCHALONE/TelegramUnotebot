package com.ubermenschalone.unotebot.cache;

import com.ubermenschalone.unotebot.botapi.BotState;
import com.ubermenschalone.unotebot.model.User;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    User getUserProfileData(int userId);

    void saveUserProfileData(int userId, User user);
}

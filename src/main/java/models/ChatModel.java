package models;

import java.util.List;
import java.util.UUID;

public interface ChatModel {
    void addChatMessage(String user_id, String time_created, String date_created, String content);
    List<Chat> getAllChatMessages();
}


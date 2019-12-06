package models;


import lombok.Data;

@Data
public class ChatUserMap {
    private Chat chat;
    private User user;

    public ChatUserMap(Chat chat, User user){
        this.chat = chat;
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public User getUser() {
        return user;
    }
}

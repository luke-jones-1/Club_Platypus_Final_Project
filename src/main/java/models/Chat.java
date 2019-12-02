package models;

import java.util.UUID;
import lombok.Data;

@Data
public class Chat {
    private UUID chat_id;
    private UUID user_id;
    private String time_created;
    private String date_created;
    private String content;

    public Chat(UUID chat_id, UUID user_id, String time_created, String date_created, String content){
        super();
        this.chat_id = chat_id;
        this.user_id = user_id;
        this.time_created = time_created;
        this.date_created = date_created;
        this.content = content;
    }
}

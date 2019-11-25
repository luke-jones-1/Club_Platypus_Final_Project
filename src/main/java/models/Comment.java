package models;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import lombok.Data;
@Data

public class Comment {
    UUID post_id;
    UUID comment_id;
    String comment;

    public Comment (UUID post_id, UUID comment_id, String comment) {
        this.post_id = post_id;
        this.comment_id = comment_id;
        this.comment = comment;
    }
}

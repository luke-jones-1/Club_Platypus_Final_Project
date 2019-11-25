package models;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
    UUID comment_id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cc");

    @org.junit.jupiter.api.Test
    void constructor() {
        Comment comment = new Comment(id, comment_id, "This is a comment");
        assertEquals(comment.post_id, id);
        assertEquals(comment.comment_id, comment_id);
        assertEquals(comment.comment, "This is a comment");
    }

}
package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content);
    List getAllPosts();
    void addLike(String id);
    String gettingPost(String title);
    void deletePost(String post_id);
    boolean doesEmailExist(String email);
}



package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.lang.*;

public class Sql2oModel implements Model, UserModel, ChatModel {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public UUID createUser(String first_name, String last_name, String password, String email, String platypus_colour) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = UUID.randomUUID();
            conn.createQuery("insert into users(id, first_name, last_name, email, password, platypus_colour) VALUES (:id, :first_name, :last_name, :email, :password, :platypus_colour)")
                    .addParameter("id", userUuid)
                    .addParameter("first_name", first_name) .addParameter("last_name", last_name)
                    .addParameter("email", email).addParameter("password", password)
                    .addParameter("platypus_colour", platypus_colour)
                    .executeUpdate();
            conn.commit();
            return userUuid;
        }
    }

    public Boolean verifyUser(String email, String password) {
        boolean correct_password = false;

        try (Connection conn = sql2o.open()) {
            List<User> user = conn.createQuery("select password from users where email = :email")
                    .addParameter("email", email)
                    .executeAndFetch(User.class);
            password = "[User(id=null, first_name=null, last_name=null, email=null, password=" + password + ", platypus_colour=null)]";
            if(user.toString().equals(password)){
                correct_password = true;
            };
        }

        return correct_password;
    }

    public boolean doesEmailExist(String email) {
        try (Connection conn = sql2o.open()) {
            List<String> emails = conn.createQuery("select email from users")
                    .executeAndFetch(String.class);
            for (String s : emails)
                if (email.equals(s)) {
                    return true;
                }
            return false;
        }
    }
  
    public String getUserID(String email){
        try (Connection conn = sql2o.open()) {
            List<String> id = conn.createQuery("select id from users where email = :email")
                    .addParameter("email", email)
                    .executeAndFetch(String.class);
            return id.toString().replaceAll("[\\[\\]]","");
        }
    }

//    public String getUsername(String userID){
//        StringBuilder username = new StringBuilder("");
//        try (Connection conn = sql2o.open()) {
//            List<String> first_name = conn.createQuery("select first_name from users where id = :userID") //gets ID from users table, using ID stored in SessionID
//                    .addParameter("userID", userID)
//                    .executeAndFetch(String.class);
//            username.append(first_name.toString().replaceAll("[\\[\\]]",""));
//        }
//        try (Connection conn = sql2o.open()) {
//            List<String> last_name = conn.createQuery("select last_name from users where id = :userID") //gets ID from users table, using ID stored in SessionID
//                    .addParameter("userID", userID)
//                    .executeAndFetch(String.class);
//            username.append(" " + last_name.toString().replaceAll("[\\[\\]]",""));
//        }
//
//        return username.toString();
//    }
//    public String getPlatypusColour(String userID){
//        try (Connection conn = sql2o.open()) {
//            List<String> colour = conn.createQuery("select platypus_colour from users where id = :userID") //gets ID from users table, using ID stored in SessionID
//                    .addParameter("userID", userID)
//                    .executeAndFetch(String.class);
//            return colour.toString().replaceAll("[\\[\\]]","");
//        }
//    }

    public User fetchUserById(String userID) {
        try (Connection conn = sql2o.open()) {
            List<User> userList = conn.createQuery("select * from users where id = :userID") //gets ID from users table, using ID stored in SessionID
                    .addParameter("userID", userID)
                    .executeAndFetch(User.class);
            User user = userList.get(0);
            return user;
        }
    }

//    Methods used for chatlog table

    public void addChatMessage(String user_id, String time_created, String date_created, String content){
        try (Connection conn = sql2o.beginTransaction()) {
            UUID chat_id = UUID.randomUUID();
            conn.createQuery("insert into chatlog(chat_id, user_id, time_created, date_created, content) VALUES (:chat_id, :user_id, :time_created, :date_created, :content)")
                    .addParameter("chat_id", chat_id)
                    .addParameter("user_id", user_id)
                    .addParameter("time_created", time_created)
                    .addParameter("date_created", date_created)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
        }
    }

    public List<Chat> getAllChatMessages() {
        try (Connection conn = sql2o.open()) {
            List<Chat> chats = conn.createQuery("select * from chatlog")
                    .executeAndFetch(Chat.class);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(chats);
            return chats;
        }
    }

}


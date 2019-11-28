package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model, UserModel {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public UUID createUser(String first_name, String last_name, String password, String email) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = UUID.randomUUID();
            conn.createQuery("insert into users(id, first_name, last_name, email, password) VALUES (:id, :first_name, :last_name, :email, :password)")
                    .addParameter("id", userUuid)
                    .addParameter("first_name", first_name)
                    .addParameter("last_name", last_name)
                    .addParameter("email", email)
                    .addParameter("password", password)
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
            password = "[User(id=null, first_name=null, last_name=null, email=null, password="+password+")]";
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

//    public String getUsername(String email){
//        try (Connection conn = sql2o.open()) {
//            List<String> username = conn.createQuery("select first_name, last_name from users where email = :email")
//                    .addParameter("email", email)
//                    .executeAndFetch(String.class);
//            return username.toString();
//        }
//    }
}


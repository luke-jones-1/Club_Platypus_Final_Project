package models;

import java.util.UUID;

public interface UserModel {
    UUID createUser(String first_name, String last_name, String password, String email, String platypus_colour);
    Boolean verifyUser(String email, String password);
    User fetchUserById(String id);
    User fetchUser(String email);
}

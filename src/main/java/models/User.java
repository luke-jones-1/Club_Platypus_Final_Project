package models;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    public User(UUID id, String first_name, String last_name, String email, String password){
        super();
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

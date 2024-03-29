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
    private String platypus_colour;

    public User(UUID id, String first_name, String last_name, String email, String password, String platypus_colour){
        super();
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.platypus_colour = platypus_colour;
    }

    public UUID getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPlatypus_colour() {
        return platypus_colour;
    }

    public String getUsername() {
        return generateUsername(first_name, last_name);
    }

    private String generateUsername(String f_name, String l_name){
        String username = new String();
        username += String.format("%s %s", f_name, l_name);
        return username;
    }
}

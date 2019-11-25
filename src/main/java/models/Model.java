package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    boolean doesEmailExist(String email);
}



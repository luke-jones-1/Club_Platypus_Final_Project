package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "cgi_platypi_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    Connection conn = sql2o.open();
    Model model = new Sql2oModel(sql2o);
    UserModel userModel= new Sql2oModel(sql2o);

    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/cgi_platypi_test", null, null).load();
        flyway.migrate();

    }

//    @BeforeEach
//    void setUp() {
//        conn.createQuery("insert into posts(post_id, title, content, time, likes) VALUES (:post_id, :title, :content, :time, 0)")
//                .addParameter("post_id", id)
//                .addParameter("title", "example title")
//                .addParameter("content", "example content")
//                .addParameter("time", timestamp)
//                .executeUpdate();
//    }

    @AfterEach
    void tearDown() {
        conn.createQuery("TRUNCATE TABLE users")
        .executeUpdate();
    }

    @Test
    void verifyUser(){

        userModel.createUser("Example", "name","password","name@name.com");
        List<User> user = new ArrayList<>();
        user.add(new User( id , "Example", "name", "name@name.com", "password"));
        assertTrue(userModel.verifyUser("name@name.com", "password"));
    }

    @Test
    void createUser(){
        assertEquals(userModel.createUser("Example", "name","password","name@name.com").toString(), userModel.getUserID("name@name.com"));
    }

    @Test
    void doesUserAlreadyExistYes(){
        userModel.createUser("Example", "lastname", "password", "test@gmail.com");
        assertTrue(model.doesEmailExist("test@gmail.com"));
    }

    @Test
    void doesUserAlreadyExistNo(){
        assertFalse(model.doesEmailExist("test@gmail.com"));
    }
}
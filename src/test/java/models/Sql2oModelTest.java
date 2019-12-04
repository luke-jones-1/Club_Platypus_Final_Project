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
    UserModel userModel = new Sql2oModel(sql2o);
    ChatModel chatModel = new Sql2oModel(sql2o);

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
        conn.createQuery("TRUNCATE TABLE users CASCADE;")
        .executeUpdate();
    }

    @Test
    void verifyUser(){
        userModel.createUser("Example", "name","password","name@name.com", "blue");
        assertTrue(userModel.verifyUser("name@name.com", "password"));
    }

    @Test
    void createUser(){
        assertEquals(userModel.createUser("Example", "name","password","name@name.com", "blue"), userModel.fetchUser("name@name.com").getId());
    }

    @Test
    void doesUserAlreadyExistYes(){
        userModel.createUser("Example", "lastname", "password", "test@gmail.com", "blue");
        assertTrue(model.doesEmailExist("test@gmail.com"));
    }

    @Test
    void doesUserAlreadyExistNo(){
        assertFalse(model.doesEmailExist("test@gmail.com"));
    }

    @Test
    void canAddChatMessagesToTable(){
//        ARRANGE
        userModel.createUser("Example", "lastname", "password", "test@gmail.com", "blue");
        String userModelID = userModel.fetchUser("test@gmail.com").getId().toString();
//        ACT
        chatModel.addChatMessage(userModelID, "16:34", "29th Nov", "This is a test post");
        List<String> returnTimeSQL = conn.createQuery("select time_created from chatlog where user_id=:user_ID")
                .addParameter("user_ID", userModelID)
                .executeAndFetch(String.class);
        assertEquals("16:34", returnTimeSQL.toString().replaceAll("[\\[\\]]",""));
    }

    @Test
    void canGetAllMessagesFromTable(){ // Similar to verifyUser test
        // arrange
        //        Create user
        userModel.createUser("Sir","Bath", "password", "a@b.c", "real");
        String userId = userModel.fetchUser("a@b.c").getId().toString();
        //        SQL - Insert chat message ...
        UUID chatId = UUID.randomUUID();
        conn.createQuery("INSERT into chatlog (chat_id, user_id, time_created, date_created, content) Values (:chat_id, :user_id, :time_created, :date_created, :content);")
                .addParameter("chat_id", chatId)
                .addParameter("user_id", userId)
                .addParameter("time_created", "16:20")
                .addParameter("date_created", "1st")
                .addParameter("content", "this is content")
                .executeUpdate();

        // act
        //        Run method getAllMessages
        List<Chat> chat = chatModel.getAllChatMessages();
        // assert
        //        assertEquals()
        assertTrue(chat.toString().replaceAll("[\\[\\]]","").contains("time_created=16:20, date_created=1st, content=this is content"));
    }

    @Test
    void canfetchUser(){
        userModel.createUser("Sir","Bath", "password", "a@b.c", "real");
        User user = userModel.fetchUser("a@b.c");
        User expectedUser = new User(user.getId(), "Sir", "Bath", "a@b.c", "password", "real");
        assertEquals(expectedUser, user);
    }

    @Test
    void canCreateInstanceOfChat(){
        UUID userId = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        ChatModel model = new Sql2oModel(sql2o);
        List<Chat> chat = new ArrayList<Chat>();
        Chat chatinstance = new Chat(chatId, userId, "16:32", "1st", "this is a test");
        chat.add(chatinstance);
        assertEquals(chat.get(0), chatinstance);
    }

    @Test
    void canCreateInstanceOfUser(){
        UUID id = UUID.randomUUID();
        UserModel model = new Sql2oModel(sql2o);
        List<User> user = new ArrayList<User>();
        User userinstance = new User(id, "Will", "Bath", "a@b.c", "test", "real");
        user.add(userinstance);
        assertEquals(user.get(0), userinstance);
    }

    @Test
    void canGetUsername(){
        UUID id = UUID.randomUUID();
        User userinstance = new User(id, "Albion", "Bidder", "x@y.z", "WillIsTheBest", "green");

        assertEquals("Albion Bidder", userinstance.getUsername());
    }

    @Test
    void canFetchUserById(){
        userModel.createUser("Sir","Bath", "password", "a@b.c", "real");
        User expectedUser = userModel.fetchUser("a@b.c");
        String Id = expectedUser.getId().toString();
        User Actual = userModel.fetchUserById(Id);
        assertEquals(Actual, expectedUser);
    }

}
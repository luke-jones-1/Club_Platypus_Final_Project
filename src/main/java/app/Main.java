package app;

import models.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.velocity.app.Velocity;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;
import java.util.*;
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        BasicConfigurator.configure();

        staticFileLocation("/public");
        port(getHerokuAssignedPort());
        Velocity.init();

        String dbHost = "localhost";
        String dbName = "cgi_platypi";
        String dbUser = null;
        String dbPass = null;

        if ( args.length != 0 ){
            dbHost = args[0];
            dbName = args[1];
            dbUser = args[2];
            dbPass = args[3];
        }

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://"+dbHost+":5432/"+dbName, dbUser,
                dbPass).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + dbHost + ":5432/" + dbName , dbUser,
                dbPass, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);
        UserModel userModel = new Sql2oModel(sql2o);
        ChatModel chatModel = new Sql2oModel(sql2o); // creates instance of chatmodel
        PaddleChatWebSocketHandler PaddleChatWebSocket = new PaddleChatWebSocketHandler(chatModel);
        // passes chat instance into paddlechat socket handler so that it can be used inside the class
        webSocket("/chat", PaddleChatWebSocket);// loads the web socket with the instance of paddlechathandler


        //Frontpage method
        get("/", (req, res) -> {
            HashMap index = new HashMap();
            return new ModelAndView(index, "templates/index.vtl");
        }, new VelocityTemplateEngine());


        get("/room", (req, res) -> {
            Map<Chat, User> chatLog = new HashMap<>();
            if (PaddleChat.currentUserClass == null){
                res.redirect("/");
            }
            List<Chat> log = chatModel.getAllChatMessages();
//             for loop for entire of log
            for (Chat chatInstance : log) {
            // user class from user id
                User currentUser = userModel.fetchUserById(chatInstance.getUser_id().toString());
            // call broadcast message
                chatLog.put(chatInstance, currentUser);
            }
//            System.out.println(chats.keySet());
            return new ModelAndView(chatLog, "templates/room.vtl");
        }, new VelocityTemplateEngine());


        //Sign-in methods

        get("/sign-in", (req, res) -> {
            HashMap signIn = new HashMap();
            return new ModelAndView(signIn, "templates/sign_in.vtl");
        }, new VelocityTemplateEngine());

        post("/sign-in", (req,res) -> {

            String password = req.queryParams("password");
            String email = req.queryParams("email");

            if(userModel.verifyUser(email, password)) { //pulls user from database and compares if
                SetPaddle(userModel, email);
                res.redirect("/room");
            }
            else { res.redirect("/sign-in"); }
            return null;
        });


        //Sign up methods

        get("/sign-up", (req, res) -> {
            HashMap users = new HashMap();
            return new ModelAndView(users, "templates/sign_up.vtl");
        }, new VelocityTemplateEngine());

        post("/sign-up", (req,res) -> {
            String first_name = req.queryParams("first_name");
            String last_name = req.queryParams("last_name");
            String password = req.queryParams("password");
            String email = req.queryParams("email");
            String platypus_colour = req.queryParams("platypus_colour");

            if (model.doesEmailExist(email)) {
                res.redirect("/sign-up");
            } else {
                userModel.createUser(first_name, last_name, password, email, platypus_colour);
                SetPaddle(userModel, email);
                res.redirect("/room");
            }
            return null;
        });
    }


    //Utility functions
    static void SetPaddle(UserModel userModel, String email){
//        PaddleChat.currentSessionUser = userModel.getUserID(email);
        PaddleChat.currentUserClass = userModel.fetchUser(email);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}

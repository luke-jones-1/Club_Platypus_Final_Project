package app;

import models.User;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

public class PaddleChat {
    static Map<Session, User> userUsernameMap = new ConcurrentHashMap<>(); // hash of each session and the username as a string
//    static String currentSessionUser = null;
    public static User currentUserClass = null;
//    public static Chat

    public static void broadcastMessage(User sender, String message){
        // selects only open session (websockets that are active) then iterates through each
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            // takes a session makes a websocket request to send a string
            try {
                // creating a string in the format of a JSON Object (kind of like a hash that can be read by multiple languages)
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender.getUsername(), message, sender.getPlatypus_colour())) // sends data to construct messages
                        .put("userlist", generateUserList(userUsernameMap.values())) // sends data to make list of users
                ));
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public static void broadcastServerMessage(String message){
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            // takes a session makes a websocket request to send a string
            try {
                // creating a string in the format of a JSON Object (kind of like a hash that can be read by multiple languages)
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromServer(message)) // sends data to construct messages
                        .put("userlist", generateUserList(userUsernameMap.values())) // sends data to make list of users
                ));
            } catch (Exception e){
                e.printStackTrace(); //prints error log on error
            }
        });
    }



    // takes java variables as arguments and returns html code with the arguments values as text
    private static String createHtmlMessageFromSender(String sender, String message, String platypusColour) {
        return article().with(
                div(attrs(".media"), // <div class="media">
                        div(attrs(".media-body"), // <div class="media-body">
                                img(attrs(".align-self-start.mr-3")).withSrc("/images/" + platypusColour + "Platypus.png").withId("avatarimg"), // <img class="align-self-start mr-3" src="/images/Platypus.png">
                                b(" " +sender + "   " ).withText(new SimpleDateFormat("HH:mm").format(new Date())), // <span class="timestamp">HH:mm:ss</span>
                                h6(message).withClass("msgFormat") // <p>hello</p>
                        ) // </div>
                ), // </div>
                br()
        ).render(); // convert / parse java into html
    }

    private static String createHtmlMessageFromServer(String message){
        return article().with(
                div(attrs(".media"),
                        br(),
                        i(message).withId("serverMessage"),
                        br()
                )
        ).render();
    }

    private static String generateUserList(Collection<User> userObjectList){
        StringBuilder userList = new StringBuilder();
        int i = 0;
        userObjectList.forEach(userObject -> {
            userList.append(p(userObject.getUsername()));
        });
    return userList.toString();
    }
}

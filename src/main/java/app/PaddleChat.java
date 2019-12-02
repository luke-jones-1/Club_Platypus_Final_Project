package app;

import models.Model;
import models.Sql2oModel;
import models.UserModel;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.websocket.api.Session;
import org.flywaydb.core.Flyway;
import org.json.JSONObject;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;
import static j2html.TagCreator.span;
import static spark.Spark.*;

public class PaddleChat {

    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>(); // hash of each session and the username as a string
    static String currentSessionUser = null;
    public static String username = "User";
    public static String platypusColour = null;

    public static void broadcastMessage(String sender, String message){
        // selects only open session (websockets that are active) then iterates through each
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            // takes a session makes a websocket request to send a string
            try {
                // creating a string in the format of a JSON Object (kind of like a hash that can be read by multiple languages)
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message)) // sends data to construct messages
                        .put("userlist", userUsernameMap.values()) // sends data to make list of users
                ));
            } catch (Exception e){
                e.printStackTrace(); //prints error log on error
            }
        });
    }



    // takes java variables as arguments and returns html code with the arguments values as text
    private static String createHtmlMessageFromSender(String sender, String message) {
        if (sender == "Server"){ //If it's a server message different formatting applies
            return article().with(
                    div(attrs(".media"),
                        i(message)//italics
                    )
            ).render();
        }
        return article().with(
                div(attrs(".media"), // <div class="media">
                        div(attrs(".media-body"), // <div class="media-body">
                                img(attrs(".align-self-start.mr-3")).withSrc("/images/" + platypusColour + "Platypus.png").withId("avatarimg"), // <img class="align-self-start mr-3" src="/images/Platypus.png">
                                b(" " +sender + "   " ).withText(new SimpleDateFormat("HH:mm").format(new Date())), // <span class="timestamp">HH:mm:ss</span>
                                h6(message).withClass("msgFormat") // <p>hello</p>
                        ) // </div>
                ) // </div>
        ).render(); // convert / parse java into html
    }
}

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
    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1;

    public static void main(String[] args) {

        BasicConfigurator.configure();

        port(Main.getHerokuAssignedPort());

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


        staticFileLocation("/public");
        webSocket("/PaddleChat", PaddleChatWebSocketHandler.class);
        init();
    };

    public static void broadcastMessage(String sender, String message){
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + ":"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }



}

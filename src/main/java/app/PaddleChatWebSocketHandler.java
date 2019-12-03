package app;

import models.UserModel;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import models.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@WebSocket
public class PaddleChatWebSocketHandler {
    private ChatModel chat;
    private User sender;
    private String msg;
    // gets called when web page loaded

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        PaddleChat.userUsernameMap.put(user, PaddleChat.currentUserclass); // adds an element to userUsernameMap
        PaddleChat.broadcastServerMessage(msg = (PaddleChat.userUsernameMap.get(user).getUsername() + " joined the Paddle"));
    }

    // gets called whenever a websocket is closed
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = PaddleChat.userUsernameMap.get(user).getUsername(); // gets username from userUsernameMap
        PaddleChat.userUsernameMap.remove(user); // removes user from userUsernameMap
        PaddleChat.broadcastServerMessage(msg = (username + " has left the Paddle")); // makes the server left the chat message
    }

    // gets called when webSocket.send is used
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        PaddleChat.broadcastMessage(sender = PaddleChat.userUsernameMap.get(user), message);
        System.out.println("ATATATATATATATATATATATA");
        String userID = PaddleChat.userUsernameMap.get(user).getId().toString();
        System.out.println(userID);
        System.out.println(userID.getClass());
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println(currentTime);
        System.out.println(currentTime.getClass());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println(currentDate);
        System.out.println(currentDate.getClass());
        System.out.println(message);
        System.out.println(message.getClass());
        chat.addChatMessage(userID, currentTime, currentDate, message);
    }
}

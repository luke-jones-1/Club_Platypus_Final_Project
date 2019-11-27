package app;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;


@WebSocket
public class PaddleChatWebSocketHandler {
    private String sender, msg;

    // gets called when web page loaded
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
//        System.out.println("this is the session user:");
//        System.out.println(user);
//        System.out.println("this is the end of session user");
        String username = "User" + PaddleChat.nextUserNumber++; // temporary username definition
        PaddleChat.userUsernameMap.put(user, username); // adds an element to userUsernameMap
        PaddleChat.broadcastMessage(sender = "Server", msg = (username + " joined the Paddle"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = PaddleChat.userUsernameMap.get(user);
        PaddleChat.userUsernameMap.remove(user);
        PaddleChat.broadcastMessage(sender = "Server", msg = (username + " has left the Paddle"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        PaddleChat.broadcastMessage(sender = PaddleChat.userUsernameMap.get(user), msg = message);
    }
}

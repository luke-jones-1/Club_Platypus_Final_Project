package app;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;


@WebSocket
public class PaddleChatWebSocketHandler {
    private String sender, msg;

    // gets called when web page loaded
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = PaddleChat.currentUsername; // temporary username definition
        PaddleChat.userUsernameMap.put(user, username); // adds an element to userUsernameMap
        PaddleChat.broadcastMessage(sender = "Server", msg = (username + " joined the Paddle"));
    }

    // gets called whenever a websocket is closed
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = PaddleChat.userUsernameMap.get(user); // gets username from userUsernameMap
        PaddleChat.userUsernameMap.remove(user); // removes user from userUsernameMap
        PaddleChat.broadcastMessage(sender = "Server", msg = (username + " has left the Paddle")); // makes the server left the chat message
    }

    // gets called when webSocket.send is used
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        PaddleChat.broadcastMessage(sender = PaddleChat.userUsernameMap.get(user), msg = message);
    }
}

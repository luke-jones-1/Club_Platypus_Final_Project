package app;

import models.UserModel;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import models.*;

@WebSocket
public class PaddleChatWebSocketHandler {
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
    }
}

package app;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;


@WebSocket
public class PaddleChatWebSocketHandler {
    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + PaddleChat.nextUserNumber++;
        PaddleChat.userUsernameMap.put(user, username);
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

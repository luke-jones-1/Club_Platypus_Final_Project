package app;

import models.UserModel;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import models.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@WebSocket(maxIdleTime=1000000000)
public class PaddleChatWebSocketHandler {
    private ChatModel chat;// defines a null chat which is a type of chatmodel
    private User sender;
    private String msg;
    // gets called when web page loaded


     // constructor
    public PaddleChatWebSocketHandler(ChatModel chat){ // starts constructor and takes a instance of chat class
        this.chat = chat; // initializes chat as the argument so chat can be used in the socket handler
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {

        // for loop for entire of log
//        for (Chat chatinstance : log) {
//            // user class from user id
//            User currentuser = usermodel.fetchUserById(chatinstance.getUser_id().toString());
//            // call broadcast message
//            PaddleChat.broadcastMessage(currentuser, chatinstance.getContent()); // message from instance of chat inside log
//        }
        PaddleChat.userUsernameMap.put(user, PaddleChat.currentUserClass); // adds an element to userUsernameMap
        PaddleChat.broadcastServerMessage(msg = (PaddleChat.userUsernameMap.get(user).getUsername() + " joined the Paddle"));
        PaddleChat.currentUserClass = null;
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
        String userID = PaddleChat.userUsernameMap.get(user).getId().toString();
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        chat.addChatMessage(userID, currentTime, currentDate, message);
        PaddleChat.broadcastMessage(sender = PaddleChat.userUsernameMap.get(user), message);
    }
}

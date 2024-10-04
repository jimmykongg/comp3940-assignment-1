package com.quizapp.quizroom;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/quizRoomSocket")
public class quizRoomSocket {

    @OnOpen
    public void onOpen(Session session){
        System.out.println("Connection is ok");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @OnClose
    public void onClose(){
        System.out.println("close");
    }

    @OnError
    public void onError(Throwable t){
        System.out.println("Connection has error: " + t.getMessage());
    }

}

package com.quizapp.quizroom;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/quizRoomSocket")
public class quizRoomSocket {
    private static final Set<quizRoomSocket> connections = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        connections.add(this);
        String message = String.format("* %s %s", session.getId(), "has joined.");
        System.out.println("Connection is ok");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
        broadcast(message);
    }

    @OnClose
    public void onClose(){
        connections.remove(this);
        System.out.println("close");
    }

    @OnError
    public void onError(Throwable t){
        System.out.println("Connection has error: " + t.getMessage());
    }


    private void broadcast(String message) throws IOException {
        for (quizRoomSocket client : connections) {
            try {
                if (client.session.isOpen()) {
                    client.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                System.err.println("Error sending message to client: " + e.getMessage());
                connections.remove(client);
                client.session.close();
            }
        }
    }
}



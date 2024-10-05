package com.quizapp.quizroom;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/quizRoomSocket")
public class quizRoomSocket {
    private static final Log logger = LogFactory.getLog(quizRoomSocket.class);
    private static final Set<quizRoomSocket> connections = new CopyOnWriteArraySet<>();

    private Session session;
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session) throws UnsupportedEncodingException {
        this.session = session;

        String query = session.getQueryString();
        String token = null;

        if (query != null && query.contains("token=")) {
            token = query.split("token=")[1];
            token = URLDecoder.decode(token, "UTF-8");
            JsonObject jsonObject = JsonParser.parseString(token).getAsJsonObject();
            token = jsonObject.get("token").getAsString();
        }

        if (token != null) {
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            if (decodedJWT != null) {
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();
                String quizIndex = decodedJWT.getClaim("quizIndex").asString();
                System.out.println("Connected user: " + username + ", Role: " + role + ", Quiz Index: " + quizIndex);
            } else {
                System.out.println("Invalid token provided.");
            }
        } else {
            System.out.println("No token provided in WebSocket connection.");
        }

        logger.info("Connection is ok");
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

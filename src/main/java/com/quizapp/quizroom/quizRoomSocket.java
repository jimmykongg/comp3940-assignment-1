package com.quizapp.quizroom;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quizapp.database.DatabaseConnection;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/quizRoomSocket")
public class quizRoomSocket {
    private static final Log logger = LogFactory.getLog(quizRoomSocket.class);
    private static final Set<quizRoomSocket> connections = new CopyOnWriteArraySet<>();

    private Session session;
    private String categoryID;

    @OnOpen
    public void onOpen(Session session) throws UnsupportedEncodingException {
        this.session = session;
        connections.add(this);

        String query = session.getQueryString();
        Map<String, String> queryParams = parseQueryString(query);

        // Store categoryId from websocket URL
        if (query != null && query.contains("categoryID=")) {
            categoryID = queryParams.get("categoryID");

            System.out.println("CategoryID: " + categoryID);
        }

        /*
         * Load user information stored in JWT into websocket session
         */
        String token = queryParams.get("token");
        if (token != null) {
            token = URLDecoder.decode(token, StandardCharsets.UTF_8.toString());
            JsonObject jsonObject = JsonParser.parseString(token).getAsJsonObject();
            token = jsonObject.get("token").getAsString();  // Extract the real JWT token
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);

            if (decodedJWT != null) {
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                // Store user information in WebSocket session user properties
                session.getUserProperties().put("username", username);
                session.getUserProperties().put("role", role);

                // Store quiz index to admins' session only since they're responsible to host the quiz
                if (role.equalsIgnoreCase("admin")) {
                    session.getUserProperties().put("quizIndex", 0);
                }

                System.out.println("Connected user: " + username + ", Role: " + role);
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

    // Method to fetch quiz data from the database
    private Map<String, String> getQuizData() {
        String quizSql = "SELECT id, description, media_id FROM quizzes WHERE category_id = ? AND id > ? LIMIT 1";

        List<Object> quizParams = new ArrayList<>();
        // Assume categoryID is passed via URL and extracted similarly
        quizParams.add(categoryID);
        quizParams.add(getQuizIndex());

        try {
            return DatabaseConnection.queryOne(quizSql, quizParams);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Some helper and ultility methods
     */

    // Some getter methods
    private String getUsername() {
        return (String) session.getUserProperties().get("username");
    }

    private String getRole() {
        return (String) session.getUserProperties().get("role");
    }

    private int getQuizIndex() {
        if (getRole().equalsIgnoreCase("admin")) {
            return (int) session.getUserProperties().get("quizIndex");
        } else {
            return -1;
        }
    }

    // Utility method to parse websocket query string into a map of key-value pairs
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0 && idx < pair.length() - 1) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }
}
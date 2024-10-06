package com.quizapp.quizroom;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    // questionIndex would be updated whenever questions are fetched
    private static int questionIndex = 0;
    private static int categoryID = -1;

    private Session session;
    /*
     * The queue of messages that may build up while another message is being sent. The thread that sends a message is
     * responsible for clearing any queue that builds up while that message is being sent.
     */
//    private Queue<String> messageBacklog = new ArrayDeque<>();
//    private boolean messageInProgress = false;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);

        String query = session.getQueryString();
        Map<String, String> queryParams = parseQueryString(query);

        // Store categoryId from websocket URL
        if (query != null && query.contains("categoryID=")) {
            categoryID = Integer.parseInt(queryParams.get("categoryID"));

            System.out.println("CategoryID: " + categoryID);
        }

        String token = queryParams.get("token");
        try {
            loadHttpSessionData(token, session);
        } catch (UnsupportedEncodingException e) {
            logger.error("Error loading http session data: " + e.getMessage());
        }

        if (getRole().equalsIgnoreCase("admin")) {
            sendQuestionToAdmin(session);
        } else if (getRole().equalsIgnoreCase("general")) {
            sendAnswersToGeneral();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "getNextQuestion":
                sendQuestionToAdmin(session);
                break;
            case "submitAnswer":
                String answerID = jsonObject.get("answerID").getAsString();
                String username = jsonObject.get("username").getAsString();
                System.out.println(answerID);
                System.out.println(username);
                break;
            default:
                logger.error("Unrecognized message type: " + type);
        }

        if (session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(){
        System.out.println(getUsername() + " closes connection");
    }

    @OnError
    public void onError(Throwable t){
        System.out.println("Connection has error: " + t.getMessage());
        t.printStackTrace();
    }

    // Load http session user data stored in JWT into websocket session
    private void loadHttpSessionData(String token, Session session) throws UnsupportedEncodingException {
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

                System.out.println("Connected user: " + username + ", Role: " + role);
            } else {
                System.out.println("Invalid token provided.");
            }
        } else {
            System.out.println("No token provided in WebSocket connection.");
        }

        logger.info("Connection is ok");

        // TODO Change it to send to admin
        Map<String, Object> message = new HashMap<>();
        String joinMessage = "[" + getRole().toUpperCase() + "] " + getUsername() + " has joined.";
        message.put("type", "joinRoom");
        message.put("joinMessage", joinMessage);

        // Convert the data map to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(message);

        // Send JSON data to the admin via WebSocket
        try {
            session.getBasicRemote().sendText(jsonString);
        } catch (IOException e) {
            logger.error("Failed to send join message: " + e.getMessage(), e);
        }
    }

    // Fetch quiz data from the database
    private Map<String, String> getQuizData() {
        String quizSql = "SELECT id, description, media_id FROM quizzes WHERE category_id = ? AND id > ? ORDER BY id ASC LIMIT 1";

        List<Object> quizParams = new ArrayList<>();

        quizParams.add(categoryID);
        quizParams.add(questionIndex);

        try {
            return DatabaseConnection.queryOne(quizSql, quizParams);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fetch media details from the database
    private Map<String, String> getMediaData(Integer mediaId) {
        String mediaSql = "SELECT media_type, file_path FROM media WHERE id = ?";

        List<Object> params = new ArrayList<>();
        params.add(mediaId);

        try {
            return DatabaseConnection.queryOne(mediaSql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Send JSON data (quiz description, mediaType, and filePath) to admin client
    private void sendQuestionToAdmin(Session session) {
        String description = "", mediaType = "", filePath = "";
        int quizID = -1, mediaID = -1;
        // Data where admin needs in order to host the quiz
        Map<String, Object> adminData = new HashMap<>();

        Map<String, String> quizData = getQuizData();

        if (quizData != null && !quizData.isEmpty()) {
            quizID = Integer.parseInt(quizData.get("id"));
            description = quizData.get("description");
            mediaID = Integer.parseInt(quizData.get("media_id"));
        } else {
            // If there's no quiz questions left, close all client connections
            closeAllConnections();
            return;
        }

        if (mediaID > -1) {
            Map<String, String> mediaData = getMediaData(mediaID);
            mediaType = mediaData.get("media_type");
            filePath = mediaData.get("file_path");
        }

        // !!IMPORTANT: Update quizIndex to current quizID in order to load next question
        questionIndex = quizID;

        adminData.put("type", "question");
        adminData.put("description", description);
        adminData.put("mediaType", mediaType);
        adminData.put("filePath", filePath);

        // Convert the data map to a JSON string
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(adminData);

        // Send JSON data to the admin via WebSocket
        try {
            session.getBasicRemote().sendText(jsonString);
        } catch (IOException e) {
            logger.error("Failed to send data to admin: " + e.getMessage(), e);
        }
    }

    // Send JSON answers data (questionID, description) to general clients
    private void sendAnswersToGeneral() {
        Map<String, Object> answersData = new HashMap<>();
        List<Map<String, String>> answers = getQuizAnswers();

        answersData.put("type", "answers");
        answersData.put("username", getUsername());
        answersData.put("role", getRole());
        answersData.put("answers", answers);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(answersData);

        try {
            session.getBasicRemote().sendText(jsonString);
        } catch (IOException e) {
            logger.error("Failed to send JSON answers to general clients: " + e.getMessage(), e);
        }
    }

    // Fetch quiz answers from the database
    private List<Map<String, String>> getQuizAnswers() {
        String ansSql = "SELECT id, description FROM answers WHERE quiz_id = ?";
        List<Object> ansParams = new ArrayList<>();
        ansParams.add(questionIndex);
        List<Map<String, String>> answers = null;
        try {
            answers = DatabaseConnection.query(ansSql, ansParams);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!answers.isEmpty()) {
            return answers;
        } else {
            logger.error("No answer for this question");
            return null;
        }
    }

    // Send JSON data to particular group(s) of clients(
    private void broadcast(String jsonString) {
        for (quizRoomSocket connection : connections) {
            if (connection.getRole().equalsIgnoreCase("general")) {

            }
        }
    }

    // handle user answer submission
    private void handleUserAnswer(String username, String answer) {
        System.out.println("User: " + username + " submitted answer: " + answer);
        // Here you could store the answer in the database or collect it for the admin to review.
    }

    // handle closing all connections and instruct clients to leave the quiz room by URL redirection
    private void closeAllConnections() {
        for (quizRoomSocket client : connections) {
                String redirectURL = "/categories";
                Map<String, String> message = new HashMap<>();
                message.put("type", "redirect");
                message.put("redirectURL", redirectURL);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gson.toJson(message);

            try {
                session.getBasicRemote().sendText(jsonString);
                client.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Quiz ended, redirecting"));
            } catch (IOException e) {
                logger.error("Failed to close connection for client: " + e.getMessage(), e);
            }
        }
    }

    /*
     * Some helper methods and utility methods
     */
    private String getUsername() {
        return (String) session.getUserProperties().get("username");
    }

    private String getRole() {
        return (String) session.getUserProperties().get("role");
    }

    private void setQuizIndex(int curQuizID) {
        if (getRole().equalsIgnoreCase("admin")) {
            session.getUserProperties().put("quizIndex", curQuizID);
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
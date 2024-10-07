package com.quizapp.quizroom;

import com.quizapp.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/createRoom")
public class CreateRoomServlet extends HttpServlet {
    private static final Log logger = LogFactory.getLog(CreateRoomServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String sql = "SELECT * FROM category";
        try{
            List<Map<String, String>> categories = DatabaseConnection.query(sql, null);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/pages/createRoom.jsp").forward(req, res);
        }catch(Exception e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.info("doPost of /createRoom is called");

        String categoryID = req.getParameter("category");
        String roomID = req.getParameter("roomID");

        if (RoomManager.hasID(roomID)) {
            logger.error("Room ID " + roomID + " already exists");
        } else {
            Room newRoom = new Room(categoryID, roomID);
            RoomManager.addRoom(roomID, newRoom);
            logger.info("Room ID " + roomID + " created");
        }

        String url = "/quizRoom?categoryID=" + categoryID + "&roomID=" + roomID;
        res.sendRedirect(url);
    }
}

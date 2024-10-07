package com.quizapp.quizroom;

import com.quizapp.DaoPattern.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/createRoom")
public class CreateRoomServlet extends HttpServlet {

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
        System.out.println("doPost of /createRoom is called");

        String categoryID = req.getParameter("category");
        String roomID = req.getParameter("roomID");

        String url = "/quizRoom?categoryID=" + categoryID + "&roomID=" + roomID;
        res.sendRedirect(url);

//        if(RoomManager.hasID(roomID)){
//            // the room id is already used.
//            // send a error message back.
//        }else{
////            Room newRoom = new Room(categoryID, roomID);
////            RoomManager.addRoom(roomID, newRoom);
//
//        }
    }
}

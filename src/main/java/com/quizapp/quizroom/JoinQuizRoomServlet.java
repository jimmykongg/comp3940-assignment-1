package com.quizapp.quizroom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/joinRoom")
public class JoinQuizRoomServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("doGet method of joinRoom called.");
        Map<String, Room> quizRooms = RoomManager.getRoomMap();

        req.setAttribute("quizRooms", quizRooms);
        req.getRequestDispatcher("/pages/joinRoom.jsp").forward(req, res);
    }
}

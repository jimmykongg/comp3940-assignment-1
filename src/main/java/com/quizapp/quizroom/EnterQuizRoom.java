package com.quizapp.quizroom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/quizRoom")
public class EnterQuizRoom extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String categoryID = req.getParameter("categoryID");
        String roomID = req.getParameter("roomID");

        req.setAttribute("categoryID", categoryID);
        req.setAttribute("roomID", roomID);
        req.getRequestDispatcher("/pages/quizRoom.jsp").forward(req, res);
    }
}

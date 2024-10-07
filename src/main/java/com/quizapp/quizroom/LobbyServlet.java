package com.quizapp.quizroom;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/lobby")
public class LobbyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String role = (String) session.getAttribute("role");

        if (role.equalsIgnoreCase("admin")) {
            req.setAttribute("isAdmin", true);
        }

        req.getRequestDispatcher("/pages/lobby.jsp").forward(req, res);
    }
}

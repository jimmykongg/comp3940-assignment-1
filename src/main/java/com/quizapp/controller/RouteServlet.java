package com.quizapp.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/")
public class RouteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String url = req.getRequestURI();
        if (url.equals("/login")) {
            req.getRequestDispatcher("/pages/login.jsp").forward(req, res);
        } else if (url.equals("/signup")) {
            req.getRequestDispatcher("/pages/signup.jsp").forward(req, res);
        }

        req.getRequestDispatcher("/pages/index.jsp").forward(req, res);
    }
}

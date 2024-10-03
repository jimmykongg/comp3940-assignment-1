package com.quizapp.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/")
public class RouteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("route handler is called");
        String url = req.getRequestURI();

        HttpSession session = req.getSession(false);
        if(url.equals("/")){
            req.getRequestDispatcher("/pages/index.jsp").forward(req, res);
        } else if (url.equals("/login")) {
            req.getRequestDispatcher("/pages/login.jsp").forward(req, res);
        } else if (url.equals("/signup")) {
            req.getRequestDispatcher("/pages/signup.jsp").forward(req, res);
        } else if (url.equals("/admin")) {
            req.getRequestDispatcher("/pages/admin.jsp").forward(req, res);
        }
    }
}

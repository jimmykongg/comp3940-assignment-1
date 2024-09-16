package com.quizapp.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

// Check whether a user has already logged in.
@WebServlet("/api/check-auth")
public class CheckAuthServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        res.setContentType("application/json");

        PrintWriter out = res.getWriter();

        if (session.getAttribute("username") != null) {
            out.println("{\"loggedIn\": true, \"role\": \"" + session.getAttribute("role") + "\"}");
        } else {
            out.println("{\"loggedIn\": false}");
        }

        out.flush();
    }
}

package com.quizapp.authentication;

import com.quizapp.quizroom.JwtUtil;
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

    // Store user info in JWT and send it to client in order to pass data to webSocket
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();

        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            String role = session.getAttribute("role").toString();
            String quizIndex = session.getAttribute("quizIndex").toString();
            String jwt = JwtUtil.generateToken(username, role, quizIndex);

            res.setContentType("application/json");
            out.print("{\"token\": \"" + jwt + "\"}");
        } else {
            out.println("{\"token\": null}");
        }
        out.flush();
    }
}

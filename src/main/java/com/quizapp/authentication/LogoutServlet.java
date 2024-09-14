package com.quizapp.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet{
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        if (session != null) {
            session.invalidate();
            out.println("{\"message\": \"Logout successful\", \"status\": \"ok\"}");
        } else {
            out.println("{\"message\": \"Operation not allowed\"}");
        }
    }
}

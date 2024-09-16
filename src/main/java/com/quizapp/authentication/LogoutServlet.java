package com.quizapp.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet{
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session.getAttribute("username") != null) {
            session.invalidate();
            res.sendRedirect("/");
        } else {
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Operation not allowed\"}");
        }
    }
}

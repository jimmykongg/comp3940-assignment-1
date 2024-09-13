package com.quizapp.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import com.quizapp.database.DatabaseConnection;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        PrintWriter out = res.getWriter();

        if (authenticateUser(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);

            out.println("{\"message\": \"Login successful\", \"status\": \"ok\"}");
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("{\"message\": \"Invalid credentials\", \"status\": \"error\"}");
        }
        out.flush();
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM app_user WHERE username = ?";

        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, username);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

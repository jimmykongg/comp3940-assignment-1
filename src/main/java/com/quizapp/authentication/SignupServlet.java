package com.quizapp.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import com.quizapp.database.DatabaseConnection;

@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"message\": \"Invalid input\"}");
            return;
        };

        try {
            Connection con = DatabaseConnection.initializeDatabase();

            if (isUserNotExisted(username, con, res)) {
                insertUser(username, password, con, res);
                res.getWriter().write("{\"message\": \"Signup successful\"}");
            } else {
                res.getWriter().write("{\"message\": \"Signup failed\"}");
            }
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void insertUser(String username, String password, Connection con, HttpServletResponse res) throws SQLException {
        String query = "INSERT INTO app_user (username, password, role) VALUES (?, ?, ?)";

        PreparedStatement stm = con.prepareStatement(query);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        stm.setString(1, username);
        stm.setString(2, hashedPassword);
        stm.setString(3, "general");

        if (stm.executeUpdate() != 1) throw new SQLException();
    }

    private boolean isUserNotExisted(String username, Connection con, HttpServletResponse res) throws SQLException {
        String query = "SELECT * FROM app_user WHERE username = ?";

        PreparedStatement stm = con.prepareStatement(query);
        stm.setString(1, username);

        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            } else {
                return true;
            }
        }
    }
}

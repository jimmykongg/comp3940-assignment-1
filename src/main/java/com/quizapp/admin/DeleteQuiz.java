package com.quizapp.admin;

import com.quizapp.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/deleteQuiz")
public class DeleteQuiz extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get quiz ID from the form
        System.out.println("Delete Quiz");
        Integer quizId = Integer.parseInt(req.getParameter("quizId"));

        // SQL query to delete the quiz and answers by ID

        String answerSql = "DELETE FROM answers WHERE quiz_id = ?";
        String sql = "DELETE FROM quizzes WHERE id = ?";

        try {
            // Initialize the database connection
            // Prepare parameters for the query
            List<Object> params = new ArrayList<>();
            params.add(quizId);
            DatabaseConnection.execute(answerSql, params);
            // Execute the delete query using DatabaseConnection's execute method
            DatabaseConnection.execute(sql, params);
            res.sendRedirect("/admin");
        } catch (SQLException e) {
            // Handle SQL exceptions and forward to error page with the error message
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
}

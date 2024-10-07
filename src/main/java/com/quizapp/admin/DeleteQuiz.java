package com.quizapp.admin;

import com.quizapp.DaoPattern.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/deleteQuiz/delete/*")
public class DeleteQuiz extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] pathParts = pathInfo.split("=");
        if (pathParts.length < 2) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(pathParts[1]);
        } catch (NumberFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String answerSql = "DELETE FROM answers WHERE quiz_id = ?";
        String sql = "DELETE FROM quizzes WHERE id = ?";

        try {
            List<Object> params = new ArrayList<>();
            params.add(quizId);
            DatabaseConnection.execute(answerSql, params);
            DatabaseConnection.execute(sql, params);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

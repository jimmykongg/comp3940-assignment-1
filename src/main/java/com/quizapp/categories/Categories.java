package com.quizapp.categories;

import com.quizapp.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/categories")
public class Categories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        session.setAttribute("quizIndex", 0);
        String sql = "SELECT * FROM category";
        try {
            List<Map<String, String>> categories = DatabaseConnection.query(sql, null);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/pages/categories.jsp").forward(req, res);
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
}

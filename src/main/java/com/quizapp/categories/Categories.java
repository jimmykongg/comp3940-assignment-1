package com.quizapp.categories;


import com.quizapp.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/categories")
public class Categories extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        System.out.println(session);

        if (session == null) {
            req.getRequestDispatcher("/pages/index.js").forward(req, res);
        } else {
            session.setAttribute("quizIndex", 0);

            System.out.println("quizIndex set to " + session.getAttribute("quizIndex"));
            System.out.println("username: " + session.getAttribute("username"));
            System.out.println("role: " + session.getAttribute("username"));
        }

        String sql = "SELECT * FROM category";
        try {
            List<Map<String, String>> categories = DatabaseConnection.query(sql, null);

            // Use Stream API to sort categories alphabeticaly and lambda function to sort
            List<Map<String, String>> sortedCategories = categories.stream()
                    .sorted((category1, category2) -> category1.get("name").compareToIgnoreCase(category2.get("name")))
                    .collect(Collectors.toList());

            req.setAttribute("categories", sortedCategories);
            req.getRequestDispatcher("/pages/categories.jsp").forward(req, res);
        } catch (SQLException e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
}

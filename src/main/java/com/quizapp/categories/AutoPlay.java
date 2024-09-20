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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@WebServlet("/autoplay")
public class AutoPlay extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer preQuizIndex = (Integer) session.getAttribute("quizIndex");
        Integer categoryId = Integer.parseInt(req.getParameter("category"));

        String quizSql = "SELECT id, description, media_id FROM quizzes WHERE category_id = ? AND id > ? LIMIT 1";
        String ansSql = "SELECT id, description FROM answers WHERE quiz_id = ?";
        List<Object> quizParams = new ArrayList<>();
        List<Object> ansParams = new ArrayList<>();
        quizParams.add(categoryId);
        quizParams.add(preQuizIndex);

        try{
            Map<String, String> quiz = DatabaseConnection.queryOne(quizSql, quizParams);
            if(quiz.isEmpty()){
                req.getRequestDispatcher("./Categories").forward(req, res);
            }else{
                // Get the current quiz's index to query for answers for this quiz.
                Integer curQuizIndex = Integer.parseInt(quiz.get("id"));
                ansParams.add(curQuizIndex);
                List<Map<String, String>> answers = DatabaseConnection.query(ansSql, ansParams);
                if(answers.isEmpty()){
                    // No answers for the current questions. Error
                    req.setAttribute("message", "No answer for this question");
                    req.getRequestDispatcher("/page/error.jsp").forward(req, res);
                }else{
                    // these are the data you want to pass to the jsp file.
                    req.setAttribute("quiz", quiz); // This is the quiz query result, one row.
                    req.setAttribute("quizIndex", curQuizIndex);
                    req.setAttribute("answers", answers);
                    req.setAttribute("category", categoryId);
                    req.getRequestDispatcher("/pages/autoplay.jsp").forward(req, res);
                }
            }
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Integer curQuizIndex = Integer.parseInt(req.getParameter("curQuizIndex"));
        System.out.println("curQuizIndex: " + curQuizIndex);
        String category = req.getParameter("category");
        String sql = "SELECT right_answer FROM answers WHERE id = ?";
        String redirectUrl = "/autoplay?category=" + category;
        List<Object> params = new ArrayList<>();

        try{
            Map<String, String> retInString = DatabaseConnection.queryOne(sql, params);
            String correct = retInString.get("right_answer");

            req.setAttribute("correctAnswer", correct);
            req.setAttribute("quizIndex", curQuizIndex);
            req.setAttribute("category", category);

            // Display correct answer
            req.getRequestDispatcher("/pages/autoplay.jsp").forward(req, res);
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
// using Ziqi's methods instead :D
//    private String getCorrectAnswer(List<Map<String, String>> answers) {
//        for (Map<String, String> answer : answers) {
//            if("t".equals(answer.get("right_answer"))){
//                return answer.get("right_answer");
//            }
//        }
//        return null;
//    }
}
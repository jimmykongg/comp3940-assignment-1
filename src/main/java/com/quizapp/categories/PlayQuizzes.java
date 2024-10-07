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
import java.util.logging.Logger;

@WebServlet("/play")
public class PlayQuizzes extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PlayQuizzes.class.getName());

    // This doGet function will get the Data that we need from database
    // and then forward the request with the data to play.jsp
    // Data that will be fetched from database:
    // from quizzes table: id, description, media_id
    // from answers table: id, description, right_answer
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        // Get the previous quiz Index from session.
        // I create a session in Categories.java, just a dummy session
        Integer preQuizIndex = (Integer) session.getAttribute("quizIndex");
        // Get the categoryID from url
        Integer categoryId = Integer.parseInt(req.getParameter("category"));

        // We need to query both quizzes and answers table, here are some preparation.
        // use the previous quiz index so we can find the next quiz index
        String quizSql = "SELECT id, description, media_id FROM quizzes WHERE category_id = ? AND id > ? LIMIT 1";
        String ansSql = "SELECT id, description FROM answers WHERE quiz_id = ?";
        List<Object> quizParams = new ArrayList<>();
        List<Object> ansParams = new ArrayList<>();
        quizParams.add(categoryId);
        quizParams.add(preQuizIndex);

        try {
            // a general querying function, used when you are sure only one record(row) will be return.
            // note it store the key value pair in String, String format, sometime need casting
            Map<String, String> quiz = DatabaseConnection.queryOne(quizSql, quizParams);
            if(quiz.isEmpty()){
                res.sendRedirect("/categories");
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
                    req.getRequestDispatcher("/pages/play.jsp").forward(req, res);
                }
            }
        } catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer curQuizIndex = Integer.parseInt(req.getParameter("curQuizIndex"));
        Integer selectedAnswerId = Integer.parseInt(req.getParameter("selectedAnswer"));
        String category = req.getParameter("category");
        String sql = "SELECT right_answer FROM answers WHERE id = ?";
        String redirectUrl = "/play?category=" + category;
        List<Object> params = new ArrayList<>();
        params.add(selectedAnswerId);

        try{
            Map<String, String> retInString = DatabaseConnection.queryOne(sql, params);
            // the right answer is selected
            if(retInString.get("right_answer").equals("t")){
                session.setAttribute("quizIndex", curQuizIndex);
            }
            res.sendRedirect(redirectUrl);
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
}

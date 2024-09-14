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
import java.util.logging.Level;


@WebServlet("/play")
public class PlayQuizzes extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PlayQuizzes.class.getName());

    // I need category id and current quiz ID which is stored in the session.
    // Data I need from the database:
    // quizzes: id, description, media_id
    // answers: description, right_answer
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer preQuizIndex = (Integer) session.getAttribute("quizIndex");
        Integer categoryId = Integer.parseInt(req.getParameter("category"));

        // Query preparation for quiz
        String quizSql = "SELECT id, description, media_id FROM quizzes WHERE category_id = ? AND id > ? LIMIT 1";
        String ansSql = "SELECT id, description FROM answers WHERE quiz_id = ?";
        List<Object> quizParams = new ArrayList<>();
        List<Object> ansParams = new ArrayList<>();
        quizParams.add(categoryId);
        quizParams.add(preQuizIndex);


        try{
            Map<String, String> quiz = DatabaseConnection.queryOne(quizSql, quizParams);
            if(quiz.isEmpty()){
                //session.setAttribute("quizIndex", 1); usually I need to do this,but I set it in Categories servlet.
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
                    req.setAttribute("quiz", quiz);
                    req.setAttribute("quizIndex", curQuizIndex);
                    req.setAttribute("answers", answers);
                    req.setAttribute("category", categoryId);
                    req.getRequestDispatcher("/pages/play.jsp").forward(req, res);
                }
            }
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Integer curQuizIndex = Integer.parseInt(req.getParameter("curQuizIndex"));
        System.out.println("curQuizIndex: " + curQuizIndex);
        Integer selectedAnswerId = Integer.parseInt(req.getParameter("selectedAnswer"));
        System.out.println("selectedAnswerId: " + selectedAnswerId);
        String category = req.getParameter("category");
        String sql = "SELECT right_answer FROM answers WHERE id = ?";
        String redirectUrl = "/WebApp_war/play?category=" + category;
        List<Object> params = new ArrayList<>();
        params.add(selectedAnswerId);

        try{
            Map<String, String> retInString = DatabaseConnection.queryOne(sql, params);
            System.out.println("Query result: " + retInString.get("right_answer"));

            String rightAnswerString = retInString.get("right_answer");
            System.out.println("Retrieved right_answer: " + rightAnswerString);

            // the right answer is selected
            if(retInString.get("right_answer").equals("t")) session.setAttribute("quizIndex", curQuizIndex);
            res.sendRedirect(redirectUrl);
        }catch(SQLException e){
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }
}

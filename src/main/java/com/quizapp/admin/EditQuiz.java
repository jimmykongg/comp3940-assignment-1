package com.quizapp.admin;

import com.quizapp.DaoPattern.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/editQuiz")
public class EditQuiz extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Integer quizId = Integer.parseInt(req.getParameter("quizId")) ;


        // get quiz details
        String quizSql = "SELECT * FROM quizzes WHERE id = ?";
        List<Object> quizParams = new ArrayList<>();
        quizParams.add(quizId);

        try {
            List<Map<String, String>> quizList = DatabaseConnection.query(quizSql, quizParams);
            Map<String, String> quiz = quizList.isEmpty() ? null : quizList.get(0);
            req.setAttribute("quiz", quiz);

            // get quiz answers
            String answerSql = "SELECT * FROM answers WHERE quiz_id = ?";
            List<Map<String, String>> answers = DatabaseConnection.query(answerSql, quizParams);
            req.setAttribute("answers", answers);


            // get categories
            String categorySql = "SELECT * FROM category";
            List<Map<String, String>> categories = DatabaseConnection.query(categorySql, null);
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("/pages/editQuiz.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        System.out.println("doPost of /editQuiz is called");
        Integer quizId = Integer.parseInt(req.getParameter("quizId"));
        String description = req.getParameter("description");
        String quizType = req.getParameter("quizType");
        String category = req.getParameter("category");
        String mediaType = req.getParameter("mediaType");
        String mediaPath = req.getParameter("mediaPath");
        Integer mediaId = null;


        if (category.equals("newCategory")) {
            System.out.println("inserting new category");
            String sql = "INSERT INTO category (name) VALUES(?) RETURNING id";
            List<Object> paramsCategory = new ArrayList<>();
            paramsCategory.add(req.getParameter("newCategoryName"));
            List<String> categoryCols = new ArrayList<>();
            categoryCols.add("id");
            try {
                Map<String, String> insertedCategory = DatabaseConnection.executeWithReturn(sql, paramsCategory, categoryCols);
                category = insertedCategory.get("id");
            } catch (Exception e) {
                System.out.println("catch block of category insertion is called");
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }


        if (!mediaType.isEmpty() && !mediaPath.isEmpty()) {
            System.out.println("inserting new media");
            String mediaSql = "INSERT INTO media (media_type, file_path) VALUES(?, ?) RETURNING id";
            List<Object> mediaParams = new ArrayList<>();
            mediaParams.add(mediaType);
            mediaParams.add(mediaPath);
            List<String> mediaCols = new ArrayList<>();
            mediaCols.add("id");
            try {
                Map<String, String> insertedMedia = DatabaseConnection.executeWithReturn(mediaSql, mediaParams, mediaCols);
                mediaId = Integer.parseInt(insertedMedia.get("id"));
            } catch (Exception e) {
                System.out.println("catch block of media is called");
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }

        // Update quiz
        String updateQuizSql = "UPDATE quizzes SET description = ?, media_id = ?, category_id = ? WHERE id = ?";
        List<Object> quizParams = new ArrayList<>();
        quizParams.add(description);
        quizParams.add(mediaId);
        quizParams.add(Integer.parseInt(category));
        quizParams.add(quizId);

        try {
            System.out.println("updating quiz");
            DatabaseConnection.execute(updateQuizSql, quizParams);
        } catch (Exception e) {
            System.out.println("catch block of quiz is called");
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
            return;
        }


        if (quizType.equals("multi")) {
            // Update answers for multiple choice quiz
            String updateAnswersSql = "UPDATE answers SET description = ?, right_answer = ? WHERE quiz_id = ? AND id = ?";

            String[] answers = new String[4];
            answers[0] = req.getParameter("correctAnswer");
            answers[1] = req.getParameter("falseAnswer1");
            answers[2] = req.getParameter("falseAnswer2");
            answers[3] = req.getParameter("falseAnswer3");

            Integer[] answersId = new Integer[4];
            answersId[0] = Integer.parseInt(req.getParameter("correctAnswer_id"));
            answersId[1] = Integer.parseInt(req.getParameter("falseAnswer1_id"));
            answersId[2] = Integer.parseInt(req.getParameter("falseAnswer2_id"));
            answersId[3] = Integer.parseInt(req.getParameter("falseAnswer3_id"));


            for (int i = 0; i < 4; i++) {
                List<Object> answerParams = new ArrayList<>();
                answerParams.add(answers[i]);
                answerParams.add(i == 0); 
                answerParams.add(quizId);
                answerParams.add(answersId[i]);
                try {
                    System.out.println("updating multiple choice");
                    DatabaseConnection.execute(updateAnswersSql, answerParams);
                } catch (Exception e) {
                    System.out.println("catch block of multi is called");
                    req.setAttribute("message", e.getMessage());
                    req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                    return;
                }
            }
        } else {
            // True or False
            boolean correctAnswerTrueFalse = Boolean.parseBoolean(req.getParameter("trueFalseAnswer"));
            String updateTrueFalseSql = "UPDATE answers SET right_answer = ? WHERE quiz_id = ? AND description = ?";

            try {
                System.out.println("updating true false answers");
                // True
                List<Object> trueParams = new ArrayList<>();
                trueParams.add(correctAnswerTrueFalse);
                trueParams.add(quizId);
                trueParams.add("True");
                DatabaseConnection.execute(updateTrueFalseSql, trueParams);

                // False
                List<Object> falseParams = new ArrayList<>();
                falseParams.add(correctAnswerTrueFalse);
                falseParams.add(quizId);
                falseParams.add("False");
                DatabaseConnection.execute(updateTrueFalseSql, falseParams);
            } catch (Exception e) {
                System.out.println("catch block of true false is called");
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }


        res.sendRedirect("/admin");
    }
}

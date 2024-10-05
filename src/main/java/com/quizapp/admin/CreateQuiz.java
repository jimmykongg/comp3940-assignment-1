package com.quizapp.admin;

import com.quizapp.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/createQuiz")
@MultipartConfig
public class CreateQuiz extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res){

        String sql = "SELECT * FROM category";
        try{
            List<Map<String, String>> categories = DatabaseConnection.query(sql, null);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/pages/createQuiz.jsp").forward(req, res);

        }catch(Exception e){

        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String sql = null;
        String description = req.getParameter("description");
        String quizType = req.getParameter("quizType");
        String category = req.getParameter("category");
        String mediaType = req.getParameter("mediaType");
        String mediaPath = req.getParameter("mediaPath");
        Integer mediaId = null;
        Integer quizID = null;

        Part filePart = req.getPart("mediaFile");
        if (filePart != null && filePart.getSize() > 0) {
            String uploadDir = System.getProperty("catalina.home") + "/webapps/ROOT/images/";

            String filePath = uploadDir + mediaPath;
            filePart.write(filePath);
        }

        if(category.equals("newCategory")){
            sql = "INSERT INTO category (name) VALUES(?) RETURNING id, name";
            List<Object>paramsCategory = new ArrayList<>();
            paramsCategory.add(req.getParameter("newCategoryName"));
            List<String> returnColumns = new ArrayList<>();
            returnColumns.add("id");
            returnColumns.add("name");

            try{
                System.out.println("inserting new category");
                Map<String, String> insertedRecord = DatabaseConnection.executeWithReturn(sql, paramsCategory, returnColumns);
                category = insertedRecord.get("id");
            }catch(Exception e){
                System.out.println("category catch called");
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }

        if(!mediaType.isEmpty() && !mediaPath.isEmpty()){
            sql = "INSERT INTO media (media_type, file_path) VALUES(?, ?) RETURNING id";
            List<Object>mediaParams = new ArrayList<>();
            mediaParams.add(mediaType);
            mediaParams.add(mediaPath);
            List<String> returnColumns = new ArrayList<>();
            returnColumns.add("id");

            try{
                System.out.println("inserting new media");
                Map<String, String> insertedRecord = DatabaseConnection.executeWithReturn(sql, mediaParams, returnColumns);
                mediaId = Integer.parseInt(insertedRecord.get("id"));
            }catch(Exception e){
                System.out.println("media catch called");
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }

        sql = "INSERT INTO quizzes (description, media_id, category_id) VALUES(?, ?, ?) RETURNING id";
        List<Object> quizParams = new ArrayList<>();
        quizParams.add(description);
        quizParams.add(mediaId);
        quizParams.add(Integer.parseInt(category));
        List<String> returnColumns = new ArrayList<>();
        returnColumns.add("id");

        try{
            System.out.println("inserting new quiz");
            Map<String, String> insertedRecord = DatabaseConnection.executeWithReturn(sql, quizParams, returnColumns);
            quizID = Integer.parseInt(insertedRecord.get("id"));
        }catch(Exception e){
            System.out.println("quiz catch called");
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
            return;
        }

        if(quizType.equals("multi")){
            String[] answers = new String[4];
            answers[0] = req.getParameter("correctAnswer");
            answers[1] =  req.getParameter("falseAnswer1");
            answers[2] =  req.getParameter("falseAnswer2");
            answers[3] =  req.getParameter("falseAnswer3");


            sql = "INSERT INTO answers (quiz_id, description, right_answer) VALUES (?,?,?),(?,?,?),(?,?,?),(?,?,?)";
            List<Object> multiParams = new ArrayList<>();
            for(int i=0; i<4; i++){
                multiParams.add(quizID);
                multiParams.add(answers[i]);
                multiParams.add(i == 0);
            }

            try{
                System.out.println("inserting new multi choice");
                DatabaseConnection.execute(sql, multiParams);
            }catch(Exception e){
                System.out.println("multi choice catch called" + sql);
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }else{
            boolean correctAnswerTrueFalse = Boolean.parseBoolean(req.getParameter("trueFalseAnswer"));
            sql = "INSERT INTO answers (quiz_id, description, right_answer) VALUES (?,?,?)";
            List<Object> trueParams = new ArrayList<>();
            trueParams.add(quizID);
            trueParams.add("True");
            trueParams.add(correctAnswerTrueFalse);

            try{
                System.out.println("inserting first true-false answer true");
                DatabaseConnection.execute(sql, trueParams);
            }catch(Exception e){
                System.out.println("trueFalse first catch called: " + sql);
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
            List<Object> falseParams = new ArrayList<>();
            falseParams.add(quizID);
            falseParams.add("False");
            falseParams.add(!correctAnswerTrueFalse);

            try{
                System.out.println("inserting second true-false answer false");
                DatabaseConnection.execute(sql, falseParams);
            }catch(Exception e){
                System.out.println("trueFalse second catch called: " + sql);
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("/pages/error.jsp").forward(req, res);
                return;
            }
        }

        System.out.println("all database operation is completed, redirect to admin page");
        res.sendRedirect("/admin");
    }
}

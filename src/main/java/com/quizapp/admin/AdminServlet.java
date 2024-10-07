package com.quizapp.admin;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.util.*;
import com.quizapp.DaoPattern.DatabaseConnection;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res){

        System.out.println("doGet of /admin is called");
        String sql = "SELECT * FROM quizzes";
        try{
            List<Map<String, String>> quizzes = DatabaseConnection.query(sql, null);
            req.setAttribute("quizList", quizzes);
            req.getRequestDispatcher("/pages/admin.jsp").forward(req,res);
        }catch(Exception e){
            System.out.println(e.getStackTrace());
        }
    }










    // Retrieve a list of quizzes from quizzes table
//    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        // Get URL parameters
////        int page = Integer.parseInt(req.getParameter("page"));
////        int limit = Integer.parseInt(req.getParameter("limit"));
//        int page = 1;
//        int limit = 10;
//        int offset = (page - 1) * limit;
//        Connection con = null;
//
//        try {
//            con = DatabaseConnection.initializeDatabase();
//            ArrayList<Map<Integer, String>> quizzes = new ArrayList<>();
//
//            String query = "SELECT id, description FROM quizzes ORDER BY id LIMIT " + limit + " OFFSET " + offset;
//            PreparedStatement stm = con.prepareStatement(query);
//
//            try (ResultSet rs = stm.executeQuery()) {
//                while (rs.next()) {
//                    int id = rs.getInt("id");
//                    String description = rs.getString("description");
//                    Map<Integer, String> quiz = new HashMap<>();
//                    quiz.put(id, description);
//                    quizzes.add(quiz);
//                }
//            }
//            req.setAttribute("quizList", quizzes);
//            req.getRequestDispatcher("/pages/admin.jsp");
////
////            // Convert ArrayList to JSON using Gson
////            Gson gson = new Gson();
////            String json = gson.toJson(quizzes);
////
////            res.setContentType("application/json");
////            PrintWriter out = res.getWriter();
////            out.print(json);
////            out.flush();
//
//        } catch (SQLException e) {
//            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            DatabaseConnection.closeConnection(con);
//            e.printStackTrace();
//        }
//
//    }
}

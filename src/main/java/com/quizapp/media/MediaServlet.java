package com.quizapp.media;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.quizapp.DaoPattern.DatabaseConnection;

@WebServlet("/media")
public class MediaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Integer mediaId = Integer.parseInt(req.getParameter("mediaId"));

        if (mediaId == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing media ID");
            return;
        }

        // database query
        String sql = "SELECT media_type, file_path FROM media WHERE id = ?";
        List<Object> params = new ArrayList<>();
        params.add(mediaId);

        try {
            Map<String, String> mediaData = DatabaseConnection.queryOne(sql, params);
            if (mediaData.isEmpty()) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Media content not found");
                return;
            }

            // file path for videos/audio will be url (ex. nMfPqeZjc2c&t) -> youtube.com/embed/nMfPqeZjc2c&t
            // file path for image will be file path to webapp/images
            String mediaType = mediaData.get("media_type");
            String filePath = mediaData.get("file_path");

            // return to jsp as json
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"mediaType\": \"" + mediaType + "\", \"filePath\": \"" + filePath + "\"}");

        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }
}

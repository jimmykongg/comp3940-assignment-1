package com.quizapp.database;

import java.sql.*;
import java.util.*;

public class DatabaseConnection {
    public static Connection initializeDatabase() throws SQLException {
        final String DB_URL = "jdbc:postgresql://comp3940-quizweb.czk8qgy0a7ba.ca-central-1.rds.amazonaws.com:5432/postgres";
        final String DB_USER = "postgres";
        final String DB_PASSWORD = "phLlFRMrlBmwbCHga0mX";

        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}

        return DriverManager.getConnection(DB_URL, props);
    }

    public static void closeConnection( Connection con ) {
        try
        {
            con.close();
        } catch (Exception e) {}
    }

    public static void execute( Connection con, String sql, List<String> params) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            if(params != null) {
                for(int i=0; i<params.size(); i++) {
                    pstmt.setString(i+1, params.get(i));
                }
            }
            if(pstmt.executeUpdate() == 0) {
                throw new SQLException("No row is updated!");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try { pstmt.close(); } catch (Exception e) {}
        }
    }

    public static List<Map<String, String>> query(String sql, List<String> params) throws SQLException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.initializeDatabase();
            pstmt = con.prepareStatement(sql);
            if(params != null) {
                for(int i=0; i<params.size(); i++) {
                    pstmt.setString(i+1, params.get(i));
                }
            }
            rs = pstmt.executeQuery();
            List<String> columnNames = new ArrayList<>();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for(int i=0;i<rsMetaData.getColumnCount();i++) {
                columnNames.add(rsMetaData.getColumnName(i+1));
            }
            List<Map<String, String>> ret = new ArrayList<>();
            while(rs.next()){
                Map<String, String> record = new HashMap<>();
                for(String columnName : columnNames) {
                    record.put(columnName, rs.getString(columnName));
                }
                ret.add(record);
            }
            return ret;
        } catch (SQLException e) {
            throw e;
        } finally {
            try { rs.close(); } catch (Exception e) {}
            try { pstmt.close(); } catch (Exception e) {}
            try { con.close(); } catch (Exception e) {}
        }

    }


}

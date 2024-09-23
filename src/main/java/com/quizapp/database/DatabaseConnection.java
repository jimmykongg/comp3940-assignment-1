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

    public static void execute(String sql, List<Object> params) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DatabaseConnection.initializeDatabase();
            pstmt = con.prepareStatement(sql);
            if(params != null) {
                for(int i=0; i<params.size(); i++) {
                    Object param = params.get(i);
                    if(param instanceof String) pstmt.setString(i+1, (String)param);
                    if(param instanceof Integer) pstmt.setInt(i+1, (Integer)param);
                    if(param instanceof Boolean) pstmt.setBoolean(i+1, (Boolean)param);
                    if(param == null) pstmt.setObject(i+1, null);
                }
            }
            if(pstmt.executeUpdate() == 0) {
                throw new SQLException("No row is updated!");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try { pstmt.close(); } catch (Exception e) {}
            try { con.close(); } catch (Exception e) {}
        }
    }

    public static List<Map<String, String>> query(String sql, List<Object> params) throws SQLException{
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.initializeDatabase();
            pstmt = con.prepareStatement(sql);
            if(params != null) {
                for(int i=0; i<params.size(); i++) {
                    Object param = params.get(i);
                    if(param instanceof String) pstmt.setString(i+1, (String) param);
                    else if(param instanceof Integer) pstmt.setInt(i+1, (Integer)param);
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

    public static Map<String, String> queryOne(String sql, List<Object> params) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.initializeDatabase();
            pstmt = con.prepareStatement(sql);
            if(params != null) {
                for(int i=0; i<params.size(); i++) {
                    Object param = params.get(i);
                    if(param instanceof String) pstmt.setString(i+1, (String) param);
                    else if(param instanceof Integer) pstmt.setInt(i+1, (Integer)param);
                }
            }
            rs = pstmt.executeQuery();
            List<String> columnNames = new ArrayList<>();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for(int i=0;i<rsMetaData.getColumnCount();i++) {
                columnNames.add(rsMetaData.getColumnName(i+1));
            }
            Map<String, String> record = new HashMap<>();
            if(rs.next()){
                for(String columnName : columnNames) {
                    record.put(columnName, rs.getString(columnName));
                }
                return record;
            }
            return record;
        } catch (SQLException e) {
            throw e;
        } finally {
            try { rs.close(); } catch (Exception e) {}
            try { pstmt.close(); } catch (Exception e) {}
            try { con.close(); } catch (Exception e) {}
        }
    }

    public static Map<String, String> executeWithReturn(String sql, List<Object> params, List<String> ret) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            pstmt = con.prepareStatement(sql);

            if(params != null){
                for(int i=0; i<params.size(); i++){
                    Object param = params.get(i);
                    if(param instanceof String) pstmt.setString(i+1, (String)param);
                    else if(param instanceof Integer) pstmt.setInt(i+1, (Integer)param);
                    else if(param instanceof Boolean) pstmt.setBoolean(i+1, (Boolean)param);
                    else if(param == null) pstmt.setObject(i+1, null);
                }
            }
            rs = pstmt.executeQuery();
            Map<String, String> result = new HashMap<>();
            if(rs.next()){
                if(ret != null){
                    for(String key : ret){
                        result.put(key, rs.getString(key));
                    }
                }
            }

            System.out.println("inserting complete, returning result");
            return result;


        }catch (SQLException e){
            throw e;
        }finally {
            try { rs.close(); } catch (Exception e) {}
            try { pstmt.close(); } catch (Exception e) {}
            try { con.close(); } catch (Exception e) {}
        }
    }

}

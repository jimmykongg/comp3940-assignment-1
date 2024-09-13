package com.quizapp.database;

import java.sql.*;
import java.util.Properties;

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
}

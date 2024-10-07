package com.quizapp.DaoPattern;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IRepository {

    String DB_URL = "jdbc:postgresql://comp3940-quizweb.czk8qgy0a7ba.ca-central-1.rds.amazonaws.com:5432/postgres";
    String DB_USER = "postgres";
    String DB_PASSWORD = "phLlFRMrlBmwbCHga0mX";

    public Connection init() throws SQLException;
    public void close(Connection con);
    public List<Entity> select(String sql, String parameters);
    public int insert(Entity entity) throws SQLException;
    public void update(Entity entity);
    public void delete(String entityType, int id);
}

package com.quizapp.DaoPattern;

import java.sql.*;
import java.util.*;

public class Repository implements IRepository{

    @Override
    public Connection init() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}

        return DriverManager.getConnection(DB_URL, props);
    }

    @Override
    public void close(Connection con) {
        try {
            con.close();
        } catch (Exception e) {}
    }

    @Override
    public List<Entity> select(String entityType, String parameters) {
        List<Entity> ret = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM" + entityType;
        if(!parameters.isEmpty()){
            sql += "WHERE " + parameters;
        }

        try{
            con = init();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<String> columnsName = EntityFactory.create(entityType).getColumnsName();
            while(rs.next()){
                StringBuilder p = new StringBuilder();
                for(int i=0; i<columnsName.size(); i++){
                    String key = columnsName.get(i);
                    String value = rs.getString(key);
                    p.append(key).append("=").append(value);
                    if(i != columnsName.size() - 1) p.append(",");
                }
                ret.add(EntityFactory.createWithParameters(entityType, p.toString()));
            }
        }catch(Exception e){

        }finally {
            try { rs.close(); } catch (Exception e) {}
            try { pstmt.close(); } catch (Exception e) {}
            try { con.close(); } catch (Exception e) {}
        }
        return ret;
    }

    @Override
    public int insert(Entity entity) throws SQLException {
        int index = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        Map<String, Object> keyValuePairs = entity.getKeyValuePairs();
        StringBuilder keys = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        keyValuePairs.forEach((key, value) -> {
            keys.append(key).append(",");
            values.append("?,");
        });

        keys.setLength(keys.length() - 1);
        values.setLength(values.length() - 1);

        keys.append(")");
        values.append(")");

        String sql = "INSERT INTO " + entity.getType() + " " + keys.toString() + " VALUES " + values.toString();
        try {
            con = init();  // Initialize database connection
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  // Return generated keys (e.g., auto-generated ID)

            // Set the values in the prepared statement
            int paramIndex = 1;
            for (Map.Entry<String, Object> entry : keyValuePairs.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    pstmt.setString(paramIndex, (String) value);
                } else if (value instanceof Integer) {
                    pstmt.setInt(paramIndex, (Integer) value);
                } else if (value instanceof Boolean) {
                    pstmt.setBoolean(paramIndex, (Boolean) value);
                } else {
                    pstmt.setObject(paramIndex, value);  // Default case for other types
                }
                paramIndex++;
            }

            // Execute the query and get the number of affected rows
            int affectedRows = pstmt.executeUpdate();

            // Get generated keys (if any)
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                index = generatedKeys.getInt(1);  // Assuming the generated key is an integer (e.g., ID)
            }

            return index;  // Return the generated ID or 0 if none
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;  // Return 0 in case of an error
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void update(Entity entity) {
        Connection con = null;
        PreparedStatement pstmt = null;
        Map<String, Object> keyValuePairs = entity.getKeyValuePairs();

        // Assuming "id" is the primary key column and is present in keyValuePairs
        Object idValue = keyValuePairs.get("id");
        if (idValue == null) {
            throw new IllegalArgumentException("Entity must have an ID to update");
        }

        // Build the SQL UPDATE query
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(entity.getType()).append(" SET ");

        keyValuePairs.forEach((key, value) -> {
            if (!key.equals("id")) {  // Skip the "id" key since it's not updated
                sql.append(key).append(" = ?, ");
            }
        });

        sql.setLength(sql.length() - 2);  // Remove trailing comma and space
        sql.append(" WHERE id = ?");

        try {
            con = init();  // Initialize the database connection
            pstmt = con.prepareStatement(sql.toString());

            // Set the values for the columns
            int paramIndex = 1;
            for (Map.Entry<String, Object> entry : keyValuePairs.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!key.equals("id")) {  // Skip setting "id" in the update values
                    if (value instanceof String) {
                        pstmt.setString(paramIndex, (String) value);
                    } else if (value instanceof Integer) {
                        pstmt.setInt(paramIndex, (Integer) value);
                    } else if (value instanceof Boolean) {
                        pstmt.setBoolean(paramIndex, (Boolean) value);
                    } else {
                        pstmt.setObject(paramIndex, value);  // Default case for other types
                    }
                    paramIndex++;
                }
            }

            // Set the ID value for the WHERE clause
            pstmt.setObject(paramIndex, idValue);

            // Execute the update query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    @Override
    public void delete(String entityType, int id) {
        Connection con = null;
        PreparedStatement pstmt = null;

        // Build the SQL DELETE query
        String sql = "DELETE FROM " + entityType + " WHERE id = ?";

        try {
            con = init();  // Initialize the database connection
            pstmt = con.prepareStatement(sql);

            // Set the ID for the WHERE clause
            pstmt.setInt(1, id);

            // Execute the delete query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

}

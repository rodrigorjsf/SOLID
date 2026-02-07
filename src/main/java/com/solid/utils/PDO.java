package com.solid.utils;

import com.solid.srp.Category;
import com.solid.srp.bad.Course;

import java.sql.*;

public class PDO {

    private static final String DB_URL = "jdbc:h2:mem:test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private Connection connection;

    public PDO() {
        initializeConnection();
        createTables();
    }

    public Connection getConnection() {
        return connection;
    }


    private void initializeConnection() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void createTables() {
        try {
            String categorySQL = "CREATE TABLE IF NOT EXISTS category (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL)";
            String courseSQL = "CREATE TABLE IF NOT EXISTS course (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, category_id INT, description VARCHAR(500), FOREIGN KEY (category_id) REFERENCES category(id))";

            Statement statement = connection.createStatement();
            statement.execute(categorySQL);
            statement.execute(courseSQL);
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void saveCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category (name) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, category.getName());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            category.setId(generatedKeys.getInt(1));
        }
        preparedStatement.close();
    }

    public void saveCourse(Course course) throws SQLException {
        String sql = "INSERT INTO course (name, category_id, description) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, course.getName());
        preparedStatement.setInt(2, course.getCategory().getId());
        preparedStatement.setString(3, course.getDescription());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            course.setId(generatedKeys.getInt(1));
        }
        preparedStatement.close();
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

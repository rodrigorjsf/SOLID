package com.solid.srp.good;

import com.solid.srp.Category;
import com.solid.utils.PDO;
import java.sql.*;

public class CourseRepository {

  private final PDO pdo;

  public CourseRepository(PDO pdo) {
    this.pdo = pdo;
  }

  // ============ SINGLE RESPONSIBILITY ============
  // CourseRepository is ONLY responsible for persistence operations
  // It handles all database operations for courses
  // ============================================

  public void save(Course course) throws SQLException {
    // Save category if it doesn't have an ID yet
    if (course.getCategory().getId() == 0) {
      saveCategory(course.getCategory());
    }

    // Save the course
    String sql = "INSERT INTO course (name, category_id, description) VALUES (?, ?, ?)";
    PreparedStatement preparedStatement =
        pdo.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

  public void saveCategory(Category category) throws SQLException {
    String sql = "INSERT INTO category (name) VALUES (?)";
    PreparedStatement preparedStatement =
        pdo.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, category.getName());
    preparedStatement.executeUpdate();

    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
    if (generatedKeys.next()) {
      category.setId(generatedKeys.getInt(1));
    }
    preparedStatement.close();
  }

  public Course findById(int id) throws SQLException {
    String sql =
        "SELECT c.id, c.name, c.description, cat.id as cat_id, cat.name as cat_name "
            + "FROM course c "
            + "JOIN category cat ON c.category_id = cat.id "
            + "WHERE c.id = ?";

    PreparedStatement preparedStatement = pdo.getConnection().prepareStatement(sql);
    preparedStatement.setInt(1, id);
    ResultSet resultSet = preparedStatement.executeQuery();

    Course course = null;
    if (resultSet.next()) {
      Category category = new Category(resultSet.getInt("cat_id"), resultSet.getString("cat_name"));
      course =
          new Course(
              resultSet.getInt("id"),
              resultSet.getString("name"),
              category,
              resultSet.getString("description"));
    }
    preparedStatement.close();
    return course;
  }

  public void update(Course course) throws SQLException {
    String sql = "UPDATE course SET name = ?, category_id = ?, description = ? WHERE id = ?";
    PreparedStatement preparedStatement = pdo.getConnection().prepareStatement(sql);
    preparedStatement.setString(1, course.getName());
    preparedStatement.setInt(2, course.getCategory().getId());
    preparedStatement.setString(3, course.getDescription());
    preparedStatement.setInt(4, course.getId());
    preparedStatement.executeUpdate();
    preparedStatement.close();
  }

  public void delete(int id) throws SQLException {
    String sql = "DELETE FROM course WHERE id = ?";
    PreparedStatement preparedStatement = pdo.getConnection().prepareStatement(sql);
    preparedStatement.setInt(1, id);
    preparedStatement.executeUpdate();
    preparedStatement.close();
  }
}

package com.solid.srp.good;

import com.solid.srp.Category;
import com.solid.srp.utils.PDO;
import java.sql.*;

/**
 * Repository for managing Course entity persistence operations.
 *
 * <p>This class follows the Single Responsibility Principle by having ONE clear responsibility:
 * handling all database operations for Course entities. It implements the Repository Pattern,
 * which provides a separation between the domain layer (Course entity) and the data access
 * layer (persistence operations).
 *
 * <p><b>SRP ADHERENCE:</b> This class has only one reason to change: when the persistence
 * strategy or database operations need to be modified. The Course entity class does NOT
 * change when persistence logic changes, and vice versa.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>Saving new Course entities to the database</li>
 *   <li>Retrieving Course entities from the database</li>
 *   <li>Updating existing Course entities</li>
 *   <li>Deleting Course entities</li>
 *   <li>Managing relationships with Category entities</li>
 * </ul>
 *
 * <p><b>Design benefits:</b>
 * <ul>
 *   <li><b>Separation of concerns:</b> Course represents data, Repository handles persistence</li>
 *   <li><b>Easy testing:</b> Can mock CourseRepository for unit testing Course logic</li>
 *   <li><b>Flexible implementation:</b> Can change from SQL to NoSQL without affecting Course</li>
 *   <li><b>SQL injection prevention:</b> Uses PreparedStatements for all queries</li>
 *   <li><b>Encapsulation:</b> All database knowledge is contained in this class</li>
 * </ul>
 *
 * <p><b>Contrast with bad design:</b>
 * The bad SRP example puts these methods directly in the Course class, violating SRP. This
 * repository pattern shows the correct approach.
 *
 * @see Course for the entity that this repository manages
 * @see PDO for database connection management
 * @see com.solid.srp.bad.Course for what NOT to do
 */
public class CourseRepository {

  /** Database connection manager. */
  private final PDO pdo;

  /**
   * Constructs a CourseRepository with the specified database connection.
   *
   * @param pdo the database connection manager
   */
  public CourseRepository(PDO pdo) {
    this.pdo = pdo;
  }

  // ============ SINGLE RESPONSIBILITY ============
  // This repository is ONLY responsible for persistence operations.
  // It knows about the database, SQL queries, and how to map data.
  // The Course entity doesn't know about any of this.
  // =============================================

  /**
   * Persists a course to the database.
   *
   * <p>If the course's category doesn't have an ID yet, this method will save the category
   * first to ensure referential integrity. The generated database IDs are automatically
   * assigned back to the Course and Category objects.
   *
   * <p><b>Database operations:</b>
   * <ul>
   *   <li>Inserts the category if needed (INSERT INTO category)</li>
   *   <li>Inserts the course (INSERT INTO course)</li>
   *   <li>Sets generated IDs on both objects</li>
   * </ul>
   *
   * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
   *
   * @param course the course to save (must have name, category, and description set)
   * @throws SQLException if a database error occurs during the insert operation
   */
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

  /**
   * Saves a category to the database.
   *
   * <p>This is a helper method used by save() when a course has a new category that hasn't
   * been persisted yet. The generated database ID is automatically assigned back to the
   * Category object.
   *
   * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
   *
   * @param category the category to save (must have name set)
   * @throws SQLException if a database error occurs
   */
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

  /**
   * Retrieves a course by its ID.
   *
   * <p>Performs a SQL JOIN with the category table to retrieve both the course data and its
   * associated category in a single operation.
   *
   * <p><b>Database operation:</b> SELECT with JOIN to fetch course and category data
   *
   * <p><b>Security:</b> Uses PreparedStatement with parameter binding to prevent SQL injection.
   *
   * @param id the course ID to retrieve
   * @return the Course if found with its category loaded, or null if no course with that ID exists
   * @throws SQLException if a database error occurs
   */
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

  /**
   * Updates an existing course in the database.
   *
   * <p>Updates the course's name, category association, and description. The category itself
   * must already exist in the database (i.e., have a valid ID).
   *
   * <p><b>Database operation:</b> UPDATE course SET ... WHERE id = ?
   *
   * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
   *
   * @param course the course with updated data (must have an ID)
   * @throws SQLException if a database error occurs
   */
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

  /**
   * Deletes a course from the database.
   *
   * <p><b>Database operation:</b> DELETE FROM course WHERE id = ?
   *
   * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
   *
   * @param id the ID of the course to delete
   * @throws SQLException if a database error occurs
   */
  public void delete(int id) throws SQLException {
    String sql = "DELETE FROM course WHERE id = ?";
    PreparedStatement preparedStatement = pdo.getConnection().prepareStatement(sql);
    preparedStatement.setInt(1, id);
    preparedStatement.executeUpdate();
    preparedStatement.close();
  }
}

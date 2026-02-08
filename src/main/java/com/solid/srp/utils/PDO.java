package com.solid.srp.utils;

import com.solid.srp.Category;
import com.solid.srp.bad.Course;

import java.sql.*;

/**
 * Database connection manager using H2 in-memory database.
 *
 * <p>This class handles database initialization, connection management, and provides utility
 * methods for basic database operations. It automatically creates the required database schema
 * on initialization.
 *
 * <p><b>Key responsibilities:</b>
 * <ul>
 *   <li>Managing JDBC connections to H2 database</li>
 *   <li>Creating database tables on initialization</li>
 *   <li>Providing access to the database connection</li>
 *   <li>Closing connections when done</li>
 * </ul>
 *
 * <p><b>Database Details:</b>
 * <ul>
 *   <li><b>Type:</b> H2 in-memory database (auto-deleted when JVM exits)</li>
 *   <li><b>Driver:</b> org.h2.Driver</li>
 *   <li><b>URL:</b> jdbc:h2:mem:test</li>
 *   <li><b>Tables created:</b> category (id, name), course (id, name, category_id FK, description)</li>
 * </ul>
 *
 * <p><b>Usage in examples:</b>
 * <ul>
 *   <li><b>Bad SRP example:</b> Uses PDO directly through Course.saveToDatabase()</li>
 *   <li><b>Good SRP example:</b> Uses PDO through CourseRepository (proper separation)</li>
 * </ul>
 *
 * <p><b>Note:</b> This class demonstrates low-level database access. The good SRP example
 * shows how to wrap this in a repository pattern for better separation of concerns.
 *
 * @see com.solid.srp.bad.Course (violates SRP by using PDO directly)
 * @see com.solid.srp.good.CourseRepository (proper SRP using PDO)
 */
public class PDO {

    /** JDBC URL for H2 in-memory database. */
    private static final String DB_URL = "jdbc:h2:mem:test";

    /** Database username for H2. */
    private static final String DB_USER = "sa";

    /** Database password (empty for in-memory H2). */
    private static final String DB_PASSWORD = "";

    /** Active database connection. */
    private Connection connection;

    /**
     * Constructs a new PDO instance and initializes the database.
     *
     * <p>This constructor performs two key operations:
     * <ol>
     *   <li>Initializes the JDBC connection to H2 database</li>
     *   <li>Creates required tables (category and course) if they don't exist</li>
     * </ol>
     *
     * <p>Any errors during initialization are logged to stderr, but the constructor will
     * not throw exceptions (to match the example's error handling style).
     */
    public PDO() {
        initializeConnection();
        createTables();
    }

    /**
     * Gets the active database connection.
     *
     * <p>This connection is shared for all database operations. Callers should not close
     * this connection directly - use {@link #close()} instead.
     *
     * @return the JDBC connection to the H2 database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes the JDBC connection to the H2 database.
     *
     * <p>This method:
     * <ol>
     *   <li>Loads the H2 JDBC driver class</li>
     *   <li>Establishes a connection using DriverManager</li>
     * </ol>
     *
     * <p>Any errors are caught and logged to stderr (connection initialization errors are
     * suppressed for demo purposes).
     */
    private void initializeConnection() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Creates the required database tables if they don't already exist.
     *
     * <p>Creates two tables:
     * <ol>
     *   <li><b>category:</b> id (AUTO_INCREMENT), name (VARCHAR 255)</li>
     *   <li><b>course:</b> id (AUTO_INCREMENT), name (VARCHAR 255), category_id (FK to category),
     *       description (VARCHAR 500)</li>
     * </ol>
     *
     * <p>Uses "CREATE TABLE IF NOT EXISTS" to allow safe re-initialization.
     *
     * <p>The course table has a foreign key constraint linking category_id to the category table,
     * maintaining referential integrity.
     *
     * <p>Any errors during table creation are caught and logged to stderr.
     */
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

    /**
     * Saves a category to the database.
     *
     * <p><b>Note:</b> This method is used by the bad SRP example ({@link Course}) to
     * demonstrate the violation. In the good example, this functionality is encapsulated
     * in {@link com.solid.srp.good.CourseRepository}.
     *
     * <p>The generated database ID is automatically assigned back to the category object
     * using {@link Category#setId(int)}.
     *
     * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
     *
     * @param category the category to persist (must have name set)
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Saves a course to the database.
     *
     * <p><b>Note:</b> This method is used by the bad SRP example ({@link Course}) to
     * demonstrate the violation. In the good example, this functionality is encapsulated
     * in {@link com.solid.srp.good.CourseRepository}.
     *
     * <p>The generated database ID is automatically assigned back to the course object
     * using {@link Course#setId(int)}.
     *
     * <p><b>Security:</b> Uses PreparedStatement to prevent SQL injection.
     *
     * <p><b>Assumes:</b> The category has already been saved (has a valid category_id).
     *
     * @param course the course to persist (must have name, category with ID, and description set)
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Closes the database connection.
     *
     * <p>Should be called when done with database operations. This method safely checks if
     * the connection exists and is open before closing.
     *
     * <p>Any errors during closing are caught and logged to stderr (to match demo's error handling).
     */
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

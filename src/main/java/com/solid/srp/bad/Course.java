package com.solid.srp.bad;

import com.solid.srp.Category;
import com.solid.srp.utils.PDO;
import java.sql.SQLException;

/**
 * Course entity that violates the Single Responsibility Principle.
 *
 * <p>This class demonstrates SRP violation by combining two distinct responsibilities:
 * <ul>
 *   <li><b>Representing course data</b> (domain model - data fields and getters/setters)</li>
 *   <li><b>Managing database persistence</b> (data access layer - saveToDatabase method)</li>
 * </ul>
 *
 * <p><b>SRP VIOLATION:</b> This class has multiple reasons to change:
 * <ul>
 *   <li>When course data structure needs modification (e.g., add new fields)</li>
 *   <li>When database persistence logic needs changes (e.g., switch from SQL to NoSQL)</li>
 *   <li>When connection pool strategy changes</li>
 *   <li>When error handling strategy needs updating</li>
 * </ul>
 *
 * <p><b>Problems with this design:</b>
 * <ul>
 *   <li><b>Cannot test without database:</b> Unit tests must set up actual database</li>
 *   <li><b>Hard to maintain:</b> Database changes require modifying entity class</li>
 *   <li><b>Tight coupling:</b> Course is coupled to PDO and database implementation</li>
 *   <li><b>Code reuse impossible:</b> Cannot use Course in contexts without database</li>
 *   <li><b>Difficult to extend:</b> Adding new persistence methods pollutes entity</li>
 *   <li><b>Violation of SRP:</b> Multiple reasons to change the same class</li>
 *   <li><b>Poor separation of concerns:</b> Business logic mixed with infrastructure</li>
 * </ul>
 *
 * @see com.solid.srp.good.Course for the correct implementation
 * @see com.solid.srp.good.CourseRepository for where persistence logic belongs
 */
public class Course {

  /** Unique identifier for the course. */
  private Integer id;

  /** Name of the course. */
  private String name;

  /** Category to which this course belongs. */
  private Category category;

  /** Detailed description of the course content and objectives. */
  private String description;

  /**
   * Constructs a new course without an ID.
   *
   * <p>Use this constructor when creating a new course that hasn't been persisted yet. The
   * ID will be assigned after saving to the database.
   *
   * @param name the course name
   * @param category the category this course belongs to
   * @param description the course description
   */
  public Course(String name, Category category, String description) {
    this.name = name;
    this.category = category;
    this.description = description;
  }

  /**
   * Constructs a course with all fields including ID.
   *
   * <p>Use this constructor when creating a course instance from persisted data.
   *
   * @param id the unique course identifier
   * @param name the course name
   * @param category the category this course belongs to
   * @param description the course description
   */
  public Course(int id, String name, Category category, String description) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.description = description;
  }

  // ============ VIOLATION OF SRP ============
  // A Course class should ONLY represent course data (domain model),
  // but here it also handles database operations (persistence layer).
  // This is a classic example of mixing concerns.
  // =============================================

  /**
   * Creates and initializes a database connection.
   *
   * <p><b>SRP VIOLATION:</b> A domain entity should not be responsible for creating database
   * connections. This method mixes database infrastructure concerns with business entity
   * concerns.
   *
   * <p>This method demonstrates the problem with the bad design: the Course entity has to
   * know how to create database connections, which is completely unrelated to representing
   * course data.
   *
   * @return a new PDO database connection object
   * @see PDO for database functionality
   */
  public PDO connection() {
    PDO pdo = new PDO();
    System.out.println("Database initialized and tables created.\n");
    return pdo;
  }

  /**
   * Saves this course and its category to the database.
   *
   * <p><b>SRP VIOLATION:</b> This is the primary SRP violation in this class. The Course
   * object knows how to persist itself to the database. This violates SRP for these reasons:
   *
   * <ul>
   *   <li><b>Mixing concerns:</b> Domain model (data) mixed with persistence (infrastructure)</li>
   *   <li><b>Multiple reasons to change:</b> Changes to database strategy require modifying
   *       Course</li>
   *   <li><b>Untestable:</b> Cannot test Course logic without database infrastructure</li>
   *   <li><b>Reusability:</b> Cannot use Course in contexts where database isn't available</li>
   *   <li><b>Coupling:</b> Course is tightly coupled to PDO and SQL implementation</li>
   * </ul>
   *
   * <p><b>Better approach:</b> Create a separate {@code CourseRepository} class whose sole
   * responsibility is managing Course persistence. The Course class should only represent
   * data.
   *
   * <p>Note: Any SQLException that occurs is caught internally and logged to stderr.
   *
   * @see com.solid.srp.good.CourseRepository for the correct approach
   */
  public void saveToDatabase() {
    PDO pdo = connection();
    try {
      pdo.saveCategory(category);
      pdo.saveCourse(this);
      System.out.println("Course saved to database successfully!");
    } catch (SQLException e) {
      System.err.println("Error saving to database: " + e.getMessage());
    } finally {
      pdo.close();
    }
  }

  // ============ END OF SRP VIOLATION ============

  /**
   * Gets the unique identifier of this course.
   *
   * @return the course ID
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this course.
   *
   * @param id the course ID
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of this course.
   *
   * @return the course name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this course.
   *
   * @param name the course name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the category of this course.
   *
   * @return the course category
   */
  public Category getCategory() {
    return category;
  }

  /**
   * Sets the category of this course.
   *
   * @param category the course category
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Gets the description of this course.
   *
   * @return the course description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this course.
   *
   * @param description the course description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns a string representation of this course.
   *
   * @return a string in the format "Course{id=..., name='...', category=..., description='...'}"
   */
  @Override
  public String toString() {
    return "Course{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", category="
        + category
        + ", description='"
        + description
        + '\''
        + '}';
  }
}

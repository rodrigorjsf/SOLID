package com.solid.srp.good;

import com.solid.srp.Category;

/**
 * Course entity following the Single Responsibility Principle.
 *
 * <p>This class has a single, well-defined responsibility: representing course data. It does
 * not handle persistence, validation, or any other concerns beyond data representation and
 * access.
 *
 * <p><b>SRP ADHERENCE:</b> This class has only one reason to change: when the course data
 * structure needs to be modified. It does NOT change when:
 * <ul>
 *   <li>Database persistence logic changes</li>
 *   <li>Data validation rules change</li>
 *   <li>Storage implementation changes (SQL to NoSQL, etc.)</li>
 *   <li>Caching strategy changes</li>
 * </ul>
 *
 * <p><b>Separation of concerns:</b>
 * <ul>
 *   <li><b>This class ({@code Course}):</b> Represents course data only</li>
 *   <li><b>{@link CourseRepository}:</b> Handles all database persistence operations</li>
 * </ul>
 *
 * <p><b>Benefits of this design:</b>
 * <ul>
 *   <li><b>Easy to test:</b> Can test Course logic without database</li>
 *   <li><b>Easy to maintain:</b> Changes to persistence don't affect Course</li>
 *   <li><b>Reusable:</b> Can use Course in any context, with or without database</li>
 *   <li><b>Loose coupling:</b> Course doesn't depend on database implementation</li>
 *   <li><b>Clear intent:</b> Purpose of the class is immediately obvious</li>
 * </ul>
 *
 * @see CourseRepository for persistence operations
 * @see com.solid.srp.bad.Course for an example of SRP violation
 */
public class Course {

  /** Unique identifier for the course. */
  private int id;

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
   * ID will be assigned by the database when the course is saved through {@link
   * CourseRepository}.
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
   * <p>Use this constructor when creating a course instance from persisted data, such as
   * when retrieving from the database through {@link CourseRepository}.
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

  // ============ SINGLE RESPONSIBILITY ============
  // This Course class is ONLY responsible for representing course data.
  // It does NOT handle database operations (see CourseRepository).
  // It does NOT handle validation, caching, or other concerns.
  // This is clean separation of concerns.
  // =============================================

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
   * <p>This is typically set by the repository after persisting to the database.
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
   * @return a string in the format "GoodCourse{id=..., name='...', category=..., description='...'}"
   */
  @Override
  public String toString() {
    return "GoodCourse{"
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

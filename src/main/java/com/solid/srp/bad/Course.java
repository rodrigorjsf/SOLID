package com.solid.srp.bad;

import com.solid.srp.Category;
import com.solid.utils.PDO;
import java.sql.SQLException;

public class Course {

  private Integer id;
  private String name;
  private Category category;
  private String description;

  public Course(String name, Category category, String description) {
    this.name = name;
    this.category = category;
    this.description = description;
  }

  public Course(int id, String name, Category category, String description) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.description = description;
  }

  // ============ VIOLATION OF SRP ============
  // A Course class should only represent course data,
  // but here it also handles database operations (persistence)
  // The Course object knows how to persist itself to the database

  public PDO connection() {
    PDO pdo = new PDO();
    System.out.println("Database initialized and tables created.\n");
    return pdo;
  }

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

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

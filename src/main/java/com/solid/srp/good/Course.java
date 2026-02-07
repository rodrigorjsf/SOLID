package com.solid.srp.good;

import com.solid.srp.Category;

public class Course {

  private int id;
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

  // ============ SINGLE RESPONSIBILITY ============
  // GoodCourse is ONLY responsible for representing course data
  // It does NOT know anything about database operations
  // ============================================

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

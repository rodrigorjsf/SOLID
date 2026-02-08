package com.solid.srp.bad;

import com.solid.srp.Category;

/**
 * Main class demonstrating violation of the Single Responsibility Principle.
 *
 * <p>This example shows how the {@link Course} class violates SRP by combining data
 * representation with database persistence logic. It creates a Course entity that knows how
 * to save itself to the database, which violates the principle that a class should have only
 * one reason to change.
 *
 * <p><b>What this example demonstrates:</b>
 * <ul>
 *   <li>Course class mixes domain logic (data) with infrastructure logic (persistence)</li>
 *   <li>Course class has multiple reasons to change (data structure or database strategy)</li>
 *   <li>Course class is tightly coupled to database implementation</li>
 *   <li>Cannot test Course without database setup</li>
 *   <li>Cannot reuse Course in contexts without a database</li>
 * </ul>
 *
 * @see com.solid.srp.good.GoodSRP for the correct implementation using separation of concerns
 * @see Course for the problematic implementation
 */
public class BadSRP {
  /**
   * Entry point for the bad SRP example.
   *
   * <p>Demonstrates how a Course entity that violates SRP (mixing data representation with
   * persistence) creates several problems:
   *
   * <ol>
   *   <li>Creates a Category entity</li>
   *   <li>Creates a Course entity (which knows how to save itself)</li>
   *   <li>Calls saveToDatabase() on the Course (SRP violation)</li>
   *   <li>Shows the resulting course state</li>
   * </ol>
   *
   * <p><b>The SRP violation:</b> The Course class has two responsibilities:
   * <ol>
   *   <li>Representing course data</li>
   *   <li>Managing its own persistence to the database</li>
   * </ol>
   *
   * <p>This means the Course class has two reasons to change:
   * <ul>
   *   <li>When the course data structure needs to be modified</li>
   *   <li>When the database persistence strategy needs to change</li>
   * </ul>
   *
   * <p>Compare this with the good example in {@link com.solid.srp.good.GoodSRP}, where
   * responsibilities are properly separated.
   */
  void main() {
    System.out.println("=== SRP Violation Example ===\n");

    // Creating a category
    Category category = new Category("Programming");
    System.out.println("Created category: " + category);

    // Creating a course with the category object
    Course course =
        new Course("Java Fundamentals", category, "Learn the basics of Java programming");
    System.out.println("Created course: " + course);

    // VIOLATION OF SRP: Course class handles both data representation AND database persistence
    System.out.println("\nSaving course to database...");
    course.saveToDatabase();

    System.out.println("\nCourse details after saving:");
    System.out.println("  ID: " + course.getId());
    System.out.println("  Name: " + course.getName());
    System.out.println("  Category: " + course.getCategory());
    System.out.println("  Description: " + course.getDescription());
  }
}

package com.solid.srp.bad;

import com.solid.srp.Category;

class BadSRP {
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

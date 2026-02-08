package com.solid.srp.good;

import com.solid.srp.Category;
import com.solid.srp.utils.PDO;

/**
 * Main class demonstrating proper adherence to the Single Responsibility Principle.
 *
 * <p>This example shows how separating concerns between {@link Course} (data representation)
 * and {@link CourseRepository} (persistence) creates more maintainable, testable, and
 * flexible code.
 *
 * <p><b>Key principle:</b> Each class has a single, well-defined responsibility:
 * <ul>
 *   <li><b>Course:</b> Represents course data only</li>
 *   <li><b>CourseRepository:</b> Handles all database persistence operations</li>
 * </ul>
 *
 * <p>This separation provides:
 * <ul>
 *   <li>Better testability (can mock the repository)</li>
 *   <li>Easier maintenance (changes don't affect other classes)</li>
 *   <li>Clear responsibilities (each class has one reason to change)</li>
 *   <li>Loose coupling (Course doesn't know about database)</li>
 * </ul>
 *
 * @see com.solid.srp.bad.BadSRP for a violation example
 * @see Course for the data representation entity
 * @see CourseRepository for the persistence handler
 */
public class GoodSRP {

  /**
   * Prints a detailed comparison between bad and good SRP implementations.
   *
   * <p>Shows the key differences in responsibilities, design benefits, and why the good
   * implementation is superior for maintainability and testability.
   */
  private static void printSRPComparison() {
    System.out.println(
        "\n"
            + "===========================================\n"
            + "SRP PRINCIPLE COMPARISON\n"
            + "===========================================\n"
            + "\n❌ BAD Implementation (Course class):\n"
            + "   - Course class has 2 responsibilities:\n"
            + "     1. Represent course data\n"
            + "     2. Handle database persistence (saveToDatabase)\n"
            + "     3. Handle database close connection\n"
            + "   - Violates Single Responsibility Principle\n"
            + "   - Changes to database logic require modifying Course class\n"
            + "   - Hard to test (needs database for unit tests)\n"
            + "\n✅ GOOD Implementation (Course + CourseRepository):\n"
            + "   - Course class has 1 responsibility:\n"
            + "     1. Represent course data\n"
            + "   - CourseRepository class has 1 responsibility:\n"
            + "     1. Handle all database persistence operations\n"
            + "   - Follows Single Responsibility Principle\n"
            + "   - Changes to database logic only affect Repository\n"
            + "   - Easy to test (can mock repository)\n"
            + "\n===========================================\n");
  }

  /**
   * Entry point for the good SRP example.
   *
   * <p>Demonstrates the following workflow with proper SRP adherence:
   *
   * <ol>
   *   <li>Initialize database connection</li>
   *   <li>Create repository for handling persistence</li>
   *   <li>Create category and course entities</li>
   *   <li>Use repository to save course (not the entity itself)</li>
   *   <li>Retrieve course from database</li>
   *   <li>Update course using repository</li>
   *   <li>Show comparison between bad and good designs</li>
   * </ol>
   *
   * <p><b>Key observation:</b> The Course class never handles database operations. All
   * persistence is delegated to CourseRepository. This is clean separation of concerns.
   */
  void main() {
    System.out.println("=== Good SRP Implementation Example ===\n");

    // Initialize the database
    PDO pdo = new PDO();
    System.out.println("Database initialized and tables created.\n");

    // Create the repository for handling course persistence
    CourseRepository courseRepository = new CourseRepository(pdo);
    System.out.println("CourseRepository created.\n");

    // Creating a category
    Category category = new Category("Web Development");
    System.out.println("Created category: " + category);

    // Creating a course with the category object
    Course course =
        new Course(
            "Spring Boot Masterclass",
            category,
            "Master Spring Boot framework for enterprise applications");
    System.out.println("Created course: " + course);

    // CORRECT SRP: Course only represents data, Repository handles persistence
    System.out.println("\nSaving course to database using repository...");
    try {
      courseRepository.save(course);
      System.out.println("Course saved to database successfully!\n");

      System.out.println("Course details after saving:");
      System.out.println("  ID: " + course.getId());
      System.out.println("  Name: " + course.getName());
      System.out.println("  Category: " + course.getCategory());
      System.out.println("  Description: " + course.getDescription());

      // Retrieve the course from database
      System.out.println("\nRetrieving course from database...");
      Course retrievedCourse = courseRepository.findById(course.getId());
      if (retrievedCourse != null) {
        System.out.println("Retrieved course: " + retrievedCourse);
      }

      // Update the course
      System.out.println("\nUpdating course...");
      retrievedCourse.setDescription("Master Spring Boot framework with advanced patterns");
      courseRepository.update(retrievedCourse);
      System.out.println("Course updated successfully!");

      // Retrieve updated course
      Course updatedCourse = courseRepository.findById(retrievedCourse.getId());
      System.out.println("Updated course: " + updatedCourse);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Close database connection
      pdo.close();
      System.out.println("\nDatabase connection closed.");
    }

    printSRPComparison();
  }
}

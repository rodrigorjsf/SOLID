package com.solid.srp;

/**
 * Represents a course category.
 *
 * <p>This is a simple data entity used to classify and organize courses. It follows the
 * Single Responsibility Principle by having only one responsibility: representing category
 * data. It does not handle persistence, validation, or any business logic beyond data
 * storage and retrieval.
 *
 * <p>This class is used as a shared component in both the bad and good SRP examples to
 * demonstrate how a simple entity should be kept separate from persistence logic.
 *
 * <p><b>SRP ADHERENCE:</b> This class has one reason to change: when the category data
 * structure needs to be modified. It does not change when persistence strategy changes,
 * which is delegated to the repository pattern.
 *
 * @see com.solid.srp.bad.Course for an example of what NOT to do
 * @see com.solid.srp.good.Course for an example of what TO do
 */
public class Category {

  /** Unique identifier for the category. Database primary key. */
  private int id;

  /** Name of the category (e.g., "Programming", "Web Development"). */
  private String name;

  /**
   * Constructs a category without an ID.
   *
   * <p>Use this constructor when creating a new category that hasn't been persisted yet.
   * The ID will be assigned when the category is saved to the database.
   *
   * @param name the category name
   */
  public Category(String name) {
    this.name = name;
  }

  /**
   * Constructs a category with all fields including ID.
   *
   * <p>Use this constructor when creating a category instance from persisted data, such as
   * when retrieving from the database.
   *
   * @param id the unique category identifier
   * @param name the category name
   */
  public Category(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the unique identifier of this category.
   *
   * @return the category ID (0 if not yet persisted)
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this category.
   *
   * <p>This is typically called by the repository after persisting the category to the
   * database and receiving the generated ID.
   *
   * @param id the category ID
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of this category.
   *
   * @return the category name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this category.
   *
   * @param name the category name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns a string representation of this category.
   *
   * @return a string in the format "Category{id=..., name='...'}"
   */
  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}

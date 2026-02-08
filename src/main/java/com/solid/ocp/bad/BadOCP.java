package com.solid.ocp.bad;

/**
 * Demonstrates violation of the Open/Closed Principle (OCP).
 *
 * <p>This class shows how using conditional logic (if-else statements) to determine behavior
 * violates OCP. The problem is that to add new video types, the existing {@link Video}
 * class must be modified.
 *
 * <p><b>OCP VIOLATION:</b> The Video class is CLOSED for extension (cannot add new types
 * without modification) and OPEN for modification (must change the code to add new types).
 * This is the opposite of what OCP prescribes.
 *
 * <p>Problems with this approach:
 * <ul>
 *   <li>Adding new video types requires modifying existing code</li>
 *   <li>If-else chains are fragile and error-prone</li>
 *   <li>Risk of breaking existing types when adding new ones</li>
 *   <li>No polymorphism - string-based type checking instead</li>
 *   <li>Violates Single Responsibility Principle (class has multiple reasons to change)</li>
 *   <li>Difficult to test in isolation</li>
 * </ul>
 *
 * @see com.solid.ocp.good.GoodOCP for a correct implementation following OCP
 */
public class BadOCP {

  /**
   * Represents a video with type-based behavior determination.
   *
   * <p><b>OCP VIOLATION:</b> This design uses a string type field and conditional logic in
   * calculateInterest() to determine behavior. Adding new video types requires modifying
   * this class.
   *
   * <p>Every time a new video type is needed (Documentary, Podcast, etc.), the
   * calculateInterest() method must be modified with an additional else-if branch. This
   * violates the principle of being "closed for modification."
   */
  public class Video {

    /** The type of video (e.g., "Movie", "TVShow"). String-based type checking. */
    private final String type;

    /**
     * Constructs a Video with the specified type.
     *
     * @param type the video type identifier (should be "Movie" or "TVShow")
     */
    public Video(String type) {
      this.type = type;
    }

    /**
     * Calculates interest based on video type using conditional logic.
     *
     * <p><b>OCP VIOLATION:</b> This method uses if-else chains to check the type string and
     * determine behavior. Adding new video types requires modifying this method:
     *
     * <ul>
     *   <li>To add "Documentary": Add another else-if block</li>
     *   <li>To add "Podcast": Add another else-if block</li>
     *   <li>And so on...</li>
     * </ul>
     *
     * <p>This violates the "closed for modification" part of OCP. The class is open for
     * modification when it should be closed (no changes to existing code).
     *
     * <p>Better approach: Use polymorphism with an abstract base class or interface, where
     * each type implements its own calculateInterest() method without modifying the base.
     */
    public void calculateInterest() {
      if (type.equals("Movie")) {
        System.out.println("Calculating interest for Movie...");
      } else if (type.equals("TVShow")) {
        System.out.println("Calculating interest for TV Show...");
      }
    }

    /**
     * Gets the type of this video.
     *
     * @return the video type string
     */
    public String getType() {
      return type;
    }
  }

}

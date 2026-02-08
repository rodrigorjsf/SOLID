package com.solid.ocp.good;

/**
 * Demonstrates proper adherence to the Open/Closed Principle (OCP) using inheritance.
 *
 * <p>This implementation shows how polymorphism through abstract classes allows extending
 * functionality without modifying existing code. New video types can be added by creating
 * new subclasses that implement the abstract method.
 *
 * <p><b>OCP ADHERENCE:</b> The Video class is CLOSED for modification (no changes needed
 * when adding new types) and OPEN for extension (new subclasses can be created). This is
 * exactly what OCP prescribes.
 *
 * <p>Benefits of this approach:
 * <ul>
 *   <li>Adding new video types requires no changes to existing code</li>
 *   <li>Uses polymorphism instead of type checking</li>
 *   <li>Compiler ensures all types implement calculateInterest()</li>
 *   <li>Zero risk of regression when adding new types</li>
 *   <li>Easy to test each type independently</li>
 *   <li>Clear separation of concerns</li>
 * </ul>
 *
 * @see com.solid.ocp.bad.BadOCP for a violation example
 */
public class GoodOCP {

  /**
   * Abstract base class for all video types.
   *
   * <p>This class defines the contract that all video types must follow. The abstract
   * method {@link #calculateInterest()} must be implemented by each subclass. This allows
   * new video types to be added without modifying this base class.
   *
   * <p><b>OCP ADHERENCE:</b> This class is:
   * <ul>
   *   <li><b>Closed for modification:</b> No changes needed to add new types</li>
   *   <li><b>Open for extension:</b> New types extend this class and provide implementations</li>
   * </ul>
   *
   * <p>Example of extending:
   * <pre>{@code
   * public class Documentary extends Video {
   *   @Override
   *   public void calculateInterest() {
   *     System.out.println("Calculating interest for Documentary...");
   *   }
   * }
   * }</pre>
   *
   * <p>No changes to Video or existing subclasses needed!
   */
  public abstract static class Video {

    /**
     * Calculates interest for this video type.
     *
     * <p>Each subclass implements its own specific logic for calculating interest. This
     * method is called by client code through polymorphism, so the actual type doesn't need
     * to be checked.
     *
     * <p>This design follows OCP by allowing behavior variation through extension (subclasses)
     * rather than modification (if-else chains).
     */
    public abstract void calculateInterest();

  }

  /**
   * Movie video type implementation.
   *
   * <p>Extends {@link Video} to provide movie-specific interest calculation. This class can
   * be added, modified, or deleted without affecting the base Video class or other video
   * types.
   *
   * <p>Demonstrates OCP by adding functionality through extension:
   * <ul>
   *   <li>No modification to Video class</li>
   *   <li>No modification to TVShow or other types</li>
   *   <li>Can be independently developed and tested</li>
   * </ul>
   */
  public static class Movie extends Video {
    /**
     * Calculates interest for movies.
     *
     * <p>Implements the abstract method from Video with movie-specific logic.
     */
    @Override
    public void calculateInterest() {
      System.out.println("Calculating interest for Movie...");
    }
  }

  /**
   * TV Show video type implementation.
   *
   * <p>Extends {@link Video} to provide TV show-specific interest calculation. Like Movie,
   * this class demonstrates how new types are added without modifying existing code.
   *
   * <p>Demonstrates OCP principles:
   * <ul>
   *   <li>Self-contained implementation</li>
   *   <li>No changes needed to base Video</li>
   *   <li>Independent testing possible</li>
   *   <li>Can be added to running system without recompilation</li>
   * </ul>
   */
  public static class TVShow extends Video {

    /**
     * Calculates interest for TV shows.
     *
     * <p>Implements the abstract method from Video with TV show-specific logic.
     */
    @Override
    public void calculateInterest() {
      System.out.println("Calculating interest for TV Show...");
    }

  }

}

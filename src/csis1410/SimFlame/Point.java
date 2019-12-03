/**
 * Name:       Tim Hansen, Jacob Winters, Adrianna Jones, Mortada Shogar
 * Assignment: SimFlame (Team Assignment)
 * File:       Point.java
 * Date:       2019-12-02
 */
package csis1410.SimFlame;

/**
 * Represents coordinates in 2D space
 * 
 * @author Tim Hansen
 */
public class Point {
   
   // Fields
   
   private int x;
   private int y;
   
   // Constructors
   
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }
   // Methods
   
   /**
    * Gets the x coordinate
    * @return the x coordinate
    */
   public int getX() {
      return x;
   }
   
   /**
    * Gets the y coordinate
    * @return the y coordinate
    */
   public int getY() {
      return y;
   }
   
   /**
    * Sets the y coordinate to the specified number
    * @param x the number to set it to
    */
   public void setX(int x) {
      this.x = x;
   }
   
   /**
    * Sets the y coordinate to the specified number
    * @param y the number to set it to
    */
   public void setY(int y) {
      this.y = y;
   }

   /**
    * HashCode method
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + x;
      result = prime * result + y;
      return result;
   }

   /**
    * Compares this Point to another
    * 
    * @return true if they are the same object
    * @return true if the x and y coordinates are the same
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!(obj instanceof Point))
         return false;
      Point other = (Point) obj;
      if (x != other.x)
         return false;
      if (y != other.y)
         return false;
      return true;
   }
   
}
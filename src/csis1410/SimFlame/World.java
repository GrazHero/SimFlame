/**
 * Name:       Tim Hansen, Jacob Winters, Adrianna Jones, Mortada Shogar
 * Assignment: SimFlame (Team Assignment)
 * File:       World.java
 * Date:       2019-12-02
 */
package csis1410.SimFlame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class representing the state of the world
 *
 * @author Tim Hansen
 */
public class World {
   
   // Fields
   private int width;
   private int height;
   private int pixelSize = 1;
   private double[] heatMap; // contains the heat values for the world
   private HashSet<Point> fuel; /* a set of coordinates which contain fuel
                                   NOTE: This used to be an ArrayList.
                                   It's better to have it be a set, since
                                   we don't want it to have any duplicate elements */
   private int[] windMapX; // contains the x component of wind vectors. Lower resolution than heatMap
   private int[] windMapY; // y component
   private int windMapBlockSize = 16; // the windMap contains one element for every windMapBlockSize elements of heatMap
   private Callback heatUpdateCallback = null; // the callback that gets fired when the world is updated
   private Random rand;
   
   // Constructors
   
   /**
    * Constructor for the world
    * 
    * if width or height are less than 1, constructs a 1x1 world
    * 
    * @param width the width of the grid
    * @param height the height of the grid
    */
   public World(int width, int height) {
      // width and height can't be less than 1
      if(width < 1 || height < 1) {
         throw(new IndexOutOfBoundsException("World cannot have negative dimensions"));
      }
      this.width = width;
      this.height = height;
      heatMap = new double[width * height];
      Arrays.fill(heatMap, 0.0);
      fuel = new HashSet<Point>();
      rand = new Random();
      // wind
      int windMapLength = (width * height) / windMapBlockSize + windMapBlockSize; /* 1 element of the wind map for
                                                                                   * every windMapBlockSize of the heat map.
                                                                                   * Adding windMapBlockSize in case the numbers
                                                                                   * don't divide evenly. This means there may
                                                                                   * be slightly more wind than needed. */
      windMapX = new int[windMapLength];
      windMapY = new int[windMapLength];
      for(int i = 0; i < windMapLength; i++) {
         windMapX[i] = rand.nextInt(3) - 1;
         windMapY[i] = rand.nextInt(3) - 1;
      }
   }
   
   /**
    * Constructor that allows the user to specify a pixel size
    * 
    * @param width the width
    * @param height the height
    * @param pixelSize the pixel size
    * @throws IllegalArgumentException if pixelSize is less than 1
    */
   public World(int width, int height, int pixelSize) {
      this(width, height); // call the other constructor
      if(pixelSize > 0)
         this.pixelSize = pixelSize;
      else throw new IllegalArgumentException(); // pixel size must be greater than 0
   }
   
   // Methods
   
   /**
    * Gets the pixel size
    * 
    * @return the pixel size
    */
   public int getPixelSize() {
      return pixelSize;
   }
   
   /**
    * Takes an index into the heatMap and returns the 
    * wind x component at that point
    * 
    * @param i the index
    * @return the wind x component between -1 and 1
    */
   public int getWindXAt(int i) {
      return windMapX[i / windMapBlockSize];
   }
   
   /**
    * Randomizes the wind's x component at the given index
    * 
    * @param i the index
    */
   public void iterateWindXAt(int i) {
      windMapX[i / windMapBlockSize] = rand.nextInt(3) - 1;
   }
   
   /**
    * Randomizes the wind's y component at the given index
    * @param i the index
    */
   public void iterateWindYAt(int i) {
      windMapY[i / windMapBlockSize] = rand.nextInt(3) - 1;
   }
   
   /**
    * Takes an index into the heatMap and returns the 
    * wind y component at that point
    * 
    * @param i the index
    * @return the wind y component between -1 and 1
    */
   public int getWindYAt(int i) {
      return windMapY[i / windMapBlockSize];
   }
   
   
   /**
    * Adds fuel at the given point
    * 
    * @param p the point
    */
   public void addFuelAt(Point p) {
      int x = p.getX();
      int y = p.getY();
      if(x >= width || y >= height || x < 0 || y < 0)
         return; // no purpose in adding an out-of-bounds point
      synchronized(this) {
         fuel.add(p);
      }
   }
   
   /**
    * Removes fuel at the given point
    * @param p the point
    */
   public void removeFuelAt(Point p) {
      int x = p.getX();
      int y = p.getY();
      if(x >= width || y >= height || x < 0 || y < 0)
         return;
      synchronized(fuel) {
         fuel.remove(p);
      }
   }
   
   /**
    * Adds a line of fuel to the world using a line draw algorithm
    * 
    * @param start the starting point of the line
    * @param end the ending point of the line
    */
   public void addFuelLine(Point start, Point end) {
      // if I want to draw a line with 30 width and 10 height...
      /*  ------------------------------
       * |xxx
       * |   xxx
       * |      xxx
       * |         xxx
       * |            xxx
       * |               xxx
       * |                  xxx
       * |                     xxx
       * |                        xxx
       * |                           xxx
       */
      // the first pixel starts at (0,0)
      // the next pixel moves down by 1/3 of a pixel
      // the next pixel moves down by 2/3 of a pixel
      // the next moves down by 3/3 of a pixel
      
      
      if(start.getX() >= width || start.getY() >= height ||
         end.getX() >= width || end.getY() >= height ||
         start.getX() < 0 || start.getY() < 0 ||
         end.getX() < 0 || end.getY() < 0) {
         return; // out of bounds
      }
      
      int deltaX = end.getX() - start.getX();
      int deltaY = end.getY() - start.getY();
      int xDir = (deltaX > 0 ? 1 : -1);
      int yDir = (deltaY > 0 ? 1 : -1);
      
      synchronized(fuel) {
         // lines where the start and end point are identical
         if(start.equals(end)) {
            fuel.add(start);
            return;
         }
         
         // horizontal lines
         if(deltaX != 0 && deltaY == 0) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(x == end.getX())
                  loop = false;
               fuel.add(new Point(x, y));
               x += xDir;
            }
            return;
         }
         
         // vertical lines
         if(deltaX == 0 && deltaY != 0) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(y == end.getY())
                  loop = false;
               fuel.add(new Point(x, y));
               y += yDir;
            }
            return;
         }
         
         // perfectly diagonal lines
         if(Math.abs(deltaX) == Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(x == end.getX() && y == end.getY())
                  loop = false;
               fuel.add(new Point(x, y));
               x += xDir;
               y += yDir;
            }
            return;
         }
         
         // line where deltaX is greater than deltaY
         if(Math.abs(deltaX) > Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            int numerator = 0;
            boolean loop = true;
            while(loop) {
               if(x == end.getX())
                  loop = false;
               fuel.add(new Point(x, y));
               x+= xDir;
               numerator += Math.abs(deltaY);
               if(numerator >= Math.abs(deltaX)) {
                  numerator -= Math.abs(deltaX);
                  y += yDir;
               }
            }
            return;
         }
         
         // line where deltaY is less than deltaX
         if(Math.abs(deltaX) < Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            int numerator = 0;
            boolean loop = true;
            while(loop) {
               if(y == end.getY())
                  loop = false;
               fuel.add(new Point(x, y));
               y += yDir;
               numerator += Math.abs(deltaX);
               if(numerator >= Math.abs(deltaY)) {
                  numerator -= Math.abs(deltaY);
                  x += xDir;
               }
            }
            return;
         }
      }
   }
   
   /**
    * Removes a line of fuel to the world using a line draw algorithm
    * 
    * @param start the starting point of the line
    * @param end the ending point of the line
    */
   public void removeFuelLine(Point start, Point end) {
      
      if(start.getX() >= width || start.getY() >= height ||
            end.getX() >= width || end.getY() >= height ||
            start.getX() < 0 || start.getY() < 0 ||
            end.getX() < 0 || end.getY() < 0) {
            return; // out of bounds
      }
      
      int deltaX = end.getX() - start.getX();
      int deltaY = end.getY() - start.getY();
      int xDir = (deltaX > 0 ? 1 : -1);
      int yDir = (deltaY > 0 ? 1 : -1);
      
      synchronized(fuel) {
         // lines where the start and end point are identical
         if(start.equals(end)) {
            fuel.remove(start);
            return;
         }
         
         // horizontal lines
         if(deltaX != 0 && deltaY == 0) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(x == end.getX())
                  loop = false;
               fuel.remove(new Point(x, y));
               x += xDir;
            }
            return;
         }
         
         // vertical lines
         if(deltaX == 0 && deltaY != 0) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(y == end.getY())
                  loop = false;
               fuel.remove(new Point(x, y));
               y += yDir;
            }
            return;
         }
         
         // perfectly diagonal lines
         if(Math.abs(deltaX) == Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            boolean loop = true;
            while(loop) {
               if(x == end.getX() && y == end.getY())
                  loop = false;
               fuel.remove(new Point(x, y));
               x += xDir;
               y += yDir;
            }
            return;
         }
         
         // line where deltaX is greater than deltaY
         if(Math.abs(deltaX) > Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            int numerator = 0;
            boolean loop = true;
            while(loop) {
               if(x == end.getX())
                  loop = false;
               fuel.remove(new Point(x, y));
               x+= xDir;
               numerator += Math.abs(deltaY);
               if(numerator >= Math.abs(deltaX)) {
                  numerator -= Math.abs(deltaX);
                  y += yDir;
               }
            }
            return;
         }
         
         // line where deltaY is less than deltaX
         if(Math.abs(deltaX) < Math.abs(deltaY)) {
            int x = start.getX();
            int y = start.getY();
            int numerator = 0;
            boolean loop = true;
            while(loop) {
               if(y == end.getY())
                  loop = false;
               fuel.remove(new Point(x, y));
               y += yDir;
               numerator += Math.abs(deltaX);
               if(numerator >= Math.abs(deltaY)) {
                  numerator -= Math.abs(deltaY);
                  x += xDir;
               }
            }
            return;
         }
      }
   }
   
   /**
    * Sets the heat value at the given coordinates
    * 
    * @param x the x coordinate
    * @param y the y coordinate
    * @param heat the new heat value
    */
   public void setHeatAt(int x, int y, double heat) {
      heatMap[pointToIndex(new Point(x, y))] = heat;
   }
   
   /**
    * Sets the heat value at the given index to the array
    * @param i the index
    * @param heat the new heat value
    */
   public void setHeatAt(int i, double heat) {
      heatMap[i] = heat;
   }
   
   /**
    * Sets the heat to zero across the whole world
    */
   public void resetHeat() {
      Arrays.fill(heatMap, 0);
      heatUpdateCallback.fire();
   }
   
   /**
    * Gets the heat value at the given coordinates
    * 
    * @param x the x coordinate
    * @param y the y coordinate
    * @return the heat value
    */
   public double getHeatAt(int x, int y) {
      int newX = x;
      int newY = y;
      if(x < 0)
         newX = 0;
      if(x >= width)
         newX = width - 1;
      if(y < 0)
         newY = 0;
      if(y >= height)
         newY = height - 1;
      return heatMap[pointToIndex(new Point(newX, newY))];
   }
   
   /**
    * Gets the heat at the given index to the array
    * 
    * @param i the index
    * @return the heat value
    */
   public double getHeatAt(int i) {
      int newI = i;
      if(i < 0)
         newI = 0;
      if(i >= width * height)
         newI = width * height - 1;
      return heatMap[newI];
   }

   
   /**
    * Sets the callback to be fired when the world updates
    * 
    * @param callback the callback to fire on update 
    */
   public void setUpdateCallback(Callback callback) {
      heatUpdateCallback = callback;
   }
   
   /**
    * Gets the update callback
    * 
    * @return the callback
    */
   public Callback getUpdateCallback() {
      return heatUpdateCallback;
   }
   
   /**
    * Removes all fuel and sets all heat to 0
    */
   public void clear() {
      fuel.clear();
      Arrays.fill(heatMap, 0.0);
      heatUpdateCallback.fire();
   }
   
   /**
    * Replaces the world's heat map with a new one. Returns the current one.
    * Useful since we'll be double buffering the heat map.
    * 
    * @param newMap the new heat map for the world to use
    * @return the current heat map used by the world
    */
   public double[] swapHeatMap(double[] newMap) {
      if(heatMap.length != newMap.length) {
         throw(new IllegalArgumentException("Argument to swapHeatMap must be an array of the same length as the World's heatMap"));
      }
      double[] oldMap = heatMap;
      heatMap = newMap;
      heatUpdateCallback.fire();
      return oldMap;
   }
   
   /**
    * Converts two dimensional coordinates to an index usable in
    * the one dimensional heat map
    * 
    * @param p the point the convert
    * @return the index
    */
   public int pointToIndex(Point p) {
      return this.width * p.getY() + p.getX();
   }
   
   /**
    * converts an index of the array into a two dimensional point
    * 
    * @param i the index
    * @return the point
    */
   public Point indexToPoint(int i) {
      // y = i / width
      // x = i % width
      return new Point(i % width, i / width);
   }
   
   /**
    * Gets the width of the world
    * 
    * @return the width
    */
   public int getWidth() {
      return width;
   }
   
   /**
    * Gets the height of the world
    * 
    * @return the height
    */
   public int getHeight() {
      return height;
   }
   
   /**
    * Gets the heat map
    * 
    * @return the heat map
    */
   public double[] getHeatMap() {
      return heatMap;
   }
   
   /**
    * Gets the fuel set
    * 
    * @return the fuel set
    */
   public Set<Point> getFuelSet() {
      synchronized(fuel) {
         return fuel;
      }
   }
   
}
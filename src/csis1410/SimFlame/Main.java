/**
 * Name:       Tim Hansen, Jacob Winters, Adrianna Jones, Mortada Shogar
 * Assignment: SimFlame (Team Assignment)
 * File:       Main.java
 * Date:       2019-12-02
 */
package csis1410.SimFlame;

/**
 * The class containing the main method
 *
 * @author Tim Hansen
 */
public class Main {
   
   // Methods
   
   /**
    * The main method.
    * Instantiates the World, Simulation and Window.
    * 
    * @param args command line arguments
    */
   public static void main(String[] args) {
      World world = new World(400, 400);
      Simulation simulation = new Simulation(world);
      Window window = new Window(simulation);
      window.setVisible(true);
   }
   
}
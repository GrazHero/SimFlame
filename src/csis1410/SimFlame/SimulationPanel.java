package csis1410.SimFlame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The panel responsible for graphically drawing the simulation as well as
 * translating mouse events into coordinates usable by the simulation.
 *
 */
public class SimulationPanel extends JPanel implements MouseListener {
   
   // Fields
   private Simulation simulation;
   private MouseEvent lastMouseEvent = null;
   private int cellSize = 5; // how big to draw each cell 
   private int worldWidth; // 
   private int worldHeight;
   private Color backgroundColor = Color.BLACK;
   private Color gridColor = Color.WHITE;
   private Color fuelColor = Color.ORANGE;
   private boolean gridVisible = false;
   
   // Private Classes
   
   /**
    * The callback that tells the panel to redraw itself
    * 
    * Gets fired when the world has been updated
    */
   private class RedrawCallback implements Callback {
      
      /**
       * Tells the panel to redraw itself
       */
      public void fire() {
         repaint();
      }
      
   }
   
   // Constructors
   
   /**
    * Constructor for SimulationPanel
    * @param simulation the simulation
    */
   public SimulationPanel(Simulation simulation) {
      this.simulation = simulation;
      this.worldWidth = simulation.getWorld().getWidth();
      this.worldHeight = simulation.getWorld().getHeight();
      setPreferredSize(new Dimension(worldWidth * cellSize, worldHeight * cellSize));
   }
   
   /**
    * Constructor for SimulationPanel with a user provided cell size
    * @param simulation the simulation
    * @param cellSize the cell size
    */
   public SimulationPanel(Simulation simulation, int cellSize) {
      this.simulation = simulation;
      this.worldWidth = simulation.getWorld().getWidth();
      this.worldHeight = simulation.getWorld().getHeight();
      this.cellSize = cellSize; 
      setPreferredSize(new Dimension(worldWidth * cellSize, worldHeight * cellSize));
   }
   
   // Methods
   
   /**
    * Converts mouse coordinates to coordinates which can be used as indices in the world
    * 
    * @param e the MouseEvent whose coordinates to convert
    * @return a Point with the converted coordinates
    */
   public Point mouseCoordsToGridCoords(MouseEvent e) {
      // convert from window coords to panel coords
      MouseEvent panelEvent = SwingUtilities.convertMouseEvent(getTopLevelAncestor(), e, this);
      
      // convert from panel coords to world coords
      int panelX = panelEvent.getX();
      int panelY = panelEvent.getY();
      int convertedX = panelX / cellSize;
      int convertedY = panelY / cellSize;
      return new Point(convertedX, convertedY);
   }
   
   @Override
   public void paintComponent(Graphics g) {
      int backgroundWidth = worldWidth * cellSize;
      int backgroundHeight = worldHeight * cellSize;
      
      // draw the background
      g.setColor(backgroundColor);
      g.fillRect(0, 0, backgroundWidth, backgroundHeight);
      
      // draw the grid
      if(gridVisible) {
         g.setColor(gridColor);
         // vertical lines
         for(int i = 0; i < worldWidth; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, backgroundHeight);
         }
         // horizontal lines
         for(int i = 0; i < worldHeight; i++) {
            g.drawLine(0, i * cellSize, backgroundWidth, i * cellSize);
         }
      }
   }
   
   public void setGridVisible(boolean b) {
      gridVisible = b;
      repaint();
   }
   
   @Override
   public void mouseClicked(MouseEvent e) {
      // TODO: Write me
   }
   
   @Override
   public void mouseEntered(MouseEvent e) {
      // TODO: Write me
   }
   
   @Override
   public void mouseExited(MouseEvent e) {
      // TODO: Write me
   }
   
   @Override
   public void mousePressed(MouseEvent e) {
      // TODO: Write me
   }
   
   @Override
   public void mouseReleased(MouseEvent e) {
      // TODO: Write me
   }
}
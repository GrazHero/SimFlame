package csis1410.SimFlame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The panel responsible for graphically drawing the simulation as well as
 * translating mouse events into coordinates usable by the simulation.
 *
 */
public class SimulationPanel extends JPanel implements MouseListener, MouseMotionListener {
   
   // Fields
   private Simulation simulation;
   private Point lastGridPosition = null;
   private int cellSize = 5; // how big to draw each cell 
   private int worldWidth; // 
   private int worldHeight;
   private Color backgroundColor = Color.BLACK;
   private Color gridColor = Color.WHITE;
   private Color fuelColor = Color.ORANGE;
   private boolean gridVisible = false;
   private boolean fuelVisible = true;
   private boolean flameVisible = true;
   private int buttonDown = 0; // 0 = none, 1 = left mouse, 2 = middle mouse, 3 = right mouse
   private Timer mouseDraggedTimer; /* while the mouse is being dragged, repaints the panel at
                                     * a fixed interval */
   private long lastDragRepaintTime = 0;
   
   
   // Private Classes
   
   private class mouseDraggedTimerTask extends TimerTask {
      @Override
      public void run() {
         
      }
   }
   
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
         if (lastGridPosition == null)
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
      simulation.getWorld().setUpdateCallback(new RedrawCallback());
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
      simulation.getWorld().setUpdateCallback(new RedrawCallback());
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
      
      // draw the flame
      if(flameVisible) {
         int heatMapLength = simulation.getWorld().getWidth() * simulation.getWorld().getHeight();
         for(int i = 0; i < heatMapLength; i++) {
            // test
            Point p = simulation.getWorld().indexToPoint(i);
            float redValue = (float)(simulation.getWorld().getHeatAt(i));
            float greenValue = (float)(simulation.getWorld().getHeatAt(i) / 5);
            Color flameColor = new Color(redValue, greenValue, 0.0f);
            g.setColor(flameColor);
   
            int x = p.getX() * cellSize;
            int y = p.getY() * cellSize;
            g.fillRect(x, y, cellSize, cellSize);
            
         }
      }
      
      // draw fuel
      if(fuelVisible) {
         Set<Point> fuel = simulation.getWorld().getFuelSet();
         synchronized(fuel) {
            g.setColor(fuelColor);
            for(Point el : simulation.getWorld().getFuelSet()) {
               int x = el.getX() * cellSize;
               int y = el.getY() * cellSize;
               g.fillRect(x, y, cellSize, cellSize);
            }
         }
      }
      
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
   
   public void setFuelVisible(boolean b) {
      fuelVisible = b;
      repaint();
   }
   
   public void setFlameVisible(boolean b) {
      flameVisible = b;
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
      // we need to initialize the lastMouseEvent variable
      lastGridPosition = mouseCoordsToGridCoords(e);
      int button = e.getButton();
      buttonDown = button;
      if(button == 1) // left button
         simulation.getWorld().addFuelAt(mouseCoordsToGridCoords(e)); // add fuel where the mouse is
      if(button == 3) // right button
         simulation.getWorld().removeFuelAt(mouseCoordsToGridCoords(e));
   }
   
   @Override
   public void mouseReleased(MouseEvent e) {
      buttonDown = 0;
      lastGridPosition = null;
      repaint();
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      /* I can't do what I did in mousePressed, because for some reason the 
       * getButton() method of MouseEvent returns 0 in the mouseDragged method.
       * So instead, I need to keep track of it myself using the mousePressed and
       * mouseReleased methods. buttonDown is an instance variable containing a number
       * corresponding to which mouse button is currently down.
        */
      long currentTime = System.currentTimeMillis();
      Point gridCoords = mouseCoordsToGridCoords(e);
      if(buttonDown == 1) // left button
         simulation.getWorld().addFuelLine(lastGridPosition,
                                           gridCoords);
      if(buttonDown == 3) // right button
         simulation.getWorld().removeFuelLine(lastGridPosition,
                                              gridCoords);

      lastGridPosition = gridCoords;
      if(currentTime - lastDragRepaintTime >= 50) {
         lastDragRepaintTime = currentTime;
         repaint();
      }
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
}
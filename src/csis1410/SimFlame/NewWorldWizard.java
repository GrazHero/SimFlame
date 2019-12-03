/**
 * Name:       Tim Hansen, Jacob Winters, Adrianna Jones, Mortada Shogar
 * Assignment: SimFlame (Team Assignment)
 * File:       NewWorldWizard.java
 * Date:       2019-12-02
 */
package csis1410.SimFlame;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

/**
 * Window where the user inputs the parameters to construct a new world with
 * 
 * @author Tim Hansen
 */
public class NewWorldWizard extends JFrame {
   
   private int newWidth = 1;
   private int newHeight = 1;
   private int newPixelSize = 1;
   
   public NewWorldWizard() {
      setPreferredSize(new Dimension(160, 180));
      setSize(new Dimension(160, 180));
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setResizable(true);
      setTitle("New World");
      
      JSpinner spinnerWidth = new JSpinner();
      spinnerWidth.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            newWidth = (int)(spinnerWidth.getValue());
         }
      });
      spinnerWidth.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      
      JLabel lblPixelSize = new JLabel("Pixel Size");
      
      JLabel lblHeight = new JLabel("Height");
      
      JLabel lblWidth = new JLabel("Width");
      
      JButton btnCreate = new JButton("Create");
      btnCreate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            World newWorld = new World(newWidth, newHeight, newPixelSize);
            Simulation newSimulation = new Simulation(newWorld);
            Window newWindow = new Window(newSimulation);
            newWindow.setVisible(true);
            dispose();
         }
      });
      
      JSpinner spinnerHeight = new JSpinner();
      spinnerHeight.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            newHeight = (int)(spinnerHeight.getValue());
         }
      });
      spinnerHeight.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      
      JSpinner spinnerPixelSize = new JSpinner();
      spinnerPixelSize.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            newPixelSize = (int)(spinnerPixelSize.getValue());
         }
      });
      spinnerPixelSize.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      
      GroupLayout groupLayout = new GroupLayout(getContentPane());
      groupLayout.setHorizontalGroup(
         groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
               .addContainerGap()
               .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                  .addGroup(groupLayout.createSequentialGroup()
                     .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblPixelSize)
                        .addComponent(lblHeight)
                        .addComponent(lblWidth))
                     .addGap(18)
                     .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(spinnerPixelSize, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinnerHeight, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinnerWidth, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)))
                  .addComponent(btnCreate))
               .addContainerGap(300, Short.MAX_VALUE))
      );
      groupLayout.setVerticalGroup(
         groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
               .addContainerGap()
               .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                  .addComponent(lblWidth)
                  .addComponent(spinnerWidth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
               .addPreferredGap(ComponentPlacement.UNRELATED)
               .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                  .addComponent(lblHeight)
                  .addComponent(spinnerHeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
               .addPreferredGap(ComponentPlacement.UNRELATED)
               .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                  .addComponent(lblPixelSize)
                  .addComponent(spinnerPixelSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
               .addPreferredGap(ComponentPlacement.UNRELATED)
               .addComponent(btnCreate)
               .addContainerGap(143, Short.MAX_VALUE))
      );
      getContentPane().setLayout(groupLayout);
   }
}

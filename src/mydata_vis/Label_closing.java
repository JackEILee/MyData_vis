/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydata_vis;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Jackokie
 */
public class Label_closing extends JLabel implements MouseListener{
    private JLabel label;
    private JLabel label1;
    private  JPanel jp;
    
  

    Label_closing(String s) {
        jp=new JPanel();
        label=new JLabel(s) ; //To change body of generated methods, choose Tools | Templates.
        label1=new JLabel();
        label.setHorizontalAlignment(JLabel.LEFT);
        label1.setHorizontalAlignment(JLabel.CENTER);
    
        GridLayout gl= new GridLayout(1,1,10,0);
        jp.setLayout(gl);
       
        jp.add(label);
        jp.add(label1);
        label1.addMouseListener(this);
        
        
    }
   public JPanel getJPanel()
   {
       return jp;
   }
    public void mouseClicked(MouseEvent e) {
        MenuDemo.workTabbedPane.remove(MenuDemo.workTabbedPane.indexOfTabComponent(jp)); //To change body of generated methods, choose Tools | Templates.
    }

    public void mousePressed(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseReleased(MouseEvent e) {
       //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseEntered(MouseEvent e) {
       label1.setText("x"); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseExited(MouseEvent e) {
       label1.setText("  "); //To change body of generated methods, choose Tools | Templates.
    }
    
}

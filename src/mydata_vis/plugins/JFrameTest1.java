package mydata_vis.plugins;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class JFrameTest1 extends JFrame {

    /**
     * @param args the command line arguments
     */
    JFrameTest1()
    {
    	 getContentPane().setBackground(Color.blue);
        setTitle("右键弹出菜单"); 
        // 设置窗口位置和大小。 
         getContentPane().setBackground(Color.WHITE);
           setBounds(200, 200, 400, 300); 
        // 设置窗口为可视 
        setVisible(true); 
        // 关闭窗口时退出程序 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        //给该窗口添加监听器
        addMouseListener(new MouseAdapter() { // 窗口的鼠标事件处理 
            public void mouseReleased(MouseEvent event) { // 释放鼠标 
                if (event.isPopupTrigger()) // 如果是弹出菜单事件(根据平台不同可能不同) 
                    // 显示菜单 
                   new GlobalPopmenu().popupMenu.show(event.getComponent(), event.getX(), event 
                            .getY()); 
            } 
        }); 
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        new JFrameTest1();
    }
    
}
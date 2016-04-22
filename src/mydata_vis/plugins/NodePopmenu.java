/*
 author :jianqin
 该类为右击点时触发关于点的局部菜单 
 * 
 */





package mydata_vis.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.gephi.datalab.api.GraphElementsController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.openide.util.Lookup;

public class NodePopmenu {
	//声明弹出菜单以及其子菜单项
	public JPopupMenu popupMenu;
	private JMenuItem[]  items;
	
	
	  NodePopmenu()
	{

		popupMenu = new JPopupMenu(); // 实例化弹出菜单 
        String[] str = { "删除", "Node2", "Node3", "Node4", "Node5" }; // 菜单项名称 
        items = new JMenuItem[5]; // 创建5个菜单项 
        
        MenuItemMonitor menuItemMonitor = new MenuItemMonitor(); //初始化一个菜单项的监听器
        //对弹出来的菜单中的每一个菜单项实例化并且添加监听器
        for (int i = 0; i < items.length; i++) { 
            items[i] = new JMenuItem(str[i]); // 实例化菜单项 
            popupMenu.add(items[i]); // 将菜单项添加到菜单中 
            // 设置ActionCommand，方便我们获取下标 ，当该菜单项被触发时，可以通过getActionCommand()获取对应下标
            items[i].setActionCommand(i + ""); 
            // 为各菜单项设置监听 
            items[i].addActionListener(menuItemMonitor); 
        } 
	}
	 //弹出来的
	  private class MenuItemMonitor implements ActionListener { 
		  //弹出来的全局选项卡的每一个选项的触发相应都在该函数中执行
	        @Override 
	        public void actionPerformed(ActionEvent event) { 
	            // 获取String格式的ActionCommand 
	        	System.out.println("");
	            String strIndex = ((JMenuItem) event.getSource()) .getActionCommand(); 
	            // 将上面取到的String格式的内容变为int类型作为取颜色的下标 
	            int niIndex = Integer.parseInt(strIndex); 
	            // 设置背景色为对应下标的颜色 
	            //根据菜单项中第几个菜单被触发，响应对应的事件
	            if(niIndex==0)
	            {
	            	//删除点的操作                 
                    DirectedGraph graph2=Lookup.getDefault().lookup(GraphController.class).getGraphModel().getDirectedGraph();
                    GraphElementsController gec=Lookup.getDefault().lookup(GraphElementsController.class);    
                    JOptionPane.showConfirmDialog(null, "Node will be deleted,Continue? ","Delete Node ID "+MouseListenerTemplate.nodeclicked.getId(), JOptionPane.YES_OPTION); 
                            
                             graph2.readUnlockAll();                                            //这行代码很关键，在写图数据之前必须解除对图数据的读同步锁             
                             gec.deleteNode(MouseListenerTemplate.nodeclicked);             
                             return;
                        
                     } 	            	
	            }
	           
	        } 
	   
}

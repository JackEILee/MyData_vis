/*
 author:jianqin
 该类为点击图片空白处弹出来的全局菜单
 
 */




package mydata_vis.plugins;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.gephi.datalab.api.GraphElementsController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.openide.util.Lookup;

public class GlobalPopmenu {
	//声明弹出菜单以及其子菜单项
	public JPopupMenu popupMenu;
	private JMenuItem[]  items;
	Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, 
            Color.PINK }; // 背景颜色集 
	
	  GlobalPopmenu()
	{
      
		popupMenu = new JPopupMenu(); // 实例化弹出菜单 
        String[] str = { "region choose", "recovery ego", "blue", "yellow", "pink" }; // 菜单项名称 
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
	            String strIndex = ((JMenuItem) event.getSource()).getActionCommand(); 
	            // 将上面取到的String格式的内容变为int类型作为取颜色的下标 
	            int niIndex = Integer.parseInt(strIndex); 
	            // 设置背景色为对应下标的颜色 
	             if(niIndex==0)
	             {

	            	 MouseListenerTemplate.region=true;
	             }
	             //执行ego点的恢复
	             else if(niIndex==1 && NodePopmenu.egoNodes.size()!=0)
	             {
	            	 System.out.println("egoNodes中的点的标签："+NodePopmenu.egoNodes.getFirst().getId());
	            	 DirectedGraph graph2=Lookup.getDefault().lookup(GraphController.class).getGraphModel().getDirectedGraph();
	            	 GraphElementsController gec=Lookup.getDefault().lookup(GraphElementsController.class);
//                     gec.createNode("新加入的", NodePopmenu.egoNodes.pollFirst().getId().toString(), graph2);
	            	 //恢复ego点
	            	 while(NodePopmenu.egoNodes.size()!=0)
	            	 {
	            	    graph2.addNode(NodePopmenu.egoNodes.pollFirst());
	            	 }
	            	 
	            	 PreviewSketch1.target.refresh();
                     PreviewSketch1.refreshLoop.refreshSketch();
	             }
	           
	           
	        } 
	    } 
}

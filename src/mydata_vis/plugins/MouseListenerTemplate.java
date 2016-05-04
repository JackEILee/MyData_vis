/*
Copyright 2008-2014 Gephi
Authors : Eduardo Ramos <eduardo.ramos@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package mydata_vis.plugins;


import java.awt.Color;
import static java.awt.Color.yellow;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterable;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewMouseEvent;
import static org.gephi.preview.api.PreviewMouseEvent.Button.LEFT;
import static org.gephi.preview.api.PreviewMouseEvent.Button.RIGHT;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;


import java.awt.Color;
import javax.swing.JOptionPane;
import org.gephi.datalab.api.GraphElementsController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewMouseEvent.Button;
import static org.gephi.preview.api.PreviewMouseEvent.Button.RIGHT;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PreviewMouseListener.class)
public class MouseListenerTemplate implements PreviewMouseListener {

  //  @Override
    float start_x;
    float  start_y;
    //记录框选的范围
    float region_x1;
    float region_y1;
    float region_x2;
    float region_y2;
    
    public static boolean region=false;//用来表示是否框选
    public static boolean innode;//用来指代是否击中点
    public static boolean inedge;//用来指代是否击中线
    public static Node nodeclicked;//定义该静态变量，用来指代被点中的点
    public static Edge edgeclicked;//定义该静态变量，用来指代被点中的线
    
    PreviewController  previewController;
    private PreviewMouseEvent.Button Left;            //左按键事件触发值
    private PreviewMouseEvent.Button Right;          // 右按键触发事件
     static ArrayList co=new ArrayList();
    private static  Boolean setHighlight=false;//初始时没有任何点被点亮
     static NodeIterable qq;
       GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
       
	private float dist;//点及点的距离
       
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
    
         Graph graph=Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph();
         
       
     //左键高亮   
        for (Node node : graph.getNodes()) {
           
            if (clickingInNode(node, event)) {
              
//                  System.out.println(setHighlight);
                  
              //如果点击左键
               if(event.button==Button.LEFT)
               {
            	   //如果设置高亮为假，将其高亮
                    if(setHighlight==false)
		                    {
		                     NodeIterable pp=graph.getNeighbors(node);
		                           qq=graph.getNeighbors(node);
		                     Iterator<Node> cc=pp.iterator();
		                     //对点进行迭代，并将对应点高亮
		                     while (cc.hasNext() ) 
		                     {
		        	           Node n=cc.next();
		                
		                       System.out.println(n.getColor());
		                       co.add(n.getColor());   //将得到的颜色信息存储到集合中
		        	           n.setColor(yellow);       //将需要高亮的点设置为黄色 
		                       setHighlight=true;
//		                       System.out.println(n);                   
		                      }
//		                       System.out.println(qq); 
		                   }   
                    //如果高亮为真，恢复原来的数据
	               else if(setHighlight==true){
	                      Iterator<Node> cc2=qq.iterator();
	                        int i=0;
//	                        System.out.println(i);
//	                           System.out.println(qq);   
//	                           System.out.println(cc2);
	                           
	                       //将点恢复到原来的颜色    
			                while (cc2.hasNext()) {
			        	         Node nn=cc2.next();
			        	         nn.setColor((Color) co.get(i));   
			                       i++;
			                    
			                     }
	                        co.clear();//清空集合中的数据
	              
	                        NodeIterable pp2=graph.getNeighbors(node);
	                        qq=graph.getNeighbors(node);
	                        Iterator<Node> cc3=pp2.iterator();
			                 while (cc3.hasNext() ) {
				        	        Node n=cc3.next();                                    
				                        co.add(n.getColor());
				        	        n.setColor(yellow);                                                                      
			                   }                         
	                   }
                    //如果点击左键，此处将Consumed置为false;
                    event.setConsumed(false);
               } 
              
              

               
            }
        }
        
    //如果框选，记录框的起始点
        if(region)
        {
        	region_x1=event.x;
        	region_y1=event.y;
        	
        }
      
        
    }
    public void  recoveryEgo()
    {
    	
    	
    }

    @Override
    public void mousePressed(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
            innode=false;inedge=false;
      //找出点击的点
         for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
         {
             if(clickingInNode(node,event))
             {
                start_x= node.x();//记录下被点击点的位置
                start_y= node.y(); 
                innode=true;
                nodeclicked=node;
                break;
             }
         }
      //找出点击的线
         for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getEdges())
         {
              if (clickingInEdge(edge,event))
              {
            	  inedge=true;
            	  edgeclicked=edge;
            	  break;
            	  
              }
         }
         
         //如果框选，记录框的起始点
        
         	region_x1=event.x;
         	region_y1=-event.y;
         	System.out.println("框选的起始点:"+"("+region_x1+"，"+region_y1+")");
         	
                System.out.println("pressed");
                event.setConsumed(true);
                
    }

    @Override
    public void mouseDragged(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) 
    {
        /*
         System.out.println("dragged开始");
         for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
         {
             boolean temp=dragNode(node);
             System.out.println(temp);
             if (temp)
             {
               node.setPosition(event.x, event.y);
              
              System.out.println("拖拽进入");
               return; 
             }
         }
          event.setConsumed(true);
     
        */
    	System.out.println("MouseTemplate的拖拽进入");
    	event.setConsumed(true);
    }

    @Override
    public void mouseReleased(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
            System.out.println("释放开始");
      
        	//计算释放的时候鼠标事件的坐标与点的坐标的距离，如果过小，则不会重置点的坐标
        if(nodeclicked!=null)
        	  dist = (nodeclicked.x() - event.x)*(nodeclicked.x() - event.x)+(-nodeclicked.y() - event.y)*(-nodeclicked.y() - event.y);
           //判断移动点的距离是否在点的半径以内
        	if(innode && dist>nodeclicked.size()*nodeclicked.size()  ) 
            {
               
        		nodeclicked.setPosition(event.x, -event.y);
        		innode=false;
   		        nodeclicked=null;
                
                System.out.println("释放进入");
            }
        	
        	//执行框选操作
        	if(region)
        	{
        		for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes())
        		{
        			if(includeInRegion(node,event))
        			{
        				node.setColor(Color.red);
        				System.out.println("迭代到的点的坐标:"+"("+node.x()+","+node.y()+")");
        			}
        		}
        		//框选操作完成后，将region变量恢复
        		region=false;
              
        		
        	}
        
        
    }
    private boolean includeInRegion(Node node,PreviewMouseEvent event)
    {
    	//记录框选的终点的坐标
    	region_x2=event.x;
        region_y2=-event.y;
        //将所框选的区域颜色改变
      //  event.getClass()event.toString()
        
        
        
        System.out.println("框选的终点:"+"("+region_x2+","+region_y2+")");
    	return   node.x()<=region_x2 && node.x()>=region_x1 && node.y()<=region_y1 && node.y()>=region_y2;
    	
    }
    
     //判断是否击中线
    private boolean clickingInEdge (Edge edge, PreviewMouseEvent event) 
    {
                  float x1=edge.getSource().x();
                  float y1=edge.getSource().y();
                  float x2=edge.getTarget().x();
                  float y2=edge.getTarget().y();
                  float d;
                  
          //计算点击的位置与线的距离
              if(x1==x2)
              {
            	  d=Math.abs(event.x-x1);
              }
              else if(y1==y2)
              {
            	  d=Math.abs(-y1-event.y);
              }
              else
              {
            	  float k=(y2-y1)/(x2-x1);
            	  float b=y1-(y2-y1)*x1/(x2-x1);
            	  d=(float) (Math.abs(k*event.x+event.y+b)/Math.sqrt(k*k+1));
              }
              
             
               return d<20;   //如果该点离线的距离小于20，则表示击中了该线
    	
    }
    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
        float xdiff = node.x() - event.x;
        float ydiff = -node.y() - event.y;//Note that y axis is inverse for node coordinates
        float radius = node.size();
        
        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }
}

/*
 Copyright 2008-2010 Gephi
 Authors : Jérémy Subtil <jeremy.subtil@gephi.org>, Mathieu Bastian
 Website : http://www.gephi.org

 This file is part of Gephi.

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2011 Gephi Consortium. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package mydata_vis.plugins;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.gephi.preview.api.G2DTarget;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.Vector;
import org.openide.util.Lookup;
//import org.gephi.desktop.preview.PreviewTopComponent;

/**
 *
 * @author mbastian
 */
public class PreviewSketch1 extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private static final int WHEEL_TIMER = 500;
    //Data
    private final PreviewController previewController;
    public   static G2DTarget target;//改为静态，便于在外部更新
    //Geometry
    private final Vector ref = new Vector();
    private final Vector lastMove = new Vector();
    //Utils
    public static  RefreshLoop refreshLoop;//改为静态，便于在外部更新
    private Timer wheelTimer;
    private boolean inited=false;
    private final boolean isRetina;

    public PreviewSketch1(G2DTarget target) {
        this.target = target;
        refreshLoop = new RefreshLoop();
        previewController = Lookup.getDefault().lookup(PreviewController.class);
        isRetina =false;// PreviewTopComponent.isRetina();
    }
    
    
    @Override
	public void paint(Graphics g) {
    	System.out.println("在刷新画图");
          super.paint(g);

        if (!inited) {
            //Listeners
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            inited = true;
        }

        int width = (int) (getWidth() * (isRetina ? 2.0 : 1.0));
        int height = (int) (getHeight() * (isRetina ? 2.0 : 1.0));
        System.out.println("图像的大小"+"("+width+","+height+")");

        if (target.getWidth() != width || target.getHeight() != height) {
            target.resize(width, height);
        }
        //refreshLoop.refreshSketch();
       target.refresh();
//        repaint();
      g.drawImage(target.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
  
    public void setMoving(boolean moving) {
    	
        target.setMoving(moving);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	
        
        if (previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.CLICKED))) {
           // refreshLoop.refreshSketch();
           
           }
        int count =e.getClickCount();
        if(count==2)
        {
           // JOptionPane.showConfirmDialog(null, "Delelte ", "double clicked this node?", JOptionPane.YES_NO_OPTION); 
        }
       
    }

    @Override
    public void mousePressed(MouseEvent e) {
      previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.PRESSED));
        ref.set(e.getX(), e.getY());       //得到鼠标在屏幕上press的位置
        lastMove.set(target.getTranslate());

//        refreshLoop.refreshSketch();
//	   target.refresh();
//       updateU();
        repaint();
         
    	System.out.println("press完成后的坐标：("+e.getX()+","+e.getY()+")");
    }
    
    
    //在drag中的repaint()即target.refresh()会出现空白，单独的previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.DRAGGED))
    //也会导致空白出现
    @Override
    public void mouseDragged(MouseEvent e) {
    	System.out.println("拖拽进入");
   
	if(!MouseListenerTemplate.innode && !MouseListenerTemplate.inedge &&!MouseListenerTemplate.region ){
     if (previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.DRAGGED)))
		   {
			
	           setMoving(false);                  //与边的显示有关，如果改为true,边就不显示了
	            Vector transl = target.getTranslate();
           transl.set(e.getX(), e.getY());
	       transl.sub(ref);
           transl.mult(isRetina ? 2f : 1f);
           transl.div(target.getScaling()); // ensure const. moving speed whatever the zoom is
           transl.add(lastMove);
//          target.refresh();
          
//           
//           if (wheelTimer != null) {
//               wheelTimer.cancel();
//               wheelTimer = null;
//           }
//           wheelTimer = new Timer();
//           wheelTimer.schedule(new TimerTask() {   //延迟WHEEL_TIMER后，执行run方法
//               @Override
//               public void run() {
//                   setMoving(false);
//                  // refreshLoop.refreshSketch();
//                   repaint();
//                   wheelTimer = null;
//               }
//           }, WHEEL_TIMER);
	      
              repaint();   

    }
        
	      }
  }
    @Override
    public void mouseReleased(MouseEvent e) {
    	 if (!previewController.sendMouseEvent(buildPreviewMouseEvent(e, PreviewMouseEvent.Type.RELEASED))) {
            // setMoving(false);
         }
    	//如果击中的不是一个点，并且是右键，则弹出全局菜单,如果右击的是一个点，则弹出关于点的局部菜单,如果右击的是一条线，则弹出来关于线的局部菜单
    	if (e.isPopupTrigger())
    	{
    		//如果点中点
    		if(MouseListenerTemplate.innode)
    		{
  			
  			    new NodePopmenu().popupMenu.show(e.getComponent(), e.getX(), e.getY());
  			     MouseListenerTemplate.innode=false;
    		}
    		//如果点中线
    		else if(MouseListenerTemplate.inedge)
    		{
    			new EdgePopmenu().popupMenu.show(e.getComponent(), e.getX(), e.getY());
    			 MouseListenerTemplate.inedge=false;
    			
    		}
    		//如果点中空白地方
    		else
    			new GlobalPopmenu().popupMenu.show(e.getComponent(), e.getX(), e.getY());
    	}
                   
    	
       
       
        
          // refreshLoop.refreshSketch(); 
//        target.refresh();
        repaint();
      previewController.refreshPreview();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getUnitsToScroll() == 0) {
            return;
        }
        float way = -e.getUnitsToScroll() / Math.abs(e.getUnitsToScroll());
        target.setScaling(target.getScaling() * (way > 0 ? 2f : 0.5f));//获取滚轮
        setMoving(true);
        if (wheelTimer != null) {
            wheelTimer.cancel();
            wheelTimer = null;
        }
        wheelTimer = new Timer();
        wheelTimer.schedule(new TimerTask() {   //延迟WHEEL_TIMER后，执行run方法
            @Override
            public void run() {
                setMoving(false);
               // refreshLoop.refreshSketch();
                repaint();
                wheelTimer = null;
            }
        }, 0);
       // refreshLoop.refreshSketch();
//        target.refresh();
        repaint();
    }

   

    @Override
    public void mouseMoved(MouseEvent e) {
    	System.out.println("鼠标移动");
    if(MouseListenerTemplate.innode && MouseListenerTemplate.nodeclicked!=null)
    	                    MouseListenerTemplate.nodeclicked.setPosition(e.getX(), e.getY());
    
    	repaint();
    }

    public void zoomPlus() {
        target.setScaling(target.getScaling() * 2f);
        refreshLoop.refreshSketch();
    }

    public void zoomMinus() {
        target.setScaling(target.getScaling() / 2f);
        refreshLoop.refreshSketch();
    }

    public void resetZoom() {
        target.reset();
        refreshLoop.refreshSketch();
    }

     //将在屏幕上得到的位置坐标转换到模型中的位置坐标
    private Vector screenPositionToModelPosition(Vector screenPos) {
        Vector center = new Vector(getWidth() / 2f, getHeight() / 2f);//屏幕的原点，即中心点
        Vector scaledCenter = Vector.mult(center, target.getScaling());
        Vector scaledTrans = Vector.sub(center, scaledCenter);

        Vector modelPos = new Vector(screenPos.x, screenPos.y);
        modelPos.sub(scaledTrans);
        modelPos.div(target.getScaling());
        modelPos.sub(target.getTranslate());
        return modelPos;
    }

    private PreviewMouseEvent buildPreviewMouseEvent(MouseEvent evt, PreviewMouseEvent.Type type) {
        int mouseX = evt.getX();
        int mouseY = evt.getY();
        PreviewMouseEvent.Button button = PreviewMouseEvent.Button.LEFT;
        if (SwingUtilities.isMiddleMouseButton(evt)) {
            button = PreviewMouseEvent.Button.MIDDLE;
        } else if (SwingUtilities.isLeftMouseButton(evt)) {
            button = PreviewMouseEvent.Button.LEFT;
        } else if (SwingUtilities.isRightMouseButton(evt)) {
            button = PreviewMouseEvent.Button.RIGHT;
        }
 
        //将在屏幕上得到的位置坐标转换到模型中的位置坐标
        Vector pos = screenPositionToModelPosition(new Vector(mouseX, mouseY));

        return new PreviewMouseEvent((int) pos.x, (int) pos.y, type, button, null);
    }

    class RefreshLoop {

        private final long DELAY = 100;
        private final AtomicBoolean running = new AtomicBoolean();
        private final AtomicBoolean refresh = new AtomicBoolean();
        //Timer
        private long timeout = DELAY * 10;
        private Timer timer;

        public RefreshLoop() {
           // super();
        }

        public void refreshSketch() {
        	long time1=System.currentTimeMillis();
            refresh.set(true);
            if (!running.getAndSet(true)) {   //如果running值为false,则开始计时器
                startTimer();
            }
            previewController.refreshPreview();
            long time2=System.currentTimeMillis();
//            System.out.println("刷新消耗的时间"+(time2-time1));
        }

        private void startTimer() {
            timer = new Timer("PreviewRefreshLoop", true);  //将timer设置为一个名为PreviewRefreshLoop的新的线程，并且指定其为守护线程
            timer.schedule(new TimerTask() {                //TimerTask中run方法指定需要执行的任务
                @Override
                public void run() {
                    if (refresh.getAndSet(false)) {
//                    	System.out.println("计时器中的刷新");
                        target.refresh();
                        repaint();                        //组件的repaint
                    } else if (timeout == 0) {
//                    	System.out.println("计时器停止");
                        timeout = DELAY * 10;
                        stopTimer();
                    } else {
                        timeout -= DELAY;
//                        System.out.println("计时器减法操作");
                    }
                    
                }
            }, 0, DELAY);                     //0延迟后，开始以时间间隔为DELAY进行TimerTask中的Run方法执行
        }

        private void stopTimer() {
            timer.cancel();
            running.set(false);
        }
    }
}

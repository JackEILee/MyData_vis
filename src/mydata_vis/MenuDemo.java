/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydata_vis;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

//import org.gephi.data.attributes.api.AttributeTable;
//import org.gephi.data.attributes.api.AttributeColumn;
//import org.gephi.data.attributes.api.AttributeModel;
//import org.gephi.data.attributes.api.AttributeOrigin;
//import org.gephi.data.attributes.api.AttributeRow;
//import org.gephi.data.attributes.api.AttributeType;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphModel;

import org.gephi.graph.api.Node;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openide.util.NbBundle;

import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.gephi.preview.api.*;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;



import org.gephi.statistics.plugin.Degree;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;

/*
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
*/
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingLabelSizeTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;

import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.UndirectedGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import org.apache.commons.codec.binary.Base64;
import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.EgoBuilder;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.GraphView;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;



import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;


import org.openide.util.Lookup;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;


import javax.swing.JSlider;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JScrollPane;
import mydata_vis.plugins.PreviewSketch1;
import java.awt.BorderLayout;
import java.util.Iterator;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterable;
import org.gephi.graph.api.Graph;
import org.gephi.layout.api.LayoutController;
import static org.gephi.preview.types.EdgeColor.Mode.ORIGINAL;
 class new_demo {
     
     JPanel script(String fileName,String layoutMethod,String graphIsDirect,
                                            int egoNodeID,Double repulseStrength,Double gravity,Double speed,
                                            Float area,Float optimalDistance,Float stepRatio )    //增加两个参数,是否为有向图
    {
        
//1.创建一个project、一个workspace，这是必做的工作，是进行后续操作的前提：
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        
        Workspace workspace = pc.getCurrentWorkspace();
        
//2.得到该空间的各个模型以及控制器，方便后面使用          
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
        LayoutController lc = Lookup.getDefault().lookup(LayoutController.class);
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        
       AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();
       
 //3.导入数据，使用Container接收，并将数据导入到空间中
        Container container;
        try {
         // File file = new File(getClass().getResource("/org/gephi/toolkit/demos/resources/lesmiserables.gml").toURI());
             File file = new File(fileName);
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.UNDIRECTED);   //Force UNDIRECTED       
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }      
//Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);



//4.通过打印信息，验证图形数据是否导入成功
        DirectedGraph graph = graphModel.getDirectedGraph();
        System.out.println("Nodes: " + graph.getNodeCount());
        System.out.println("Edges: " + graph.getEdgeCount());
        
        
//5.对图形数据进行过滤操作
     
      /*  DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(0, Integer.MAX_VALUE));     //Remove nodes with degree < 30
        Query query = filterController.createQuery(degreeFilter);
        GraphView view = filterController.filter(query);
        graphModel.setVisibleView(view);    //Set the filter result as the visible view
  */
        
   
      
        
//7.布局算法：2）执行 ForceAtlasLayout布局算法:
if(!graphIsDirect.equals("Direct")&&(!graphIsDirect.equals("unDirect")))   //进行数据统计时不进行布局
{
           if(layoutMethod.equals("ForceAtlasLayout"))
           {
                System.out.println("Yes,ForceAtlasLayout");
               ForceAtlasLayout secondLayout = new ForceAtlasLayout(null);
               secondLayout.setGraphModel(graphModel);
               secondLayout.resetPropertiesValues();
        //重新调节布局参数
              secondLayout.setRepulsionStrength(repulseStrength);
              secondLayout.setAdjustSizes(true);
              secondLayout.setGravity(gravity);
              secondLayout.setSpeed(100d);
              secondLayout.setConverged(false);
              secondLayout.setOutboundAttractionDistribution(true);
        //进行迭代
             secondLayout.initAlgo();
             for (int i = 0; i < 2000 && secondLayout.canAlgo(); i++) 
               {
                  secondLayout.goAlgo();
                }
             secondLayout.endAlgo();
             lc.canStop();
           }
           
           else if (layoutMethod.equals("YifanHuLayout"))  //1）YifanHuLayout布局算法
           {
               System.out.println("Yes,YifanHuLayout");
                 YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
                layout.setGraphModel(graphModel);
                 layout.resetPropertiesValues();
                layout.setOptimalDistance(optimalDistance);
                layout.setStepRatio(stepRatio/100);
                layout.initAlgo();
                for (int i = 0; i < 2000 && layout.canAlgo(); i++) 
                {
                      layout.goAlgo();
                }
             layout.endAlgo();
             lc.canStop();
           }
           else if (layoutMethod.equals("FruchtermanReingold")) //3) 执行 FruchtermanReingold算法
           {
               System.out.println("Yes,FruchtermanReingold");
                 FruchtermanReingold   thirdLayout=new FruchtermanReingold(null);
                 thirdLayout.setGraphModel(graphModel);
                 thirdLayout.resetPropertiesValues();
                 thirdLayout.setGravity(gravity);
                 thirdLayout.setSpeed(speed);
                 thirdLayout.setArea(area);
                         
                 thirdLayout.initAlgo();
             for (int i = 0; i < 2000 && thirdLayout.canAlgo(); i++) 
              {
                      thirdLayout.goAlgo();
                }
             thirdLayout.endAlgo();
             lc.canStop();
       }
}
   if(egoNodeID!=-1)
   {
       EgoBuilder.EgoFilter egoFilter = new EgoBuilder.EgoFilter();
       
         Node [] temp=new Node[graph.getNodeCount()];
         int i=0;
         for (Node node : Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph().getNodes()) 
         {
               temp[i++]=node;
         } 
         egoFilter.setPattern(temp[egoNodeID].getLabel()); //Regex accepted
         egoFilter.setDepth(1);
         Query queryEgo = filterController.createQuery(egoFilter);
         GraphView viewEgo = filterController.filter(queryEgo);
         graphModel.setVisibleView(viewEgo);    //Set the filter result as the visible view
   }
       
   
       
  

//8.根据节点度值分配节点颜色：
/*
        Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        AbstractColorTransformer colorTransformer;
        colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT,
                                                                                                  Transformer.RENDERABLE_COLOR);
        
        colorTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000),new Color(0xB3FCFFaa),new Color(0xFCC0c9)});
        rankingController.transform(degreeRanking,colorTransformer);

        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel, attributeModel);

        //Rank size by centrality
        AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        
        Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT,
                                                                                                         Transformer.RENDERABLE_SIZE);
        sizeTransformer.setMinSize(10);
        sizeTransformer.setMaxSize(25);
        rankingController.transform(centralityRanking,sizeTransformer);

        //Rank label size - set a multiplier size
        Ranking centralityRanking2 = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
        AbstractSizeTransformer labelSizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.LABEL_SIZE);
        labelSizeTransformer.setMinSize(1);
        labelSizeTransformer.setMaxSize(2);
        rankingController.transform(centralityRanking2,labelSizeTransformer);
       //New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
        PApplet applet = target.getApplet();
        applet.init();
 
        //Refresh the preview and reset the zoom
        previewController.render(target);
        target.refresh();
        target.resetZoom();
 
        


*/
   /*
        Function degreeRanking = appearanceModel.getNodeFunction(graph, AppearanceModel.GraphFunction.NODE_DEGREE, RankingElementColorTransformer.class);
        RankingElementColorTransformer degreeTransformer = (RankingElementColorTransformer) degreeRanking.getTransformer();
        degreeTransformer.setColors(new Color[]{new Color(0x000000), new Color(0xFF3000)});
        degreeTransformer.setColorPositions(new float[]{0f, 1f});
        appearanceController.transform(degreeRanking);

        //Get Centrality
        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(graphModel);

        //Rank size by centrality
        Column centralityColumn = graphModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
        Function centralityRanking = appearanceModel.getNodeFunction(graph, centralityColumn, RankingNodeSizeTransformer.class);
        RankingNodeSizeTransformer centralityTransformer = (RankingNodeSizeTransformer) centralityRanking.getTransformer();
        centralityTransformer.setMinSize(30);
        centralityTransformer.setMaxSize(100);
        appearanceController.transform(centralityRanking);

        //Rank label size - set a multiplier size
        Function centralityRanking2 = appearanceModel.getNodeFunction(graph, centralityColumn, RankingLabelSizeTransformer.class);
        RankingLabelSizeTransformer labelSizeTransformer = (RankingLabelSizeTransformer) centralityRanking2.getTransformer();
        labelSizeTransformer.setMinSize(1);
        labelSizeTransformer.setMaxSize(3);
        appearanceController.transform(centralityRanking2);
*/
        //Graph graph=Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph();
          //Color[] colory={RED,YELLOW,BLUE,GREEN,PINK,GRAY,BLACK};
        Graph graph2=Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace).getGraph();
          //Color[] colory={RED,YELLOW,BLUE,GREEN,PINK,GRAY,BLACK};
          ArrayList bo=MyColor.ColorRead();
          
           EdgeIterable  edges=graph2.getEdges();
             Iterator<Edge> cco=edges.iterator();
                      int i=0;
                      int j=0;
                      while (cco.hasNext() ) {
                          int a=Integer.parseInt ((String) bo.get(0+j));
                           int b=Integer.parseInt ((String) bo.get(1+j));
                            int c=Integer.parseInt ((String) bo.get(2+j));
        	       Edge n=cco.next();
                      System.out.println(n.getColor());
        	       n.setColor(new Color(a,b,c));  
                       j+=3;
            }     

  //Preview configuration
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 100f);
        
   
//10.预览效果配置，并呈现到Display上，添加到JFrame，进行界面展示(主要是节点、标签、边等参数的设置，但目前觉得透明度很差）
      //  PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
        model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        //model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.BLACK));
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        model.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        model.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 30f);
        model.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 20f);
      //  model.getProperties().putValue(PreviewProperty.NODE_LABEL_OUTLINE_OPACITY,0.0);
        model.getProperties().putValue(PreviewProperty.EDGE_LABEL_OUTLINE_COLOR,new DependantOriginalColor(Color.BLACK) );
        model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 1f);
        model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
        model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(ORIGINAL));

        previewController.refreshPreview();
          

//Add the applet to a JFrame and display
                    
        G2DTarget target = (G2DTarget) previewController.getRenderTarget(RenderTarget.G2D_TARGET);
        PreviewSketch1 previewSketch = new PreviewSketch1(target);
        previewController.refreshPreview();       
        previewSketch.resetZoom();
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        panel.add(previewSketch, BorderLayout.CENTER);
        panel.setVisible(true);
        
        //Add the applet to a JFrame and display                                                 
      return panel;
}
 }


public class MenuDemo extends Frame 
{
        
	private Frame frame;                                         //整体框架
        
       // private Frame statisticFrame;                               
        private JPanel  statisticPanel=new JPanel();                //统计显示窗口
        
        private String layoutMethod=new String("ForceAtlasLayout");   //默认随机布局方式，效果混乱
        
	private MenuBar menubar;                                   //菜单栏，后续进行补充和完善
	private Menu menu;
	private MenuItem menuitem;
	private FileDialog filedialog;
	private String filedir;                                    //文件对话框和文件路径
        
        JPanel statistic=new JPanel();                            //数据统计和布局算法执行的面板
        JPanel excute=new JPanel();
        
        
        JPanel FirLOJp=new JPanel();
        JPanel FirLOJp0=new JPanel();
        JPanel FirLOJp1=new JPanel();
        JPanel FirLOJp2=new JPanel();
        JPanel FirLOJp3=new JPanel();
        
        JPanel SecLOJp=new JPanel();
        JPanel SecLOJp0=new JPanel();
        JPanel SecLOJp1=new JPanel();
        JPanel SecLOJp2=new JPanel();
        JPanel SecLOJp3=new JPanel();
        JPanel SecLOJp4=new JPanel();
        
        JPanel ThiLOJp=new JPanel();
        JPanel ThiLOJp0=new JPanel();
        JPanel ThiLOJp1=new JPanel();
        JPanel ThiLOJp2=new JPanel();
        JPanel ThiLOJp3=new JPanel();
        JPanel ThiLOJp4=new JPanel();
        
        
       
        JLabel label1=new JLabel("斥力强度:");
        JLabel label2=new JLabel("中心引力:");
        JLabel label4=new JLabel("最佳距离:");
        JLabel label5=new JLabel("步比率:");
        JLabel label7=new JLabel("重力:");
        JLabel label8=new JLabel("速度:");
        JLabel label9=new JLabel("面积:");
        
        JSlider  slider1=new JSlider(2000,20000,12000);
        JSlider  slider2=new JSlider(0,300,100);
       
        //private  JButton sure1=new JButton("确定");
        
        JSlider  slider4=new JSlider(0,600,200);
        JSlider  slider5=new JSlider(0,100,80);
        
        //private  JButton sure2=new JButton("确定");
        
        JSlider  slider7=new JSlider(0,50,10);
        JSlider  slider8=new JSlider(0,10,3);
        JSlider  slider9=new JSlider(10000,50000,30000);
        //private  JButton sure3=new JButton("确定");
        
        JScrollBar  scrollbar=new JScrollBar();
        static JTabbedPane workTabbedPane=new JTabbedPane();  
        JSplitPane downSplitPane =new JSplitPane();  
        JSplitPane leftSplitPane =new JSplitPane();//自定义工作区间
        //JScrollPane leftSplitPane =new JScrollPane();
        //leftSplitPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        
        private  JButton ForceAtlas2=new   JButton("ForceAtlas2");
        private  JButton Yifanhu=new JButton("Yifanhu");
        private  JButton FR=new   JButton("FR");  //定义布局算法执行
        
        private  JButton ego=new   JButton("EGO");  
        
        private JButton Direct=new JButton("Direct");
        private JButton unDirect=new JButton("unDirect");         //有向图和无向图数据统计
        private Double gravity;
        private Double repulseStrength;
        private Double speed;
        private Float area;
        private Float optimalDistance;
        private Float stepRatio;

        
        private JPanel temp;
        
public	MenuDemo()
         {      
             gravity = 10d;
                repulseStrength = 12000d;
                speed=3d;
                area=30000f;
                optimalDistance=200f;
                stepRatio=80f;
              slider1.setMajorTickSpacing(5000);
              slider1.setMinorTickSpacing(1000);
              slider1.setPaintTicks(true);
              slider1.setPaintLabels(true);
              slider1.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            gravity = (double)slider2.getValue();
                            repulseStrength = (double)slider1.getValue();
                            layoutMethod=new String("ForceAtlasLayout");                       
                            temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                            workTabbedPane.add("ForceAtlasLayout",temp);
                            workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("ForceAtlasLayout").getJPanel());
                            
                     }
                }
            });
       //        workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("ForceAtlasLayout").getJPanel());   
              slider2.setMajorTickSpacing(50);
              slider2.setMinorTickSpacing(10);
              slider2.setPaintTicks(true);
              slider2.setPaintLabels(true);
              slider2.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            gravity = (double)slider2.getValue();
                            repulseStrength = (double)slider1.getValue();
                            layoutMethod=new String("ForceAtlasLayout");                       
                            temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                            workTabbedPane.add("ForceAtlasLayout",temp);
                            workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("ForceAtlasLayout").getJPanel());
                     }
                }
            });
                  
              slider4.setMajorTickSpacing(100);
              slider4.setMinorTickSpacing(20);
              slider4.setPaintTicks(true);
              slider4.setPaintLabels(true);
              slider4.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            optimalDistance=(float)slider4.getValue();
                            stepRatio=(float)slider5.getValue();
                            layoutMethod=new String("YifanHuLayout");
                            temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                            workTabbedPane.add("YifanHuLayout",temp);
                            workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("YifanHuLayout").getJPanel());
                     }
                }
            });
                  
              slider5.setMajorTickSpacing(20);
              slider5.setMinorTickSpacing(5);
              slider5.setPaintTicks(true);
              slider5.setPaintLabels(true);
              slider5.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            optimalDistance=(float)slider4.getValue();
                            stepRatio=(float)slider5.getValue();
                            layoutMethod=new String("YifanHuLayout");
                            temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                            workTabbedPane.add("YifanHuLayout",temp);
                             workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("YifanHuLayout").getJPanel());
                     }
                }
            });
                 
              slider7.setMajorTickSpacing(10);
              slider7.setMinorTickSpacing(2);
              slider7.setPaintTicks(true);
              slider7.setPaintLabels(true);
              slider7.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            gravity = (double)slider7.getValue();
                             speed=(double)slider8.getValue();
                             area=(float)slider9.getValue();
                             layoutMethod=new String("FruchtermanReingold");                       
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                             workTabbedPane.add("FruchtermanReingold",temp);
                             workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("YifanHuLayout").getJPanel());
                     }
                }
            });
                  
              slider8.setMajorTickSpacing(2);
              slider8.setMinorTickSpacing(1);
              slider8.setPaintTicks(true);
              slider8.setPaintLabels(true);
              slider8.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            gravity = (double)slider7.getValue();
                             speed=(double)slider8.getValue();
                             area=(float)slider9.getValue();
                             layoutMethod=new String("FruchtermanReingold");                       
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                             workTabbedPane.add("FruchtermanReingold",temp);
                             workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("FruchtermanReingold").getJPanel());
                     }
                }
            });
                  
              slider9.setMajorTickSpacing(5000);
              //slider9.setMinorTickSpacing(1000);
              slider9.setPaintTicks(true);
              slider9.setPaintLabels(true);
              slider9.addChangeListener(new ChangeListener(){
                    public void stateChanged(ChangeEvent event){
                       JSlider source=(JSlider) event.getSource();
                     if (source.getValueIsAdjusting() != true){
                            gravity = (double)slider7.getValue();
                             speed=(double)slider8.getValue();
                             area=(float)slider9.getValue();
                             layoutMethod=new String("FruchtermanReingold");                       
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                             workTabbedPane.add("FruchtermanReingold",temp);
                             workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("FR").getJPanel());
                     }
                }
            });
                
                frameInit();
	}
	private void frameInit()
	{
		frame = new Frame("Data Visualization Ver0.1");
                frame.setBounds(200,200,600,500);
		frame.setLayout(new BorderLayout());
		menubar = new MenuBar();
		menu = new Menu("File");
		menuitem = new MenuItem("fileload");
		filedialog = new FileDialog(frame,"fileload",FileDialog.LOAD);
		frame.setMenuBar(menubar);
                menu.add(menuitem);
		menubar.add(menu);
                
               
                
                Menu editMenu=new Menu("Edit");  
                menubar.add(editMenu);  
                Menu viewMenu=new Menu("WorkSpace");  
                menubar.add(viewMenu);  
   
                Menu sourceMenu=new Menu("Tools");  
                menubar.add(sourceMenu);  
                Menu refactorMenu=new Menu("Window");  
                menubar.add(refactorMenu);   
                Menu helpMenu=new Menu("Help");  
                menubar.add(helpMenu); 
       		                                   
		myEvent();
                buttonEvent();
                //SliderEvent();
                setLayout();
		frame.setVisible(true);               
        }
	 private void buttonEvent()
         {
            
             
             unDirect.addActionListener(new ActionListener()
                     {    
                         public void actionPerformed(ActionEvent e)
                         {                  
                             statisticPanel=new new_demo().script(filedir,layoutMethod,"unDirect",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图                                                      
                             statistic.add("unDirect",statisticPanel);          //将统计信息面板返回
                             
                         }
                     });
             
             Direct.addActionListener(new ActionListener()
                     {    
                        public void actionPerformed(ActionEvent e)
                         {                  
                             statisticPanel=new new_demo().script(filedir,layoutMethod,"Direct",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图                           
                             statistic.add("Direct",statisticPanel);          //将统计信息面板返回
                            
                         }
                     });
            
             
             //为布局算法设置监听事件
//              sure1.addActionListener(new ActionListener()
//                     {
//                         public void actionPerformed(ActionEvent e)
//                         {
//                            gravity = (double)slider2.getValue();
//                            repulseStrength = (double)slider1.getValue();
//                             layoutMethod=new String("ForceAtlasLayout");                       
//                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
//                             workTabbedPane.add("ForceAtlasLayout",temp);
//                         }
//                     });
//               sure2.addActionListener(new ActionListener()
//                     {
//                         public void actionPerformed(ActionEvent e)
//                         {
//                             optimalDistance=(float)slider4.getValue();
//                             stepRatio=(float)slider5.getValue();
//                             layoutMethod=new String("YifanHuLayout");
//                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
//                             workTabbedPane.add("YifanHuLayout",temp);
//                         }
//                     });
//             sure3.addActionListener(new ActionListener()
//                     {
//                         public void actionPerformed(ActionEvent e)
//                         {
//                             gravity = (double)slider7.getValue();
//                             speed=(double)slider8.getValue();
//                             area=(float)slider9.getValue();
//                             layoutMethod=new String("FruchtermanReingold");                       
//                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
//                             workTabbedPane.add("FruchtermanReingold",temp);
//                         }
//                     });
             ForceAtlas2.addActionListener(new ActionListener()
                     {
                         public void actionPerformed(ActionEvent e)
                         {
                             layoutMethod=new String("ForceAtlasLayout");                       
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                             workTabbedPane.add("ForceAtlasLayout",temp);
                              workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("ForceAtlas2").getJPanel());
                         }
                     });
            
              Yifanhu.addActionListener(new ActionListener()
                     {
                         public void actionPerformed(ActionEvent e)
                         {
                             layoutMethod=new String("YifanHuLayout");
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                             workTabbedPane.add("YifanHuLayout",temp);
                               workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("Yifanhu").getJPanel());
                         }
                     }
              );
            

               FR.addActionListener(new ActionListener()
                     {
                         public void actionPerformed(ActionEvent e)
                         {
                             layoutMethod=new String("FruchtermanReingold");
                             temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
                              workTabbedPane.add("FruchtermanReingold",temp);
                               workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("FR").getJPanel());
                         }
                     });
              
                ego.addActionListener(new ActionListener()
                     {
                         public void actionPerformed(ActionEvent e)
                         {       
                             
                            String s=JOptionPane.showInputDialog("Enter the ID of the ego point.");                                                 
                            JOptionPane.showMessageDialog(null,"You want to watch"+s ,"number",JOptionPane.INFORMATION_MESSAGE);
                             temp=new new_demo().script(filedir,layoutMethod,"unDirect", Integer.parseInt(s),repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图                                                      
                             workTabbedPane.add("EGO",temp);        //将统计信息面板返回                             
                         }
                     });
               
         }
         /*
         private void SliderEvent(){
            ChangeListener listener;
            listener=new ChangeListener(){
                public void stateChanged(ChangeEvent event){
                     slider1=(JSlider) event.getSource();
                     //获得滑动条当前值。。。
                }
            };
            
         }
         */
//          private void SliderEvent(){
//                ChangeListener listener;
//                listener=new ChangeListener(){
//                    public void stateChanged(ChangeEvent event){
//                       JSlider source=(JSlider) event.getSource();
//                     //获得滑动条当前值。。。
//                     if (source.getValueIsAdjusting() != true){
//                            gravity = (double)slider2.getValue();
//                            repulseStrength = (double)slider1.getValue();
//                            layoutMethod=new String("ForceAtlasLayout");                       
//                            temp=new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);  //调用布局算法返回生成的布局图
//                            workTabbedPane.add("ForceAtlasLayout",temp);
//                     }
//                }
//            };
//          }
         
	private void myEvent()
	{
		menuitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				filedialog.setVisible(true);
				filedir = filedialog.getDirectory()+filedialog.getFile();
				System.out.println(filedir);
                                
                                JPanel temp= new new_demo().script(filedir,layoutMethod,"unKnown",-1,repulseStrength,gravity,speed,area,optimalDistance,stepRatio);                                                                                  
                                workTabbedPane.add("Default",temp);
                                 workTabbedPane.setTabComponentAt( workTabbedPane.indexOfComponent(temp),new Label_closing("Default").getJPanel());
			}
		});
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

	}
	public String getPath()
	{
		return filedir;
	}
	

	public static void main(String[] args) 
	{
		MenuDemo menuTest=new MenuDemo();
                
	}
        public void setLayout()
        { 
         JPanel centerPanel=new JPanel();   
         centerPanel.setLayout(new java.awt.GridLayout());  
        
       
        leftSplitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));  
        leftSplitPane.setResizeWeight(0.1);  
        
        JPanel leftPanel=new JPanel();  
        leftPanel.setLayout(new java.awt.GridLayout());  
        JTabbedPane leftTabbedPane=new JTabbedPane(); 
        
        FirLOJp0.setLayout(new BorderLayout());
        FirLOJp0.add(ForceAtlas2,BorderLayout.WEST);
        FirLOJp1.setLayout(new BorderLayout());
        FirLOJp1.add(label1,BorderLayout.WEST);
        FirLOJp1.add(slider1,BorderLayout.CENTER);
        FirLOJp2.setLayout(new BorderLayout());
        FirLOJp2.add(label2,BorderLayout.WEST);
        FirLOJp2.add(slider2,BorderLayout.CENTER);
        FirLOJp3.setLayout(new BorderLayout());
        //FirLOJp3.add(sure1,BorderLayout.WEST);
        
        FirLOJp.setLayout(new BoxLayout(FirLOJp,BoxLayout.Y_AXIS));
        FirLOJp.add(FirLOJp0);
        FirLOJp.add(FirLOJp1);
        FirLOJp.add(FirLOJp2);
        FirLOJp.add(FirLOJp3);
        
        
        SecLOJp0.setLayout(new BorderLayout());
        SecLOJp0.add(Yifanhu,BorderLayout.WEST);
        SecLOJp1.setLayout(new BorderLayout());
        SecLOJp1.add(label4,BorderLayout.WEST);
        SecLOJp1.add(slider4,BorderLayout.CENTER);
        SecLOJp2.setLayout(new BorderLayout());
        SecLOJp2.add(label5,BorderLayout.WEST);
        SecLOJp2.add(slider5,BorderLayout.CENTER);
        SecLOJp3.setLayout(new BorderLayout());
        SecLOJp4.setLayout(new BorderLayout());
        //SecLOJp4.add(sure2,BorderLayout.WEST);
        
        SecLOJp.setLayout(new BoxLayout(SecLOJp,BoxLayout.Y_AXIS));
        SecLOJp.add(SecLOJp0);
        SecLOJp.add(SecLOJp1);
        SecLOJp.add(SecLOJp2);
        SecLOJp.add(SecLOJp3);
        SecLOJp.add(SecLOJp4);
        
        
        ThiLOJp0.setLayout(new BorderLayout());
        ThiLOJp0.add(FR,BorderLayout.WEST);
        ThiLOJp1.setLayout(new BorderLayout());
        ThiLOJp1.add(label7,BorderLayout.WEST);
        ThiLOJp1.add(slider7,BorderLayout.CENTER);
        ThiLOJp2.setLayout(new BorderLayout());
        ThiLOJp2.add(label8,BorderLayout.WEST);
        ThiLOJp2.add(slider8,BorderLayout.CENTER);
        ThiLOJp3.setLayout(new BorderLayout());
        ThiLOJp3.add(label9,BorderLayout.WEST);
        ThiLOJp3.add(slider9,BorderLayout.CENTER);
        ThiLOJp4.setLayout(new BorderLayout());
        //ThiLOJp4.add(sure3,BorderLayout.WEST);
        
        ThiLOJp.setLayout(new BoxLayout(ThiLOJp,BoxLayout.Y_AXIS));
        ThiLOJp.add(ThiLOJp0);
        ThiLOJp.add(ThiLOJp1);
        ThiLOJp.add(ThiLOJp2);
        ThiLOJp.add(ThiLOJp3);
        ThiLOJp.add(ThiLOJp4);
        
      
        excute.setLayout(new BoxLayout(excute,BoxLayout.Y_AXIS));
        excute.add(FirLOJp);
        excute.add(SecLOJp);
        excute.add(ThiLOJp);
        //excute.add(ego);
        /*
        statistic.setLayout(new BoxLayout(statistic,BoxLayout.Y_AXIS));
        statistic.add(Direct);
        statistic.add(unDirect);  
        */
        leftTabbedPane.add("Layout",excute);
        //leftTabbedPane.add("Anlynase",statistic);
        //leftTabbedPane.add(scrollbar);
      
        leftPanel.add(leftTabbedPane);  
        leftSplitPane.setLeftComponent(leftPanel);
        
        JPanel rightPanel=new JPanel();  
        rightPanel.setLayout(new java.awt.GridLayout());  
        
        downSplitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));  
        downSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);  
        downSplitPane.setResizeWeight(0.85);  
        downSplitPane.setTopComponent(workTabbedPane);
        
        JTabbedPane downTabbedPane=new JTabbedPane();  
        downTabbedPane.add("TimeLine Display", new JPanel());  
        downSplitPane.setBottomComponent(downTabbedPane);  
        rightPanel.add(downSplitPane);  
        leftSplitPane.setRightComponent(rightPanel);  
        
        centerPanel.add(leftSplitPane); 
        frame.add(centerPanel, java.awt.BorderLayout.CENTER);  
        
        //底部状态栏  
        JPanel bottomPanel=new JPanel();  
        bottomPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));  
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));  
        bottomPanel.add(new JLabel("Data Visualization Ver0.1"));  
        bottomPanel.add(new JProgressBar());  
        bottomPanel.add(new JLabel("INS"));  
        frame.add(bottomPanel, java.awt.BorderLayout.PAGE_END);  
        }
}



         
        
        
         
       
       
package mydata_vis;

import java.util.ArrayList;
import org.openide.util.Exceptions;

public class MyColor {
	 static String filenametopo="C:\\Users\\lgw\\Desktop\\颜色测试.csv";
	 public static ArrayList ColorRead()  {
		// TODO Auto-generated method stub
		duwenjian dw = null;
             try {
                 dw = new duwenjian(filenametopo);
             } catch (Exception ex) {
                 Exceptions.printStackTrace(ex);
             }
		 ArrayList br = new ArrayList();
		 /*ͳ��CSV��*/
		  while(true){
		   String str = null;
                    try {
                        str = dw.readLine();
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
		   ArrayList ar=dw.fromCSVLinetoArray(str);
		   if(str==null){
		   break;}
		   System.out.println(ar);
		   System.out.println(ar.get(1));
		   
		   br.add(ar.get(1));
		   
		   br.add(ar.get(2));
		   br.add(ar.get(3));
		   
		  }
		  return br;

	}

}

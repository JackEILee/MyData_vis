package mydata_vis;

import java.util.ArrayList;
import org.openide.util.Exceptions;

public class MyColor {
	
	 public static ArrayList ColorRead(String filename) {
		// TODO Auto-generated method stub
                 String filenametopo=filename;
                 ReadFile dw = null;
             try {
                 dw = new ReadFile(filenametopo);
             } catch (Exception ex) {
                 Exceptions.printStackTrace(ex);
             }
		 ArrayList br = new ArrayList();
		
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

package listener;


import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import util.LuceneUtil;

	public class MyContextListener implements ServletContextListener {
		private ServletContext context = null;  
		 public void contextDestroyed(ServletContextEvent event){  
		  //Output a simple message to the server's console   
		  System.out.println("The Simple Web App. Has Been Removed");  
		  this.context = null;  
		}  
		 // 这个方法在Web应用服务做好接受请求的时候被调用。   
		public void contextInitialized(ServletContextEvent event){  
		  this.context = event.getServletContext();  
		  try {
			new LuceneUtil("").createIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}  
		
}

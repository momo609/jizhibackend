package test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

	public class MyContextListener implements ServletContextListener {  
		private ServletContext context = null;  
		  
		
		 // 这个方法在Web应用服务做好接受请求的时候被调用。   
		public void contextInitialized(ServletContextEvent event){  
		  this.context = event.getServletContext();  
		  
		}


		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			// TODO Auto-generated method stub
			
		}  
		
}

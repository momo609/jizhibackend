package test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

	public class MyContextListener implements ServletContextListener {  
		private ServletContext context = null;  
		  
		
		 // ���������WebӦ�÷������ý��������ʱ�򱻵��á�   
		public void contextInitialized(ServletContextEvent event){  
		  this.context = event.getServletContext();  
		  
		}


		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			// TODO Auto-generated method stub
			
		}  
		
}

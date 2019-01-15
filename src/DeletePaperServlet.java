import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class DeletePaperServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String paperid=request.getParameter("paperid");
		PaperDaoImpl daoImpl=new PaperDaoImpl();
	   if(daoImpl.deletePaper(Integer.parseInt(paperid))!=0)
	   {
		   out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	   }else
	   {
		   out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
	   }
			
//		}
		out.close();
	}
	

	

}

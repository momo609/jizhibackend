import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class ChangePasswordServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		String oldpw=request.getParameter("oldpw");
		String newpw=request.getParameter("newpw");
	
		if((user==null))
			user = dao1.findUser(userid);
		if(user!=null)
		{
		   UserDaoImpl dao=new UserDaoImpl();
		   if(user.getPassword().equals(oldpw))
		   {
			   if(dao.changePassword(user, newpw))
			   {
				   out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
			   }
					   
		   }else
		   {
			   out.print("{\"errcode\":106,\"errmsg\":\"æ…√‹¬Î≤ª’˝»∑\"}");
		   }
			
		}
		out.close();
	}

}

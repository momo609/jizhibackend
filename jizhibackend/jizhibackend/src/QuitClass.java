import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class QuitClass extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
      
		
		PrintWriter out = response.getWriter();
		int classid=Integer.parseInt(request.getParameter("classid"));
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		User user=(User) request.getSession().getAttribute("user");
		UserDaoImpl dao1 = new UserDaoImpl();
		if((user==null))
			user = dao1.findUser(userid);
		if(user!=null)
		{
			MyClassDaoImpl dao=new MyClassDaoImpl();
			if(dao.findClass(classid).getOwner()!=userid){
				if(dao.removeClassMember(classid, user.getUserid()))
				{
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");	
				}else
				{
					out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");	
				}
			}else{
				out.print("{\"errcode\":102,\"errmsg\":\"无法退出\"}");	
			}
			
		}else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");	
		}
		out.flush();
		out.close();
	}

}

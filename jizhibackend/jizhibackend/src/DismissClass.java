import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class DismissClass extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		int classid=Integer.parseInt(request.getParameter("classid"));
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		User user=null;
		if((user==null))
			user = dao1.findUser(userid);
		if(user!=null)
		{
			int owner=0;
			MyClassDaoImpl dao=new MyClassDaoImpl();
			MyClass clz=dao.findClass(classid);
			
			
			if(clz!=null)
				owner=clz.getOwner();
			
			if(owner!=user.getUserid())
			{
				out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");		
			}
			else
			{
				if(dao.dismissClass(classid))
				{
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
				}else
				{
					out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
				}
			}
		}else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}
		out.flush();
		out.close();
	}

}

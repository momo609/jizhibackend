import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;


public class ClassPrivilegeManageServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		User user=(User)request.getSession().getAttribute("user");
		PrintWriter out = response.getWriter();
		int classid=Integer.parseInt(request.getParameter("classid"));
		int userid=Integer.parseInt(request.getParameter("userid"));
		String operation=request.getParameter("operation");
		
		MyClassDaoImpl myClassDao=new MyClassDaoImpl();
		MyClass myclass=myClassDao.findClass(classid);
		if(myclass.getOwner()==user.getUserid())
		{
			if(operation.equals("add_manager"))
			{
				if(myClassDao.addClassManager(classid, userid)>0)
					{
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
					}else
					{
						out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
					}
			}else if(operation.equals("del_manager"))
			{
				if(myClassDao.delClassManager(classid, userid)>0)
				{
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
				}else
				{
					out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
				}
			}
		}else
		{
			out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");
		}
		
	}


}

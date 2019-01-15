import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class ChangeNicknameServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		String name=request.getParameter("nickname");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		if((user==null))
			user = dao1.findUser(userid);
		if(user==null)
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}else
		{
		   UserDaoImpl dao=new UserDaoImpl();
		   user.setNickname(name);
		   if(dao.changeNickname(user, name))
			   out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		   else
				out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
			
		}
		out.close();
	}
}

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.ValidateException;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class RegisterServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		PrintWriter out = response.getWriter();
		User user=new User();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		user.setUsertype(Integer.parseInt(request.getParameter("usertype")));
		user.setNickname(request.getParameter("nickname"));
		
		try {
			user.validate();
		} catch (ValidateException e) {
			out.print("{\"errcode\":103,\"errmsg\":\""+e.errmsg+"\"}");
			return;
		}
		UserDaoImpl userimpl=new UserDaoImpl();
		if(userimpl.findUser(user.getUsername())==null)
		{
		 boolean flag=userimpl.registerUser(user);
		 if(flag)
		 {
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
			System.out.println("ע��ɹ�");
		 }
		 else
		 {
			 out.print("{\"errcode\":104,\"errmsg\":\"ϵͳ����\"}");
			System.out.println("ע��ʧ��");
		 }
		}
		else
		{
			out.print("{\"success\":false,\"errmsg\":\"�û����ѱ�ʹ��\"}");
			System.out.println("�û����ѱ�ʹ��");
		}
		
		out.flush();
		out.close();
	}


}

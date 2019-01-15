import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class LoginServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		String username=requset.getParameter("username");
		String password=requset.getParameter("password");
		UserDaoImpl userdao=new UserDaoImpl();
		User user=userdao.findUser(username);
		if (user==null)user=new User();
		if(username.equals(user.getUsername())&&password.equals(user.getPassword()))
		{
			String id=requset.getSession().getId();
			requset.getSession().setAttribute("user",user);
			out.print(id);
			out.flush();
			out.close();
		}else
		{
			out.print("error");
		}
		
	System.out.println("nihao");
	}
	
}

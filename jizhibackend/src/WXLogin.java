import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class WXLogin extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	
		PrintWriter out = response.getWriter();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		UserDaoImpl userdao=new UserDaoImpl();
		User user=userdao.findUser(username);
		if (user==null)user=new User();
		if(username.equals(user.getUsername())&&password.equals(user.getPassword()))
		{
			String id=request.getSession().getId();
			String openid=(String)request.getSession().getAttribute("openid");
			request.getSession().setAttribute("user",user);
			userdao.binduser(user,openid);
			out.print("µÇÂ¼³É¹¦");
			
		}else
		{
			out.print("error");
		}
		out.flush();
		out.close();

	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	

}

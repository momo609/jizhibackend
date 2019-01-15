import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class Login extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject jo=new JSONObject();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		UserDaoImpl userdao=new UserDaoImpl();
//		User user=(User)request.getSession().getAttribute("user");
//		if(user!=null)
//		{
//			String userID = user.getUserid()+"";
//			jo.element("userid", userID);
//			jo.element("errmsg", "你已登录请不要重复登录");
//			jo.element("errcode", "100");
//			jo.element("usertype", user.getUsertype());
//			out.print(jo);
//			out.flush();
//			out.close();
////			out.print("{\"errcode\":100,\"errmsg\":\"你已登录请不要重复登录\"}");
//			return;
//		}
		if(username==null||password==null)
		{
			out.print("{\"errcode\":102,\"errmsg\":\"用户名不存在\"}");
			return;
		}
		User user=userdao.findUser(username);
		if (user==null) {
			jo.element("errcode", "103");
			out.print(jo);
			return;
		}
		if(username.equals(user.getUsername())&&password.equals(user.getPassword()))
		{
			String userID = user.getUserid()+"";
//			System.out.println("Userid:"+userID);
			String id=request.getSession().getId();
			request.getSession().setAttribute("user",user);
			String openid=(String)request.getSession().getAttribute("openid");
			userdao.binduser(user,openid);
			jo.element("errmsg", "登录成功");
			jo.element("errcode","100");
			jo.element("userid", userID);
			jo.element("usertype", user.getUsertype());
			jo.element("nickname", user.getNickname());
//			System.out.println(jo);
//			out.print("{\"errcode\":0,\"userid\":\"1069\"}");
			out.print(jo);
			out.flush();
			out.close();
		}else
		{
			out.print("{\"errcode\":102,\"errmsg\":\"用户名或密码不正确\"}");
		}
	}

}

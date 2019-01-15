import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.cookie.Cookie;

import util.CheckopenId;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class GetUserInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
//		String currentOpenid = (String)request.getSession().getAttribute("openid");
//		CheckopenId c = new CheckopenId();
//		boolean flag = c.check(user, currentOpenid);
		UserDaoImpl userdao=new UserDaoImpl();
	
		if((user==null))
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}else
		{
			user=userdao.findUser(user.getUsername());
			user.setPassword("");
			user.setUsername("");
			JSONObject userJo=JSONObject.fromObject(user);
			userJo.element("errcode", 0);
			userJo.element("errmsg",userid);
			out.print(userJo);
		}
		
		
		out.flush();
		out.close();
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

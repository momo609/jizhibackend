import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import util.WXUtil;

import com.jizhibackend.bean.User;
import com.jizhitest.service.UserDaoImpl;


public class WXOauthLogin extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		Map<String,String> info = new HashMap<String,String>();
		response.sendRedirect("http://dawnlab.gxu.edu.cn/weixinduan/test_in.html");
//		PrintWriter out = response.getWriter();
//		JSONObject jo=new JSONObject();
//		String code=request.getParameter("code");
//		info = WXUtil.getOpenid(code);
//		String openid=WXUtil.getOpenid(code);
//		jo.element("openid", openid);
//		String refresh_token = info.get("refresh_token").toString();
//		if(openid!=null)
//		{
//			request.getSession().setAttribute("openid", openid);
//			UserDaoImpl userDaoImpl=new UserDaoImpl();
//			User user=userDaoImpl.findUserByOpenid(openid);
//			if(user==null)
//			{
////				out.print(jo);
//				response.sendRedirect("http://dawnlab.gxu.edu.cn/weixinduan/test_in.html");
//			}
//			else
//			{
////				out.print(jo);
//				request.getSession().setAttribute("user", user);
//				response.sendRedirect("http://dawnlab.gxu.edu.cn/weixinduan/test_in.html");
////				response.sendRedirect("http://dawnlab.gxu.edu.cn/weixinduan/login.html");
//			}
//		}else
//		{
////			out.print(jo);
//			response.sendRedirect("http://dawnlab.gxu.edu.cn/weixinduan/test_in.html");
//		}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}

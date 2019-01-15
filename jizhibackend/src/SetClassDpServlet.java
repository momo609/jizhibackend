import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class SetClassDpServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		String url=request.getParameter("dp_url");
		int classid=Integer.parseInt(request.getParameter("classid"));
		MyClassDaoImpl myclassdao=new MyClassDaoImpl();
		if(myclassdao.setClassDp(classid, url)!=0)
		{
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		}else
		{
			out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
		}
		out.flush();
		out.close();
	}
	

}

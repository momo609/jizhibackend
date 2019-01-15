import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;
public class DeleteTestServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		int testid=Integer.parseInt(request.getParameter("testid"));
	    MyTestDaoImpl maDaoImpl=new MyTestDaoImpl();
	    User user = (User) request.getSession().getAttribute("user");
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		if((user==null))
			user = dao1.findUser(userid);
	    if(user!=null)
	    {
	   if(maDaoImpl.deleteTestById(testid)!=0)
	   {
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	   }else
	   {
			out.print("{\"errcode\":104,\"errmsg\":\"ϵͳ����\"}");
	   }
	    }else
	    {
	    	out.print("{\"errcode\":104,\"errmsg\":\"��¼����\"}");
	    }
		out.flush();
		out.close();
	}

}

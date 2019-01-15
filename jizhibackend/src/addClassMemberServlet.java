import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;

public class addClassMemberServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String classid = request.getParameter("classid");
		String remarkname = request.getParameter("remarkname");
		String studentid = request.getParameter("userid");
//		int userid = Integer.parseInt(request.getParameter("userid")) ;
//		User user = (User) request.getSession().getAttribute("user");
//		UserDaoImpl dao = new UserDaoImpl();
//		if((user==null))
//		User user = dao.findUser(userid);
//		if (user != null) {
			MyClassDaoImpl myclassdao = new MyClassDaoImpl();
		    UserDaoImpl userDaoImpl=new UserDaoImpl();
		    User joinuser=userDaoImpl.findUser(Integer.parseInt(studentid));
		    if(joinuser==null)
		    {
		    	out.print("{\"errcode\":113,\"errmsg\":\"用户不存在\"}");
		    	out.flush();
		    	out.close();
		    	return;
		    }
	
		    List list=myclassdao.getClassMemberIds(Integer.parseInt(classid));
		    if(list.contains(Integer.parseInt(studentid)))
		    {
		    	out.print("{\"errcode\":114,\"errmsg\":\"用户已加入该班级\"}");
		    	out.flush();
		    	out.close();
		    	return;
		    }
		    if(remarkname==null||remarkname.equals(""))
		    {
		    	remarkname=joinuser.getNickname();
		    }
			if (myclassdao.addClassMember(Integer.parseInt(classid),
					Integer.parseInt(studentid), remarkname)) {
				out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
			} else {
				out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
			}
//		}else
//		{
//			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
//		}
	}
}

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

public class RemoveMemberFromClass extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		UserDaoImpl userdao = new UserDaoImpl();
//		User user = (User) request.getSession().getAttribute("user");
		int classid = Integer.parseInt(request.getParameter("classid"));
		int userid = Integer.parseInt(request.getParameter("userid"));
		User user = userdao.findUser(userid);
		int studentid = Integer.parseInt(request.getParameter("studentid"));
		MyClassDaoImpl dao = new MyClassDaoImpl();
		MyClass myClass = dao.findClass(classid);
		List<Integer> managers = dao.getClassManagerById(classid);

		if ((managers.contains(user.getUserid()) || myClass.getOwner() == user
				.getUserid())) {
			if (studentid != myClass.getOwner()) {
				if (managers.contains(studentid)) {
					if(user.getUserid()==myClass.getOwner())
					{
					dao.removeClassMember(classid, studentid);
					dao.delClassManager(classid, studentid);
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
					}else
					{
						out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");
					}
					
				} else
				{
				if (dao.removeClassMember(classid, studentid))
					out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
				else
					out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
				}
			} else
				out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");
		} else {
			out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");
		}

		out.flush();
		out.close();

	}
}

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class DeleteClassFile extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String classid = request.getParameter("classid");
		String filename = request.getParameter("filename");
		User user = (User) request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		if (user != null) {
			String path =this.getServletContext().getRealPath(
					"/File/upload/");
			File file=new File(path+"/"+classid,filename);
			file.delete();
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		}else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"µÇÂ¼¹ýÆÚ\"}");
		}
	}
}

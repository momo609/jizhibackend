import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;

public class CreateNewPaperServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		User user = (User) request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		if((user==null))
			user = dao1.findUser(userid);
		PrintWriter out = response.getWriter();
		if (user != null) {
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			Paper paper = new Paper();
			paper.setTitle(title);
			paper.setDescription(description);
			paper.setCreatetime(System.currentTimeMillis());
			paper.setOwner(user.getUserid());
			PaperDaoImpl dao = new PaperDaoImpl();		
			if (dao.createPaper(paper) != 0) {
				out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
			} else {
				out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
			}
		}else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}

		out.flush();
		out.close();
	}
}

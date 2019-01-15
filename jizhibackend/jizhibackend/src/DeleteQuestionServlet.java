import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.UserDaoImpl;

public class DeleteQuestionServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
//		User user = (User) request.getSession().getAttribute("user");
		int paperid = Integer.parseInt(request.getParameter("paperid"));
		int userid = Integer.parseInt(request.getParameter("userid"));
		int qid = Integer.parseInt(request.getParameter("qid"));
		PaperDaoImpl dao = new PaperDaoImpl();
		UserDaoImpl userdao = new UserDaoImpl();
		User user = userdao.findUser(userid);
		JSONObject jo=new JSONObject();
		if (user != null) {

			if (dao.delQ(paperid, qid)) {
				jo.element("errcode","0");
				out.print(jo);
			} else {
				out.print("{\"errcode\":104\",errmsg\":\"系统错误\"}");
			}
		} else {
			out.print("{\"errcode\":105\",errmsg\":\"登录过期\"}");
		}
		out.flush();
		out.close();
	}

}

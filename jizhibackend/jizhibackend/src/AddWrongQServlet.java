import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class AddWrongQServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String wrongQIDs=request.getParameter("wrongQIDs");
		int testId=Integer.parseInt(request.getParameter("testid"));
		MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
		MyTest t=myTestDaoImpl.getTestByid(testId);
		long endTime = t.getEnd_time();
		int userid=Integer.parseInt(request.getParameter("userid"));
		String testTitle=request.getParameter("testtitle");
//		User user=(User)request.getSession().getAttribute("user");
		UserDaoImpl dao = new UserDaoImpl();
		User user = dao.findUser(userid);
		QuestionDaoImpl daoImpl=new QuestionDaoImpl();
		daoImpl.addWrongQuestions(user.getUserid(),wrongQIDs,testId,testTitle,endTime);
		out.flush();
		out.close();
	}
}

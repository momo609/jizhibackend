import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.TestResultDaoImpl;


public class EnterTestCheckServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
	    User user=(User)request.getSession().getAttribute("user");
	    int testid=Integer.parseInt(request.getParameter("testid"));
	    TestResultDaoImpl impl=new TestResultDaoImpl();
	   TestResult t= impl.findTestResult(testid, user.getUserid());
	   if(t==null)
	   {
		   out.print("success");
	   }else
	   {
		   out.print("error");
	   }
		out.flush();
		out.close();
	}

}

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class getTestResultforTeacherServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Integer> proportion = new ArrayList<Integer>();
		PrintWriter out = response.getWriter();
//	    User user=(User)request.getSession().getAttribute("user");
	    int testid=Integer.parseInt(request.getParameter("testid"));
	    int userid = Integer.parseInt(request.getParameter("studentid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		User user = dao.findUser(userid);
	    TestResultDaoImpl impl=new TestResultDaoImpl();
	    TestResult t= impl.findTestResult(testid, user.getUserid());
	    proportion = MyTestDaoImpl.proportionTest(testid, t.getAnswers());
	   if(t!=null)
	   {
		   JSONObject jo=new JSONObject();
		   JSONObject joTestResult=JSONObject.fromObject(t);
		   jo.put("TestResult", joTestResult);
		   jo.put("proportion", proportion);
		   jo.element("errcode", 0);
		   jo.element("errmsg", "success");
		   jo.element("systime", System.currentTimeMillis());
		   out.print(jo);
	   }else
	   {
		   out.print("{\"errcode\":110,\"errmsg\":\"用户未提交该次测试答案\"}");
	   }
		out.flush();
		out.close();
	}

}

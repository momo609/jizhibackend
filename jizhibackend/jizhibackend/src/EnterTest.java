import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class EnterTest extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
	    User user=(User)request.getSession().getAttribute("user");
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
	    int testid=Integer.parseInt(request.getParameter("testid"));
	    MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
	    TestResultDaoImpl impl=new TestResultDaoImpl();
	    TestResult t= impl.findTestResult(testid, user.getUserid());
	   if(t==null)
	   {
		   MyTest myTest=myTestDaoImpl.getTestByid(testid);
		   QuestionDaoImpl questionDaoImpl =new QuestionDaoImpl();
		   Map<String, List<?>> map=new HashMap<String, List<?>>();
		   map=questionDaoImpl.getQuestionMapOfTestPaper(myTest.getUse_paperid());
		   JSONObject jo=JSONObject.fromObject(map);
			jo.element("systime", System.currentTimeMillis());
			jo.element("errcode", 0);
			jo.element("errmsg", "success");
//			System.out.println("Sss:"+jo.toString());
			out.print(jo);
	   }else
	   {
		   MyTest myTest=myTestDaoImpl.getTestByid(testid);
		   QuestionDaoImpl questionDaoImpl =new QuestionDaoImpl();
		   Map map=questionDaoImpl.getQuestionMapOfTestPaper(myTest.getUse_paperid());
		   JSONObject jo=JSONObject.fromObject(map);
			jo.element("peperid", myTest.getUse_paperid());
			jo.element("errcode", 1);
			jo.element("errmsg", "“—ÕÍ≥…≤‚ ‘£°");
//			System.out.println("Sss:"+jo.toString());
			out.print(jo);
		   
	   }
		out.flush();
		out.close();
	
	}

}

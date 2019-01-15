import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class getTestResultServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Integer> proportion = new ArrayList<Integer>();
		PrintWriter out = response.getWriter();
	    int testid=Integer.parseInt(request.getParameter("testid"));
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		User user = dao.findUser(userid);
	    TestResultDaoImpl impl=new TestResultDaoImpl();
	    TestResult t= impl.findTestResult(testid, user.getUserid());
	    MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
	    MyTest mytest=myTestDaoImpl.getTestByid(testid);
	    
	    int paperid=mytest.getUse_paperid();
	    QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
	    List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
	    
	    String rightAnswers = "";
	    String questionIds = "";
	    for (Question question : qlist) {
			rightAnswers = rightAnswers + question.getAnswer()+"@@";
			questionIds = questionIds + question.getId() + "@@";
		}
	    rightAnswers = rightAnswers.substring(0, rightAnswers.length()-2);
	    questionIds = questionIds.substring(0, questionIds.length()-2);
	    proportion = MyTestDaoImpl.proportionTest(testid, t.getAnswers());
	    Map<String, Object> map = impl.getTestStat(testid,user.getUserid());
	   if(t!=null)
	   {
		   JSONObject jo=new JSONObject();
		   JSONObject joTestResult=JSONObject.fromObject(t);
		   JSONObject jomap = JSONObject.fromObject(map);
		   jo.put("TestResult", joTestResult);
		   jo.put("TestStat",jomap);
		   jo.put("proportion", proportion);
		   jo.put("rightAnswer", rightAnswers);
		   jo.put("questionId", questionIds);
		   jo.element("errcode", 0);
//		   jo.element("errmsg", "success");
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

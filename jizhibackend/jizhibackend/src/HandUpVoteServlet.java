import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MyUtil;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhibackend.bean.VoteTestResult;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;
import com.jizhitest.service.VoteTestResultDaoImpl;

public class HandUpVoteServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		if (user != null) {
			String votetestid = (String) request.getParameter("votetestid");
			String answers = (String) request.getParameter("answers");
			int studentid = Integer.parseInt(request.getParameter("studentid"));
			User student=dao.findUser(studentid);
			VoteTestResult t = new VoteTestResult();
			t.setVoteTestid(Integer.parseInt(votetestid));
			t.setAnswers(answers);
			t.setStudentid(studentid);
			t.setUserid(userid);
			t.setStudentname(student.getNickname());
			t.setScore(MyVoteTestDaoImpl.scoreTest(Integer.parseInt(votetestid),answers,studentid));
			VoteTestResultDaoImpl testResultDaoImpl = new VoteTestResultDaoImpl();
			MyVoteTestDaoImpl myTestDaoImpl=new MyVoteTestDaoImpl();
			if (testResultDaoImpl.addTestResult(t) != 0) {
				 MyVoteTest myTest=myTestDaoImpl.getVoteTestByid(Integer.parseInt(votetestid));
//				   QuestionDaoImpl questionDaoImpl =new QuestionDaoImpl();
//				   Map<String, List<?>> map=new HashMap<String, List<?>>();
//				   map=questionDaoImpl.getQuestionMapOfTestPaper(myTest.getUse_paperid());
				   JSONObject jo=new JSONObject();
					JSONArray ja=JSONArray.fromObject(myTest);
//					System.out.println("ja:"+ja);
					jo.element("votetest", ja);
					jo.element("errcode", 0);
					jo.element("errmsg", "success");
					out.print(jo);
			} else {
				out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
				out.flush();
				out.close();
			}
		} else {
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
			out.flush();
			out.close();
		}
	}
}

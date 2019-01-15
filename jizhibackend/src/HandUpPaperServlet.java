import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import util.MyUtil;

import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;

public class HandUpPaperServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
//		try {
//		    Thread.sleep(10000);                 //1000 ���룬Ҳ����1��.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
		if((user==null))
			user = dao.findUser(userid);
		if (user != null) {
			String testid = (String) request.getParameter("testid");
			String score = request.getParameter("score");
			String answers = (String) request.getParameter("answers");
			String answerTrace = (String) request.getParameter("answertrace");
			String timeused = (String) request.getParameter("timeused");
//			System.out.println("timeused:"+timeused);
			String totaltime = (String) request.getParameter("totaltime");
//			System.out.println("totaltime:"+totaltime);
			String lookbackCount = (String) request
					.getParameter("lookbackcount");
			String bookmark = (String) request.getParameter("bookmark");
//			System.out.println("�ղأ�"+bookmark);
//			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
//			out.flush();
//			out.close();
			
			
			String[] bookmarks = bookmark.split("@");
			List<Integer> qidlist = new ArrayList<Integer>();
			if (!bookmark.equals(""))
				for (String s : bookmarks) {
					qidlist.add(Integer.parseInt(s));
				}
			TestResult t = new TestResult();
			t.setTestid(Integer.parseInt(testid));
			t.setAnswers(answers);
			t.setStudentid(user.getUserid());
			t.setAnswer_trace(answerTrace);
			t.setLook_back_times(lookbackCount);
			t.setScore(MyTestDaoImpl.scoreTest(Integer.parseInt(testid),
					answers,userid));
			t.setTotal_time_used(Long.parseLong(totaltime));
			t.setTime_used(timeused);
			t.setProportion(MyTestDaoImpl.proportionTest(Integer.parseInt(testid),answers).toString());
			t.setTagproportion(MyTestDaoImpl.proportionTag(Integer.parseInt(testid),answers).toString());
			List<Integer> timeusedList = MyUtil.String2IntArray(timeused);
			TestResultDaoImpl testResultDaoImpl = new TestResultDaoImpl();
			QuestionDaoImpl questionDaoImpl = new QuestionDaoImpl();
			JSONObject jo=new JSONObject();
			if (testResultDaoImpl.addTestResult(t, timeusedList) != 0) {
				questionDaoImpl.addBookmarkList(Integer.parseInt(testid),qidlist, user.getUserid(),1,(int)System.currentTimeMillis());
				jo.put("score", t.getScore());
				jo.element("errcode", 0);
				out.print(jo);
			} else {
				out.print("{\"errcode\":104,\"errmsg\":\"ϵͳ����\"}");
				out.flush();
				out.close();
			}
		} else {
			out.print("{\"errcode\":105,\"errmsg\":\"��¼����\"}");
			out.flush();
			out.close();
		}
	}
}

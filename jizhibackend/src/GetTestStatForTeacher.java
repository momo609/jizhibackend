import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetTestStatForTeacher extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
//		User user = (User) request.getSession().getAttribute("user");
		int testid = Integer.parseInt(request.getParameter("testid"));
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
//		if((user==null))
		User user = dao.findUser(userid);
		TestResultDaoImpl impl = new TestResultDaoImpl();
		
		
		
		
		if (user != null) {
			if (user.getUsertype() == User.teacher) {
				int paperid = Integer.parseInt(request.getParameter("paperid"));
				QuestionDaoImpl qdImpl = new QuestionDaoImpl();
				Map map = qdImpl.getQuestionMapOfTestPaper(paperid);
				Map<String, Object> map1 = impl.getTestStat(testid,user.getUserid());
				List<Question> q = qdImpl.getQustionsOfTestPaper(paperid);
				int totalScore = 0;
				for (Question question : q) {
					totalScore += question.getPoint();
				}
				List<TestResult> t = impl.findTestResultByTestid(testid);
				if (t != null) {
					JSONObject jo = new JSONObject();
					JSONArray ja = JSONArray.fromObject(t);
					JSONObject jo2 = JSONObject.fromObject(map);
					JSONObject jomap = JSONObject.fromObject(map1);
					System.out.println();
					jo.put("TestResults", ja);//全部学生的测试结果
					//System.out.println("11:"+t.get(0).getProportion());
					jo.put("questionMap", jo2);
					jo.put("TestStat",jomap);//最高分、最低分、平均分、平均时间
					jo.element("errcode", 0);
					jo.element("totalscore", totalScore);
					jo.element("errmsg", "success");
					out.print(jo);
//					System.out.println(ja);
				} else {
					out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
				}
			} else {
				out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");

			}
		}
		else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}
		out.flush();
		out.close();
	}

	}



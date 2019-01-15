import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.CheckopenId;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;

public class GetTestStatServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
//		String currentOpenid = (String)request.getSession().getAttribute("openid");
//		CheckopenId c = new CheckopenId();
//		boolean flag = c.check(user, currentOpenid);
		int testid = Integer.parseInt(request.getParameter("testid"));
		TestResultDaoImpl impl = new TestResultDaoImpl();
//		if ((user != null)) {

//				System.out.println(user.getUserid());
				Map<String, Object> map = impl.getTestStat(testid,
						user.getUserid());
				JSONObject jo = JSONObject.fromObject(map);
				jo.element("errcode", 0);
				jo.element("errmsg", "success");
				out.print(jo);

			
//		}
//		else
//		{
//			out.print("{\"errcode\":105,\"errmsg\":\"µÇÂ¼¹ýÆÚ\"}");
//		}
		out.flush();
		out.close();
	}

}

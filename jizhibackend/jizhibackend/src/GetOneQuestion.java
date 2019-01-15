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

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;


public class GetOneQuestion extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		int qid=Integer.parseInt(request.getParameter("qid"));
		QuestionDaoImpl qdImpl=new QuestionDaoImpl();
		Map map =qdImpl.getOneQuestionMap(qid);
	    JSONObject jo=JSONObject.fromObject(map);
		jo.element("systime", System.currentTimeMillis());
		out.print(jo);
		out.flush();
		out.close();
	}
}

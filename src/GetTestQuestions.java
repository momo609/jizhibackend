import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhitest.service.QuestionDaoImpl;


public class GetTestQuestions extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		int paperid=Integer.parseInt(request.getParameter("paperid"));
		QuestionDaoImpl qdImpl=new QuestionDaoImpl();
		Map map =qdImpl.getQuestionMapOfTestPaper(paperid);
	    JSONObject jo=JSONObject.fromObject(map);
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		jo.element("systime", System.currentTimeMillis());
		
		out.print(jo);
		out.flush();
		out.close();
	}

}

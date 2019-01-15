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
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
public class GetPaperQuestionServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		int paperid=Integer.parseInt(request.getParameter("paperid"));
		QuestionDaoImpl qdImpl=new QuestionDaoImpl();
		Map map =qdImpl.getQuestionMapOfPaper(paperid);
	    JSONObject jo=JSONObject.fromObject(map);
//		System.out.println(jo);
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		jo.element("systime", System.currentTimeMillis());
		
		out.print(jo);
		out.flush();
		out.close();
	}
}

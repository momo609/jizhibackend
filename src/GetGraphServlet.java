import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.KnowledgePoint;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.RecommendDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetGraphServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
	    User user=(User)request.getSession().getAttribute("user");
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		int classid=Integer.parseInt(request.getParameter("classid"));
	    int studentid=Integer.parseInt(request.getParameter("studentid"));
	    RecommendDaoImpl myrecommend=new RecommendDaoImpl();
	    Map<String,List<KnowledgePoint>> klist=new HashMap();
	    List<KnowledgePoint> kp=new ArrayList();
	    try {
			kp=myrecommend.getkpdegree(studentid);
			klist.put("knowledgepoint", kp);
			JSONObject jo=JSONObject.fromObject(klist);
		    jo.element("errcode", 0);
			jo.element("errmsg", "success");
		    out.print(jo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
		}
	    
		out.flush();
		out.close();
	}

}

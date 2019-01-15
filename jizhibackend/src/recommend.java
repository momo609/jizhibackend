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

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.RecommendDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class recommend extends HttpServlet {

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
		RecommendDaoImpl  rdao=new RecommendDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
	    int classid=Integer.parseInt(request.getParameter("classid"));
		QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
		List<String> concepts=new ArrayList<String>();
		try {
			concepts=rdao.recommendforstudent(userid, classid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(concepts.size()!=0)
		{
			List<Integer>fqlist=new ArrayList<Integer>();
		    Map<String, List<?>> map=new HashMap<String, List<?>>();
		    for(int j=0;j<concepts.size();j++)
		   {
			   List<Question> qlist=questionDaoImpl.getQuestionsofconcepts(concepts.get(j));
			
				for(int i=0;i<qlist.size();i++)
				{
					fqlist.add(qlist.get(i).getQuestionid());  //推荐习题集
					
				}
		    }
		   map.put("recommend", fqlist);
		   JSONObject jo=JSONObject.fromObject(map);
		   jo.element("errcode", 0);
		   jo.element("errmsg", "success");
		   out.print(jo);
		}
		else
		{
			out.print("{\"errcode\":1,\"errmsg\":\"无推荐习题\"}");
		}
		out.flush();
		out.close();
	}

}

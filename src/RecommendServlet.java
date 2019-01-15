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

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.RecommendDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class RecommendServlet extends HttpServlet {

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
	    ArrayList<String> recommendlist=new ArrayList();
	    QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
	    List<Question>fqlist=new ArrayList<Question>();
	    Map<String,List<Question>> rlist=new HashMap();
	    try {
			recommendlist=myrecommend.recommendforstudent(studentid, classid);
			for(int j=0;j<recommendlist.size();j++)
			{
				System.out.println(recommendlist.get(j));
				List<Question> qlist=questionDaoImpl.getQuestionsofconcepts(recommendlist.get(j));
				
					for(int i=0;i<qlist.size();i++)
					{
						fqlist.add(qlist.get(i));  //推荐习题集
						//System.out.println(qlist.get(i).getStem()+" "+qlist.get(i).getChoices()+" "+qlist.get(i).getAnswer());
					}
			}
			rlist.put("recommendlist", fqlist);
			JSONObject jo=JSONObject.fromObject(rlist);
		    jo.element("errcode", 0);
			jo.element("errmsg", "success");
		    out.print(jo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("{\"errcode\":104,\"errmsg\":\"系统错误\"}");
		}
	    
		out.flush();
		out.close();
	}

}

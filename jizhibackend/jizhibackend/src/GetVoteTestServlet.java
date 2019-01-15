import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.CheckopenId;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetVoteTestServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		String classid = request.getParameter("classid") ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		if((user!=null))
		{
			MyVoteTestDaoImpl daoImpl=new MyVoteTestDaoImpl();
			List<MyVoteTest> testList=daoImpl.getVoteOfClass(classid);
				JSONObject jo=new JSONObject();
				JSONArray ja=JSONArray.fromObject(testList);
//				System.out.println("ja:"+ja);
				jo.element("system_time", System.currentTimeMillis());
				jo.element("voteList", ja);
				jo.element("errcode", 0);
//				jo.element("errmsg", "success");
				out.print(jo);
	  
		}else
			out.print("{\"errcode\":105,\"errmsg\":\"µÇÂ¼¹ýÆÚ\"}");
		

		out.flush();
		out.close();
	}


}

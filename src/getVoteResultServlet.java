import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.User;
import com.jizhibackend.bean.VoteTestResult;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;
import com.jizhitest.service.VoteTestResultDaoImpl;


public class getVoteResultServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
	    int votetestid=Integer.parseInt(request.getParameter("votetestid"));
	    int studentid = Integer.parseInt(request.getParameter("studentid")) ;
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		User user = dao.findUser(userid);
	    VoteTestResultDaoImpl impl=new VoteTestResultDaoImpl();
	    Map<String,Object> map=new HashMap<String, Object>();
	     int score= impl.findTestResults(votetestid,studentid);
	   if(score!=-1)
	   {
		   JSONObject jo=new JSONObject();
		   jo.put("score", score);
		   jo.element("errcode", 0);
//		   jo.element("errmsg", "success");
		   jo.element("systime", System.currentTimeMillis());
		   out.print(jo);
	   }else
	   {
		   out.print("{\"errcode\":110,\"errmsg\":\"用户未提交该次测试答案\"}");
	   }
		out.flush();
		out.close();
	}

}

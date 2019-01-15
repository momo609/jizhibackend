import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhibackend.bean.VoteTestResult;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;
import com.jizhitest.service.VoteTestResultDaoImpl;


public class getVoteResultForTeacher extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Integer> proportion = new ArrayList<Integer>();
		PrintWriter out = response.getWriter();
//	    User user=(User)request.getSession().getAttribute("user");
	    int votetestid=Integer.parseInt(request.getParameter("votetestid"));
//	    int userid = Integer.parseInt(request.getParameter("studentid")) ;
		UserDaoImpl dao = new UserDaoImpl();
//		User user = dao.findUser(userid);
		MyVoteTestDaoImpl myTestDaoImpl=new MyVoteTestDaoImpl();
		MyVoteTest myTest=myTestDaoImpl.getVoteTestByid(votetestid);
		String studentid=myTest.getStudentid();
		String[] studentids=studentid.split(",");
	    VoteTestResultDaoImpl impl=new VoteTestResultDaoImpl();
	    ArrayList<String> name = new ArrayList<String>();
	    ArrayList<String> id = new ArrayList<String>();
	    ArrayList<Integer> scoress = new ArrayList<Integer>();
	    int scores=-1;
	    for(int i=0;i<studentids.length;i++)
	    {
	    	 scores= impl.findTestResults(votetestid, Integer.parseInt(studentids[i]));
	    	 User student=dao.findUser(Integer.parseInt(studentids[i]));
	    	 name.add(student.getNickname());
	    	 id.add(studentids[i]);
	    	 scoress.add(scores);
//	    	 map2.put(studentids[i], student.getNickname());
//	    	 map.put(student.getNickname(), scores);
	    }
	    
	   // proportion = MyTestDaoImpl.proportionTest(testid, t.getAnswers());
	   if(scoress!=null)
	   {
		   JSONObject jo=new JSONObject();
		   JSONArray joid=JSONArray.fromObject(id);
		   jo.put("id", joid);
		   JSONArray joname=JSONArray.fromObject(name);
		   jo.put("name", joname);
		   JSONArray joscoress=JSONArray.fromObject(scoress);
		   jo.put("score", joscoress);
		  // jo.put("proportion", proportion);
		   jo.element("errcode", 0);
		   jo.element("errmsg", "success");
		   jo.element("systime", System.currentTimeMillis());
		   out.print(jo);
	   }else
	   {
		   out.print("{\"errcode\":110,\"errmsg\":\"ÏµÍ³´ð°¸\"}");
	   }
		out.flush();
		out.close();
	}

}

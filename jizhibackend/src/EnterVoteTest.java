import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.User;
import com.jizhibackend.bean.VoteTestResult;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;
import com.jizhitest.service.VoteTestResultDaoImpl;


public class EnterVoteTest extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
	    User user=(User)request.getSession().getAttribute("user");
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
	    int votetestid=Integer.parseInt(request.getParameter("votetestid"));
	    String studentid=request.getParameter("studentid");
	    System.out.println(studentid);
	    String studentids[]=studentid.split(",");
	    MyVoteTestDaoImpl myTestDaoImpl=new MyVoteTestDaoImpl();
	    VoteTestResultDaoImpl impl=new VoteTestResultDaoImpl();
	    ArrayList<Integer> done = new ArrayList<Integer>();
	    for(int i=0;i<studentids.length;i++)
	    {
	    	 VoteTestResult t= impl.findTestResult(votetestid, Integer.parseInt(studentids[i]),userid);
	    	 if(t==null)
	    		 done.add(1);//Ã»Íê³É
	    	 else
	    		 done.add(0);
	    }
	   
		   MyVoteTest myTest=myTestDaoImpl.getVoteTestByid(votetestid);
//		   QuestionDaoImpl questionDaoImpl =new QuestionDaoImpl();
//		   Map<String, List<?>> map=new HashMap<String, List<?>>();
//		   map=questionDaoImpl.getQuestionMapOfTestPaper(myTest.getUse_paperid());
		   JSONObject jo=new JSONObject();
			JSONArray ja=JSONArray.fromObject(myTest);
			JSONArray ja1=JSONArray.fromObject(done);
//			System.out.println("ja:"+ja);
			jo.element("votetest", ja);
			jo.element("done",ja1);
			jo.element("studentid", studentid);
			jo.element("errcode", 0);
			jo.element("errmsg", "success");
			out.print(jo);
		out.flush();
		out.close();
	
	}

}

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.jizhibackend.bean.MyVoteTest;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class CreateVoteTestServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		if((user==null))
			user = dao1.findUser(userid);
		if(user!=null)
		{
			int owner=user.getUserid();
			String title=request.getParameter("title");
			long starttime=Long.parseLong(request.getParameter("start_time"));
			long endttime=Long.parseLong(request.getParameter("end_time"));
			long createtime=System.currentTimeMillis();
			String studentid=request.getParameter("studentid");
			String studentname=request.getParameter("studentname");
//			int usepaperid=Integer.parseInt(request.getParameter("paperid"));
//			int privilege=Integer.parseInt(request.getParameter("privilege"));
			String[] classes=request.getParameter("classes").split("@");
//		    PaperDaoImpl paperDaoImpl=new PaperDaoImpl();
//		    Paper paper= paperDaoImpl.getPaperByid(usepaperid);
//		    paperDaoImpl.createTestPaper(paper);
			MyVoteTest myTest=new MyVoteTest();
			myTest.setTitle(title);
			myTest.setCreate_time(createtime);
			myTest.setEnd_time(endttime);
			myTest.setStart_time(starttime);
//			myTest.setUse_paperid(paper.getId());
			myTest.setOwner(owner);
			myTest.setVoteTestid((int)System.currentTimeMillis());
			myTest.setStudentid(studentid);
			myTest.setStudentname(studentname);
//			myTest.setPrivilege(privilege);
			
			MyVoteTestDaoImpl dao=new MyVoteTestDaoImpl();
			if(dao.createTest(myTest))
			{
				for(String classid:classes)
				dao.setTakePartInClass(myTest, Integer.parseInt(classid));
				out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
			}else
			{
				out.print("{\"success\":104,\"errmsg\":\"系统错误\"}");
			}
			
			
			
		}else
		{
			out.print("{\"success\":false,\"errmsg\":\"登录过期\"}");
		}
		
		
		out.flush();
		out.close();
	}


}


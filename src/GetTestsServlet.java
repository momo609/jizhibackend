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

import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetTestsServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		int type = Integer.parseInt(request.getParameter("type")) ;//1=进行中，2=已结束，3=未开始
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		if((user!=null))
		{
			MyTestDaoImpl daoImpl=new MyTestDaoImpl();
			List<MyTest> testList=daoImpl.getTestsOfUsers(user,type);
			if(type==1||type==2){
				ArrayList<Integer> al = new ArrayList<Integer>();
				TestResultDaoImpl impl=new TestResultDaoImpl();
				for (MyTest myTest : testList) {
					TestResult t= impl.findTestResult(myTest.getTestid(), user.getUserid());
					if(t==null){//未提交过测试
						al.add(0);
					}else{
						al.add(1);
					}
				}
				JSONObject jo=new JSONObject();
				JSONArray ja=JSONArray.fromObject(testList);
//				System.out.println("ja:"+ja);
				JSONArray ja1=JSONArray.fromObject(al);
				jo.element("system_time", System.currentTimeMillis());
				jo.element("testList", ja);
				jo.element("isDone", ja1);
				jo.element("errcode", 0);
//				jo.element("errmsg", "success");
				out.print(jo);
			}
			else{
				JSONObject jo=new JSONObject();
				JSONArray ja=JSONArray.fromObject(testList);
//				JSONArray ja1=JSONArray.fromObject(al);
				jo.element("system_time", System.currentTimeMillis());
				jo.element("testList", ja);
//				System.out.println("test_before:"+ja.toString());
//				jo.element("isDone", ja1);
				jo.element("errcode", 0);
//				jo.element("errmsg", "success");
				out.print(jo);
			}
		}else
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");

		out.flush();
		out.close();
	}


}

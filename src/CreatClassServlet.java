import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class CreatClassServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			
		PrintWriter out = response.getWriter();
		String name=request.getParameter("classname");
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		
		int owner=user.getUserid();
		
	
		int membernum=0;
		
		MyClass myclass=new MyClass();
		myclass.setName(name);
		myclass.setMembernum(membernum);
		myclass.setOwner(owner);
		myclass.setCreatetime(System.currentTimeMillis());
		
		MyClassDaoImpl myclassdao=new MyClassDaoImpl();
	    if(myclassdao.createClass(myclass))
	    	{
	    	myclassdao.addClassMember(myclass.getId(), user.getUserid(), user.getNickname());
	    	out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	    	}
	    else {
	    	out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
		}
	    
		
		

		
	}

	

}

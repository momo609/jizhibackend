import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Student;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetAllTeachersServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		PrintWriter out = response.getWriter();
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		try
		{
		ArrayList<User> list = dao.findAllTeachers(userid);
		JSONArray ja=JSONArray.fromObject(list);
		JSONObject jo=new JSONObject();
		jo.put("allTeachers", list);
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		out.print(jo);
		}catch(Exception e)
		{
			e.printStackTrace();
			out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");	
		}
		out.flush();
		out.close();
	}


}

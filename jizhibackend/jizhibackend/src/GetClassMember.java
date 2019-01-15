import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Student;
import com.jizhitest.service.MyClassDaoImpl;


public class GetClassMember extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		PrintWriter out = response.getWriter();
		MyClassDaoImpl dao=new MyClassDaoImpl();
		try
		{
		int classid=Integer.parseInt(request.getParameter("classid"));
		List<Student> list = dao.getClassMembers(classid);
		List<Integer> mgrs=dao.getClassManagerById(classid);
		JSONArray ja=JSONArray.fromObject(list);
		JSONArray jaMgrs=JSONArray.fromObject(mgrs);
		JSONObject jo=new JSONObject();
		jo.put("classmembers", list);
		jo.put("managers", mgrs);
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

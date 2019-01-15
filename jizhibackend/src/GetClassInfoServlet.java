import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;


public class GetClassInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
      
		
		
		PrintWriter out = response.getWriter();
		MyClassDaoImpl dao=new MyClassDaoImpl();
		MyClass myclass=dao.findClass(
				Integer.parseInt(request.getParameter("classid")));
		if(myclass!=null)
		{
		JSONObject jo=new JSONObject();
		JSONObject joclass=JSONObject.fromObject(myclass);
		jo.put("class", joclass);
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		out.print(jo);
		}else
		{
			out.print("{\"errcode\":112,\"errmsg\":\"°à¼¶²»´æÔÚ\"}");
		}
		

		
		
		out.flush();
		out.close();
	}

}

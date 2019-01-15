import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.jizhitest.service.UserDaoImpl;


public class GetCoinMessageServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		PrintWriter out = response.getWriter();
		UserDaoImpl userdao = new UserDaoImpl();
		try
		{
			int userid=Integer.parseInt(request.getParameter("userid"));
			ArrayList<String> list = userdao.findCoinMessage(userid);
			JSONArray ja=JSONArray.fromObject(list);
			JSONObject jo=new JSONObject();
			jo.put("message", list);
			System.out.println(list.toString());
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

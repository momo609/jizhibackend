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

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetStudentClassServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
//		User student=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid"));
		UserDaoImpl dao1 = new UserDaoImpl();
//		if((student==null))
		User student = dao1.findUser(userid);
		List<MyClass> classList=new ArrayList<MyClass>();
		MyClassDaoImpl dao=new MyClassDaoImpl();
		classList=dao.ClassofStudent(student.getUserid());
		
		JSONArray ja=JSONArray.fromObject(classList);
		JSONObject jo=new JSONObject();
		
		jo.put("classes", ja);
		out.print(jo);
		out.flush();
		out.close();
	}


}

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

/**
 * 获取测试所在班级的学生列表
 * @author Administrator
 *
 */
public class GetTestStudentServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		int testid = Integer.parseInt(request.getParameter("testid"));
		MyClassDaoImpl dao=new MyClassDaoImpl();
		int classid = dao.Classoftest(testid);
		List<Map<String,String>> studentList = new ArrayList<Map<String,String>>();
		studentList = dao.getTestStudentMember(classid);
		JSONArray ja=JSONArray.fromObject(studentList);
		JSONObject jo=new JSONObject();
		
		jo.put("students", ja);
		System.out.println(jo.toString());
		out.print(jo);
		out.flush();
		out.close();
	}


}

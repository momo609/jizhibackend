import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


public class GetOwnerClassServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		UserDaoImpl userdao = new UserDaoImpl();
		int userid = Integer.parseInt(request.getParameter("userid"));
		User Owner = userdao.findUser(userid);
//		User Owner=(User)request.getSession().getAttribute("user");
		String msg="";
		Set<MyClass> classes=new HashSet<MyClass>();
		MyClassDaoImpl dao=new MyClassDaoImpl();
		
		
		//classes.addAll(dao.ClassofOwner(Owner.getUserid()));
		classes.addAll(dao.ClassofStudent(Owner.getUserid()));
		
		JSONArray ja=JSONArray.fromObject(classes);
		JSONObject jo=new JSONObject();
		jo.put("classes", ja);
		out.print(jo);
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

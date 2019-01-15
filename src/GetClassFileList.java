import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.ClassFile;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class GetClassFileList extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int classid=Integer.parseInt(request.getParameter("classid"));
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		MyClassDaoImpl myClassDaoImpl=new MyClassDaoImpl();
		List<Integer> memberlist=myClassDaoImpl.getClassMemberIds(classid);
		String uploadFilePath = this.getServletContext().getRealPath("/File/upload/"+classid);
		if(user!=null)
		{
		if(memberlist.contains(user.getUserid()))
		{
			List<ClassFile> filelist=myClassDaoImpl.getClassFileList(uploadFilePath);
			JSONObject jo=new JSONObject();
			JSONArray ja=JSONArray.fromObject(filelist);
			jo.put("filelist", ja);
			jo.element("errcode", 0);
			jo.put("errmsg", "success");
			out.print(jo);
		}
		else
		{
			out.print("{\"errcode\":111,\"errmsg\":\"没有权限\"}");	
		}
		}else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");	
		}
		out.flush();
		out.close();
	}

}

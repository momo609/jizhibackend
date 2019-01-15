import com.jizhibackend.bean.Paper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;

//获取某用户的试卷列表
public class GetOnlinePaperServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
//		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
		User user = dao.findUser(userid);
		if(user!=null)
		{
		PaperDaoImpl paperdao=new PaperDaoImpl();
		
		List<Paper> paperlist=paperdao.getOnlinePapers(user.getUserid());
		JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(paperlist);
		jo.put("papers", ja);
//		System.out.println(ja.toString());
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		out.print(jo);
		}
		else
		{
			out.print("{\"errcode\":105,\"errmsg\":\"登录过期\"}");
		}
		
		out.flush();
		out.close();
	}


}

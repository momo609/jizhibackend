import com.jizhibackend.bean.Paper;
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

import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;

//获取某用户的试卷列表
public class GetPaperServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		int type = Integer.parseInt(request.getParameter("type")) ;
		UserDaoImpl dao = new UserDaoImpl();
		if((user==null))
			user = dao.findUser(userid);
		if(user!=null)
		{
		PaperDaoImpl paperdao=new PaperDaoImpl();
		//获取该用户自身的paper
		List<Paper> paperlist=paperdao.getPapers(user.getUserid());
		if(type==1){
			//获取其它用户共享的paper
			if(user.getSharePaper()!=null&&user.getSharePaper().length()>1){
				String[] paperids = user.getSharePaper().split(","); //substring()包左不包右
				//通过paperid获取paper
				for (int i = 0; i < paperids.length; i++) {
					if(Integer.parseInt(paperids[i])!=0){
						paperlist.add(paperdao.getPaperByid(Integer.parseInt(paperids[i])));
					}
				}
			}
		
		}
		
		JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(paperlist);
		jo.put("papers", ja);
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

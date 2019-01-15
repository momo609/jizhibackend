import com.jizhibackend.bean.Gift;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhitest.service.GiftDaoImpl;
//获取某班级的礼物列表
public class getProductforTeacServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int userid = Integer.parseInt(request.getParameter("userid"));
		GiftDaoImpl dao = new GiftDaoImpl();
		List<Gift>  giftlist = dao.getGiftforTeac(userid);
		JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(giftlist);
		jo.put("gifts", ja);
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		out.print(jo);
		
		out.flush();
		out.close();
	}


}

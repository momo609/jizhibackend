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

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.GiftDaoImpl;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;
//获取某班级的礼物列表
public class GetProductServelt extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int userid = Integer.parseInt(request.getParameter("userid"));
		UserDaoImpl userdao = new UserDaoImpl();
		User user = userdao.findUser(userid);
		MyClassDaoImpl myclassdao = new MyClassDaoImpl();
		GiftDaoImpl dao = new GiftDaoImpl();
		List<MyClass> myclass = myclassdao.ClassofStudent(userid);//获取学生所有所属班级
		List<List<Gift>> list = new ArrayList<List<Gift>>();
		for (int i = 0; i < myclass.size(); i++) {
			int classid = myclass.get(i).getId();
			List<Gift>  giftlist = dao.getGift(classid);
			list.add(giftlist);
		}
		JSONObject jo=new JSONObject();
		JSONArray ja=JSONArray.fromObject(list);
		jo.put("gifts", ja);
		jo.put("coins", user.getCoins());
		jo.element("errcode", 0);
		jo.element("errmsg", "success");
		out.print(jo);
		
		out.flush();
		out.close();
	}


}

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.jizhibackend.bean.GiftMessage;
import com.jizhitest.service.GiftDaoImpl;
/**
 * 获取申请兑换礼物信息
 * @author Administrator
 *
 */

public class GetApplyGiftServlet extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			GiftDaoImpl giftdao = new GiftDaoImpl();
			List<GiftMessage> list = new ArrayList<GiftMessage>();
			list = giftdao.getGiftMessage(userid);
			
			PrintWriter out = response.getWriter();
			JSONArray jo=JSONArray.fromObject(list);
			out.print(jo);
			out.flush();
			out.close();
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				doPost(request, response);
		}
}

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.User;
import com.jizhitest.service.GiftDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * 修改礼物状态
 * @author Administrator
 *
 */

public class UpdateGiftServlet extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
//			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			int giftid = Integer.parseInt(request.getParameter("giftid")) ;
			int numbers = Integer.parseInt(request.getParameter("numbers"));
			int price = Integer.parseInt(request.getParameter("price"));
			String name = request.getParameter("name");
			
			GiftDaoImpl giftdao = new GiftDaoImpl();
			Gift gift = giftdao.getGiftById(giftid);
			JSONObject jo=new JSONObject();
			PrintWriter out = response.getWriter();
			if(userid==gift.getOwnerid()){
				gift.setName(name);
				gift.setPrice(price);
				gift.setNumbers(numbers);
				if(giftdao.updategift(gift)!=0){
					jo.element("errcode", 100);
					jo.element("errmsg", "修改成功！"); 
				}else{
					jo.element("errcode", 102);
					jo.element("errmsg", "修改失败！"); 
				}
			}else{
				jo.element("errcode", 101);
				jo.element("errmsg", "没有权限！"); 
			}
			out.print(jo);
			out.flush();
			out.close();
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				doPost(request, response);
		}
}

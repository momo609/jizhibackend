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
 * 申请兑换礼物
 * @author Administrator
 *
 */

public class ChangeGiftServlet extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
//			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			int giftid = Integer.parseInt(request.getParameter("giftid")) ;
			int numbers = Integer.parseInt(request.getParameter("numbers")) ;
			
			GiftDaoImpl giftdao = new GiftDaoImpl();
			Gift gift = giftdao.getGiftById(giftid);
			int price = gift.getPrice();
			UserDaoImpl userdao = new UserDaoImpl();
			User user = userdao.findUser(userid);
			int userCoins = user.getCoins();
			
			JSONObject jo=new JSONObject();
			PrintWriter out = response.getWriter();
			
			if(userCoins<numbers*price){
				jo.element("errcode", 101);
				jo.element("errmsg", "积分不足以兑换所选商品"); 
			}else{
				//先将用户积分扣除相应积分值,并更新商品数量
				int currCoins = userCoins - numbers*price;
				int num = gift.getNumbers() - numbers;
				if(giftdao.updategiftNumbers(num, giftid,currCoins,userid)!=0){  //更新成功
					//发消息给老师
					int ownerid = gift.getOwnerid();
					giftdao.addApply(userid, giftid, numbers, ownerid);
					userdao.addCoinMessage(userid, 1, numbers*price);
					jo.element("errcode", 100);
					jo.element("errmsg", "兑换成功");
					
				}else{
					jo.element("errcode", 102);
					jo.element("errmsg", "积分扣取失败");
				}
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

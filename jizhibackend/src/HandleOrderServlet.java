import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.Order;
import com.jizhibackend.bean.User;
import com.jizhitest.service.GiftDaoImpl;
import com.jizhitest.service.OrderDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class HandleOrderServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gbk");
		request.setCharacterEncoding("gbk");
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		int giftId=Integer.parseInt(request.getParameter("giftid"));
		String address=request.getParameter("address");
		String receiver=request.getParameter("receiver");
		String phoneNum=request.getParameter("phoneNum");
		GiftDaoImpl giftDaoImpl=new GiftDaoImpl();
		Gift gift=giftDaoImpl.getGiftById(giftId);
		if(user!=null)
		if(user.getCoins()>gift.getPrice())
		{
			user.setCoins(user.getCoins()-gift.getPrice());
			UserDaoImpl userDaoImpl=new UserDaoImpl();
			userDaoImpl.updateUser(user);
			Order order=new Order();
			order.setGift_id(gift.getGift_id());
			order.setAddress(address);
			order.setPhone_num(phoneNum);
			order.setReceiver(receiver);
			order.setUser_id(user.getUserid());
			OrderDaoImpl orderDao=new OrderDaoImpl();
			orderDao.addOrder(order);
			out.print("SUCCESS");
		}
		else
		{
			out.print("COINS_NOT_ENOUGH");
		}
		out.flush();
		out.close();
	}
}

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.Gift;
import com.jizhitest.service.GiftDaoImpl;

public class AddproductServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String path = request.getParameter("picture");
		String name = request.getParameter("name");
		int price = Integer.parseInt(request.getParameter("price"));
		int numbers = Integer.parseInt(request.getParameter("numbers"));
		int classes = Integer.parseInt(request.getParameter("classes"));
		int userid = Integer.parseInt(request.getParameter("userid"));
		
		Gift gift = new Gift();
		gift.setClass_id(classes);
		gift.setDp_url(path);
		gift.setName(name);
		gift.setNumbers(numbers);
		gift.setPrice(price);
		gift.setOwnerid(userid);
		
		GiftDaoImpl dao = new GiftDaoImpl();
		if (dao.addGift(gift)) {
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		} else {
			out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
		}
		
		out.flush();
		out.close();
	}
}

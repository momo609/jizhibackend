import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.Gift;
import com.jizhibackend.bean.User;
import com.jizhitest.service.GiftDaoImpl;
public class DeleteGiftServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		int giftid=Integer.parseInt(request.getParameter("giftid"));
	    User user = (User) request.getSession().getAttribute("user");
	    int userid = Integer.parseInt(request.getParameter("userid")) ;
	    GiftDaoImpl giftdao = new GiftDaoImpl();
	    Gift gift = giftdao.getGiftById(giftid);
	    if(gift.getOwnerid()==userid){
	    	if(giftdao.deleteGift(giftid)){
	    		out.print("{\"errcode\":100,\"errmsg\":\"success\"}");
	    	}else{
	    		out.print("{\"errcode\":102,\"errmsg\":\"操作失败\"}");
	    	}
	    }else{
	    	out.print("{\"errcode\":101,\"errmsg\":\"没有权限\"}");
	    }
		out.flush();
		out.close();
	}

}

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jizhitest.service.GiftDaoImpl;
/**
 * ����������ϢΪ�Ѷ�
 * @author Administrator
 *
 */

public class UpdateGiftMessageServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		GiftDaoImpl giftdao = new GiftDaoImpl();
		
		PrintWriter out = response.getWriter();
		if(giftdao.updateGiftMessage(userid)!=0){
			out.print("{\"errcode\":100,\"errmsg\":\"success\"}");
		}else{
			out.print("{\"errcode\":101,\"errmsg\":\"����ʧ��\"}");
		}
		out.flush();
		out.close();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}
}

/**
 * ��������Ƿ����
 * @author Wang Junbin
 *
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jizhitest.service.PaperDaoImpl;


public class ChangeShareServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		int paperid = Integer.parseInt(request.getParameter("paperid")) ;
		int isShare = Integer.parseInt(request.getParameter("share")) ;//0��˽�ܣ�1�Ƿ���
		PaperDaoImpl papdao = new PaperDaoImpl();
	   if((papdao.changeShare(isShare,userid, paperid))!=0)
	   {
		   out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	   }
		out.close();
	}

}

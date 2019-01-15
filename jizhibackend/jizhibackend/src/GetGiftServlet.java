//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import com.jizhitest.service.GiftDaoImpl;
//
//
//public class GetGiftServlet extends HttpServlet {
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//    doPost(request, response);
//	}
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//	
//		PrintWriter out = response.getWriter();
//		GiftDaoImpl daoImpl=new GiftDaoImpl();
//		List list=daoImpl.getGift();
//		JSONArray ja=JSONArray.fromObject(list);
//		out.print(ja);
//		out.flush();
//		out.close();
//	}
//}

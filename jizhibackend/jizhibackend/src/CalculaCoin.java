//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import net.sf.json.JSONObject;
//
//import com.jizhibackend.bean.TestResult;
//import com.jizhibackend.bean.User;
//import com.jizhitest.service.MyTestDaoImpl;
//import com.jizhitest.service.TestResultDaoImpl;
//import com.jizhitest.service.UserDaoImpl;
//
//
//public class CalculaCoin extends HttpServlet {
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		PrintWriter out = response.getWriter();
//	    int userid=Integer.parseInt(request.getParameter("userid"));
//	    MyTestDaoImpl testdao = new MyTestDaoImpl();
//	    List<Integer> testList = testdao.findTestByOwnerid(userid);
////	    System.out.println("¹þ¹þ¹þ£º"+testList.toString());
//	    
//		UserDaoImpl dao = new UserDaoImpl();
//	    TestResultDaoImpl impl=new TestResultDaoImpl();
//	    for (int i = 0; i < testList.size(); i++) {
//	    	List<TestResult> t = impl.findTestResultByTestid(testList.get(i));
//	    	for (int j = 0; j <t.size(); j++) {
////	    		System.out.println(t.get(j));
//	    		User user = dao.findUser(t.get(j).getStudentid());
////				int k = dao.updateCoins(t.get(j).getScore()+user.getCoins(), t.get(j).getStudentid());
//	    		int k = dao.addCoinMessage(t.get(j).getStudentid(), 0, user.getCoins());
//	    		System.out.println(k);
//			}
//		}
//	    JSONObject jo=new JSONObject();
//	    jo.element("errcode", 0);
//	    jo.element("errmsg", "success");
//	    out.print(jo);
//		out.flush();
//		out.close();
//	}
//
//}

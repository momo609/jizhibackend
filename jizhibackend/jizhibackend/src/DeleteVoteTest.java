import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.MyVoteTestDaoImpl;
import com.jizhitest.service.UserDaoImpl;
public class DeleteVoteTest extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		int votetestid=Integer.parseInt(request.getParameter("votetestid"));
	    MyVoteTestDaoImpl maDaoImpl=new MyVoteTestDaoImpl();
	   if(maDaoImpl.deleteVoteTestById(votetestid)!=0)
	   {
			out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	   }else
	   {
			out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
	   }
		out.flush();
		out.close();
	}

}

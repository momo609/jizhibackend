import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogoutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		        request.setCharacterEncoding("UTF-8");
		        response.setContentType("text/html;charset=UTF-8");
		        PrintWriter out = response.getWriter();
		        request.getSession().removeAttribute("user");
		        out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	}

}

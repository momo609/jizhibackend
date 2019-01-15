import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TestServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest requset, HttpServletResponse response)
			throws ServletException, IOException {
		    super.service(requset, response);
		
			String c=(String)requset.getSession().getAttribute("user");
		
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(c);
			out.flush();
			out.close();
	}

}

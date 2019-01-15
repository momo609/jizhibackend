import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jizhitest.service.PaperDaoImpl;




public class AddExistQtoPaperServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");	
		PrintWriter out = response.getWriter();
		String qids=request.getParameter("qids");
		int order=Integer.parseInt(request.getParameter("order"));
		int paperid=Integer.parseInt(request.getParameter("paperid"));
		Set<Integer> qset;
		Gson gson=new Gson();
		qset=gson.fromJson(qids, new TypeToken<Set<Integer>>() {  
        }.getType());
		
		PaperDaoImpl paperDaoImpl=new PaperDaoImpl();
		if(paperDaoImpl.addExistQuestion(paperid, qset, order)!=0)
		    out.print("success");
		else
			out.print("error");
		out.flush();
		out.close();
	}
}

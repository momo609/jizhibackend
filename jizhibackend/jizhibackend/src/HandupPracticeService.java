import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.PracticeResult;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PracticeDaoImpl;


public class HandupPracticeService extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gbk");
		request.setCharacterEncoding("gbk");	
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		String paperid=(String)request.getParameter("paperid");
        String answers=(String)request.getParameter("answers");
        String answerTrace=(String)request.getParameter("answertrace");
        String timeused=(String)request.getParameter("timeused");
        String totaltime=(String)request.getParameter("totaltime");
        String lookbackCount=(String)request.getParameter("lookbackcount");
        PracticeResult t=new PracticeResult();
        t.setPaper_id(Integer.parseInt(paperid));
        t.setAnswers(answers);
        t.setStu_id(user.getUserid());
        t.setAnswer_trace(answerTrace);
        t.setLook_back_times(lookbackCount);
        t.setTime_used(timeused);
        t.setTotal_time_used(Long.parseLong(totaltime));
        PracticeDaoImpl pImpl=new PracticeDaoImpl();
        
       if(pImpl.addPracticeResult(t)!=0)
       {
    	  out.print("success");
       }else
       {
    	   out.print("error");
       }
		out.flush();
		out.close();
	}
}

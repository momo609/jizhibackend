import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.User;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class PersonalizedQuestionServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
      
		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		User user=(User)request.getSession().getAttribute("user");
		UserDaoImpl userdao=new UserDaoImpl();
		if(user==null)
		{
			out.print("sessionexpired");
		}else
		{
		 QuestionDaoImpl qImpl=new QuestionDaoImpl();
		 Map map=qImpl.getRecommendMap(user.getUserid());
		 JSONObject jo=JSONObject.fromObject(map);
		 out.print(jo);
		 System.out.println(jo);
		}
		out.flush();
		out.close();
	}
}

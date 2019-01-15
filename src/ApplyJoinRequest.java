import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.*;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.Student;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * 申请加入班级
 * @author Administrator
 *
 */

public class ApplyJoinRequest extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			UserDaoImpl dao1 = new UserDaoImpl();
			if((user==null))
				user = dao1.findUser(userid);
			int classid=Integer.parseInt(request.getParameter("classid"));
			MyClassDaoImpl dao=new MyClassDaoImpl();
			MyClass myclass=dao.findClass(classid);
			UserDaoImpl userdao=new UserDaoImpl();
			//找到班级号下的所有已加入学生
			List<Student> list = dao.getClassMembers(classid);
			//判断是否已存在该学生
			int flag=0;
			for (Student student : list) {
				if(student.getId()==userid){
					flag =1;
					break;
				}
			}
			//判断是否重复申请
			int i = dao.searchApply(classid,userid);
			PrintWriter out = response.getWriter();
			JSONObject jo=new JSONObject();
			if(flag!=0){
				jo.element("errcode", 101);
			}else if(i>0){
				jo.element("errcode", 102);
			}
			else{
				boolean b = dao.applyjoinClass(classid, userid);
				if(b)
				{
					jo.element("errcode", 0);
				}else
				{
					jo.element("errcode", 104);
				}
			}
			out.print(jo);
			out.flush();
			out.close();
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				doPost(request, response);
		}
}

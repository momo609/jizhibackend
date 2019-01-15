import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 获取加入班级处理信息
 * @author Administrator
 *
 */

public class GetMessageRequest extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid"));
			UserDaoImpl dao1 = new UserDaoImpl();
			if((user==null))
				user = dao1.findUser(userid);
			
			//通过userid获取所申请加入班级的处理结果
			MyClassDaoImpl dao=new MyClassDaoImpl();
			Map<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
			map = dao.getApplyResultClass(userid);
			ArrayList<Integer> classid = map.get("classid");
			ArrayList<String> className = new ArrayList<String>();
			for (Integer integer : classid) {
				className.add(dao.findClass(integer).getName());
			}
			ArrayList<Integer> result = map.get("result");
			ArrayList<String> classid_agree = new ArrayList<String>();//同意被加入的班级号
			ArrayList<String> classid_refuse = new ArrayList<String>();//拒绝被加入的班级号
			for (int i=0;i<result.size();i++) {
				if(result.get(i)==1){
					classid_agree.add(className.get(i));
				}else if(result.get(i)==2){
					classid_refuse.add(className.get(i));
				}
			}
			PrintWriter out = response.getWriter();
			JSONObject jo = new JSONObject();
			jo.element("agree", classid_agree);
			jo.element("refuse", classid_refuse);
			out.print(jo);
			out.flush();
			out.close();
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				doPost(request, response);
		}
}

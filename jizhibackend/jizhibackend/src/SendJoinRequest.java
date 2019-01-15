import java.io.IOException;
import java.io.PrintWriter;

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
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class SendJoinRequest extends HttpServlet {

	
	  public static final String MASTER_SECRET="694d18ad233b96d597e47be2";
	    public static final String APP_KEY="f8f40a234cb34c0148da3bad";
	    private boolean isok=true;
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
	
		
			
			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			UserDaoImpl dao1 = new UserDaoImpl();
			if((user==null))
				user = dao1.findUser(userid);
			int classid=Integer.parseInt(request.getParameter("classid"));
			
			String extraMsg=request.getParameter("extraMsg");
			MyClassDaoImpl dao=new MyClassDaoImpl();
			MyClass myclass=dao.findClass(classid);
			UserDaoImpl userdao=new UserDaoImpl();
			
			
			
			JSONObject reqJO=new JSONObject();
			reqJO.element("user_id", user.getUserid());
			reqJO.element("nickname", user.getNickname());
			reqJO.element("class_id", classid);
			reqJO.element("class_name", myclass.getName());
			reqJO.element("userDP", user.getDp_url());
			reqJO.element("extraMsg",extraMsg );
			String requestmsg=reqJO.toString();
			JPushClient client=new JPushClient(MASTER_SECRET, APP_KEY);
			Message msg=Message.newBuilder().setMsgContent(requestmsg).addExtra("application_info", reqJO.toString()).build();
			System.out.println(reqJO.toString());
			PushPayload payload=PushPayload.newBuilder().setPlatform(Platform.android())
					.setAudience(Audience.alias("user"+myclass.getOwner()))
					.setMessage(msg).build();
			try {
				client.sendPush(payload);
			} catch (APIConnectionException e) {
				isok=false;
			} catch (APIRequestException e) {
				isok=false;
			}
			
			PrintWriter out = response.getWriter();
			if(isok)
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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * �����������༶��Ϣ
 * @author Administrator
 *
 */

public class ApplyJoinClassServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int classid=Integer.parseInt(request.getParameter("classid"));
		int result=Integer.parseInt(request.getParameter("result"));
		int userid = Integer.parseInt(request.getParameter("userid")) ;
		int studentid = Integer.parseInt(request.getParameter("studentid")) ;
		UserDaoImpl dao1 = new UserDaoImpl();
		User user = dao1.findUser(userid);
		MyClassDaoImpl dao=new MyClassDaoImpl();
		JSONObject jo=new JSONObject();
		String  name = dao1.findUser(studentid).getNickname();
		if(name==null){
			name = "def";
		}
		if(result==1){//ͬ��
			if(dao.addClassMember(classid, studentid, name)){
					dao.setJoinClassResult(classid, studentid, result);
					jo.element("errcode", 0);//�����ɹ����Ѽ���ð༶
				}else{
					jo.element("errcode", 104);//����ʧ�ܣ���Ҫ���²���
				}
		}else if(result==2){//�ܾ�
			int i = dao.setJoinClassResult(classid, studentid, result);
			jo.element("errcode", 0);//�����ɹ����Ѿܾ�
		}
		PrintWriter out = response.getWriter();
		out.print(jo);
		out.flush();
		out.close();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

}

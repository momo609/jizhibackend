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

import net.sf.json.JSONArray;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.UserDaoImpl;
/**
 * 获取申请加入班级信息
 * @author Administrator
 *
 */

public class GetApplyJoinRequest extends HttpServlet {
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			User user=(User)request.getSession().getAttribute("user");
			int userid = Integer.parseInt(request.getParameter("userid")) ;
			UserDaoImpl dao1 = new UserDaoImpl();
			if((user==null))
				user = dao1.findUser(userid);
			
			//通过userid获取所管理的班级id
			List<MyClass> listClass = new ArrayList<MyClass>();
			MyClassDaoImpl dao=new MyClassDaoImpl();
			listClass = dao.ClassofOwner(userid);
			List<Integer> listClassid = new ArrayList<Integer>();
			for (MyClass myClass : listClass) {
				listClassid.add(myClass.getId());
			}
			
			//通过classid获取所有的请求信息
//			ArrayList<ArrayList<String>> lastList = new ArrayList<ArrayList<String>>();
			Map<String,ArrayList<String>> lastList = new HashMap<String,ArrayList<String>>();
//			ArrayList<ArrayList<Integer>> lastListId = new ArrayList<ArrayList<Integer>>();
			UserDaoImpl userDao = new UserDaoImpl();
			for (Integer integer : listClassid) {
				ArrayList<Integer> listStudentid = dao.getApplyjoinClass(integer);
				ArrayList<String> listStudent = new ArrayList<String>();
				for (Integer integer2 : listStudentid) {
					
					listStudent.add(integer2+"@"+userDao.findUser(integer2).getNickname());
				}
//				listStudent.add(integer+"");
//				listStudentid.add(integer);
				if (listStudent.size()>0) {
					lastList.put(integer+"", listStudent);
				}
//				lastList.add(listStudent);
//				lastListId.add(listStudentid);
			}
			PrintWriter out = response.getWriter();
			JSONArray jo=JSONArray.fromObject(lastList);
//			System.out.println("jo:"+jo);
//			JSONArray jo1=JSONArray.fromObject(lastListId);
//			System.out.println("jo1:"+jo1);
			out.print(jo);
//			out.print(jo1);
			out.flush();
			out.close();
		}
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				doPost(request, response);
		}
}

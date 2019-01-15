import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.Student1;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyClassDaoImpl;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class ImportStudentServlet extends HttpServlet {

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");	
		User cuser=(User)request.getSession().getAttribute("user");
		PrintWriter out = response.getWriter();
		 String acceptjson = "";  
	        try {  
	            BufferedReader br = new BufferedReader(new InputStreamReader(  
	                    (ServletInputStream) request.getInputStream(), "UTF-8"));  
	            StringBuffer sb = new StringBuffer("");  
	            String temp;  
	            while ((temp = br.readLine()) != null) {  
	                sb.append(temp);  
	            }  
	            br.close();  
	            acceptjson = sb.toString();  
	        }catch (Exception e) {
				// TODO: handle exception
			}
	        System.out.println(acceptjson);
	        JSONObject jo=JSONObject.fromObject(acceptjson);
	        int classid=jo.getInt("classid");
	        JSONArray ja=JSONArray.fromObject(jo.get("Students"));
	        List<Student1> stulist=ja.toList(ja, Student1.class);
	        UserDaoImpl userimpl=new UserDaoImpl();
	        MyClassDaoImpl myclassdao=new MyClassDaoImpl();
	        for(Student1 s:stulist)
	        {
	        	User user=null;
	    		if((user=userimpl.findUser(s.getId()+""))==null)
	    		{
	    			user=new User();
		    		user.setUsername(s.getId()+"");
		    		user.setPassword("123456");
		    		user.setNickname(s.getName());
		    	    user.setUsertype(1);
		    	    user.setClassname(s.getClassName());
	    			boolean flag=userimpl.registerUser2(user);
	    			if(flag)
	    			{
	    				myclassdao.addClassMember(classid, user.getUserid(), s.getClassName()+"-"+s.getName());
	    				out.print(0);
	    			}
	    			else
	    			{
	    				out.print(104);
	    			}
	    		}
	    		else 
	    		{
	    			if(myclassdao.addClassMember(classid, user.getUserid(), s.getClassName()+"-"+s.getName())){
	    				out.print(0);
	    			}else{
	    				out.print(104);
	    			}
	    			
	    		} 
	        }
//	    out.print("success");
	        
		out.flush();
		out.close();
	}

}

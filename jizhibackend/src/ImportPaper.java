import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jizhibackend.bean.FillBlankQuestion;
import com.jizhibackend.bean.JudgeQuestion;
import com.jizhibackend.bean.MultipleChoiceQuestion;
import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.UserDaoImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ImportPaper extends HttpServlet {
	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");	
//		User user=(User)request.getSession().getAttribute("user");
		PrintWriter out = response.getWriter();
//		int id = Integer.parseInt(request.getParameter("id")) ;
//		int userid = Integer.parseInt(request.getParameter("userid")) ;
		UserDaoImpl dao = new UserDaoImpl();
//		String title = request.getParameter("title") ;
//		PaperDaoImpl pa = new PaperDaoImpl();
//		Paper paper=new Paper();
//		paper.setTitle(title);
//		String content = pa.findPaperContent(id);
		
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
	            
//	            System.out.println("222:"+acceptjson);
		        JSONObject jo=JSONObject.fromObject(acceptjson);
//		        System.out.println("jo:"+jo.toString());
		        Paper paper=new Paper();
		        paper.setTitle(jo.get("title").toString());
		        int userid = Integer.parseInt(jo.get("userid").toString()) ;
//		        User user = dao.findUser(userid);
		        paper.setOwner(userid);
		        paper.setCreatetime(System.currentTimeMillis());
		        JSONArray jaSCQ=JSONArray.fromObject(jo.get("SingleChoiceQuestion").toString());
		        List<SingleChoiceQuestion> scqlist=jaSCQ.toList(jaSCQ, SingleChoiceQuestion.class);
		        JSONArray jaMCQ=JSONArray.fromObject(jo.get("MultipleChoiceQuestion").toString());
		        List<MultipleChoiceQuestion> mcqlist=jaMCQ.toList(jaMCQ, MultipleChoiceQuestion.class);
		        JSONArray jaFBQ=JSONArray.fromObject(jo.get("FillBlankQuestion").toString());
		        List<Question> fbqlist=jaFBQ.toList(jaFBQ, Question.class);
		        JSONArray jaJQ=JSONArray.fromObject(jo.get("JudgeQuestion").toString());
		        List<Question> jqlist=jaJQ.toList(jaJQ, Question.class);
		        paper.setQuestion_num(0) ;
		        PaperDaoImpl daoImpl=new PaperDaoImpl();
		        daoImpl.createPaper(paper);
		        daoImpl.addSingleChoiceQustionList(paper, scqlist);
		        daoImpl.addMultipleChoiceQustionList(paper, mcqlist);
		        daoImpl.addQustionList(paper, fbqlist);
		        daoImpl.addQustionList(paper, jqlist);
	        }catch (Exception e) {
	        	out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
	        	System.out.println("´íÎó£º"+e.toString());
			}
	        out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	       
	        
	 
	        
	        
		out.flush();
		out.close();
	}


}

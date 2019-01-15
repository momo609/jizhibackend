import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jizhibackend.bean.MultipleChoiceQuestion;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.SingleChoiceQuestion;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;


public class updateQuestionServlet extends HttpServlet {

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user=(User)request.getSession().getAttribute("user");
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
	        JSONObject jo=JSONObject.fromObject(acceptjson);
	        JSONObject joscq= jo.getJSONObject("question");
	        System.out.println(joscq);
	        MultipleChoiceQuestion q=(MultipleChoiceQuestion) joscq.toBean(joscq, MultipleChoiceQuestion.class);
	        SingleChoiceQuestion scq=(SingleChoiceQuestion) joscq.toBean(joscq, SingleChoiceQuestion.class);
	        Question qq=(Question) joscq.toBean(joscq, Question.class);
	        int paperid=jo.getInt("paperid");
	        int qtype=jo.getInt("qtype");
	        QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
	        if(questionDaoImpl.updateQuestion(qq,paperid))
	        	out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	        else
	        	out.print("{\"errcode\":104,\"errmsg\":\"ÏµÍ³´íÎó\"}");
	        
	}
	

}

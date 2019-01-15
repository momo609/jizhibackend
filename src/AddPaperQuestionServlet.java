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


public class AddPaperQuestionServlet extends HttpServlet {


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");	
//		User user=(User)request.getSession().getAttribute("user");
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
	        System.out.println(jo);
	        JSONObject joscq= jo.getJSONObject("question");
	        System.out.println(joscq);
	        MultipleChoiceQuestion q=(MultipleChoiceQuestion) joscq.toBean(joscq, MultipleChoiceQuestion.class);
	        SingleChoiceQuestion scq=(SingleChoiceQuestion) joscq.toBean(joscq, SingleChoiceQuestion.class);
	        int paperid=jo.getInt("paperid");
	        int flag=jo.getInt("flag");
	        //�õ�������Ŀ����
	        PaperDaoImpl paperdao=new PaperDaoImpl();
	        int num = paperdao.getPaperByid(paperid).getQuestion_num();
	        scq.setQ_order(num+1);
	        q.setQ_order(num+1);
	        int qtype=jo.getInt("qtype");
	        
	        PaperDaoImpl daoimpl=new PaperDaoImpl();
	        if(qtype==1)//��ѡ��
	        {
//	        	System.out.println("��ѡ˳��"+scq.getQ_order());
		        if(daoimpl.addChoiceQustion(paperid, scq,flag)!=0)
		        {
		        	out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		        }else
		        {
		        	out.print("{\"errcode\":104,\"errmsg\":\"ϵͳ����\"}");
		        }
	        }else if(qtype==4)//��ѡ��
	        {
//	        	System.out.println("��ѡ˳��"+q.getQ_order());
	        	if(daoimpl.addChoiceQustion(paperid, q,flag)!=0)
		        {
	        		out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
		        }else
		        {
		        	out.print("{\"errcode\":104,\"errmsg\":\"ϵͳ����\"}");
		        }
	        }
	        else
	        { 
	        	  Question qq=(Question) joscq.toBean(joscq, Question.class);
//	        	  System.out.println("����˳��"+qq.getQ_order());
	        	  qq.setType(qtype);
	        	  daoimpl.addQuestion(paperid, qq, scq.getQ_order(),flag);
	        	  out.print("{\"errcode\":0,\"errmsg\":\"success\"}");
	        		
	        }
	        
	}

}

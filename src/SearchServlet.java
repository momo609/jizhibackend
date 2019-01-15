import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import util.LuceneUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.service.PaperDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;


public class SearchServlet extends HttpServlet 
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		User user=(User)request.getSession().getAttribute("user");
		PrintWriter out = response.getWriter();
		String keyword=request.getParameter("keyword");
		String type =request.getParameter("type");
		System.out.println(keyword);
		String searchResult=null;
		if(type.equals("paper"))
		{
		PaperDaoImpl daoImpl=new PaperDaoImpl();
		List<Integer> ids =null;
		try {
			ids=new LuceneUtil(LuceneUtil.pIndexpath).searchByTerm("title", keyword, 100);
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 JSONObject jo=new JSONObject();
		  if(ids.size()>=1)
		   {
		   java.util.List<Paper> list=daoImpl.getPaperByids(ids);
		   JSONArray ja=JSONArray.fromObject(list);
		   jo.put("papers", ja);
		  }else
		  {
			  jo.put("papers", "[]");
		  }
		  searchResult=jo.toString();
		}
		else if(type.equals("question"))
		{
			QuestionDaoImpl daoImpl=new QuestionDaoImpl();
			List<Integer> idlist =null;
			List<Integer> idlist2 =null;
			try {
				LuceneUtil util=new LuceneUtil(LuceneUtil.qIndexpath);
				idlist=util.searchByTerm("stem", keyword, 100);
				idlist2=util.searchByTerm("tag", keyword, 100);
			} catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 JSONObject jo=new JSONObject();
			  if(idlist2.size()>=1||idlist.size()>=1)
			   {
				  Set<Integer> ids=new HashSet();
				  for(int i:idlist2)
				  {
					  ids.add(i);
				  }
				  for(int i:idlist)
				  {
					  ids.add(i);
				  }
			   List<Question> list=daoImpl.getQuestionsByids(ids);
			   JSONArray ja=JSONArray.fromObject(list);
			   jo.put("questions", ja);
			  }else
			  {
				  jo.put("questions", "[]");
			  }
			  searchResult=jo.toString();
		}
		out.write(searchResult);
		System.out.println(searchResult);
		out.flush();
		out.close();
	}


}

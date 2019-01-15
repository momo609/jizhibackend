package test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jizhibackend.bean.Question;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.RecommendDaoImpl;

public class handledata {
public static void main(String args[]) throws SQLException, FileNotFoundException{
	 RecommendDaoImpl myrecommend=new RecommendDaoImpl();
//	 myrecommend.addhandledata(1993917879);
	    ArrayList<String> recommendlist=new ArrayList();
	    QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
	    List<Question>fqlist=new ArrayList<Question>();
	    Map<String,List<Question>> rlist=new HashMap();
			recommendlist=myrecommend.recommendforstudent(1333, 10156);
			System.out.println("size "+recommendlist.size());
			for(int j=0;j<recommendlist.size();j++)
			{
				System.out.println(recommendlist.get(j));
				List<Question> qlist=questionDaoImpl.getQuestionsofconcepts(recommendlist.get(j));
				
					for(int i=0;i<qlist.size();i++)
					{
						fqlist.add(qlist.get(i));  //ÍÆ¼öÏ°Ìâ¼¯
						System.out.println(qlist.get(i).getStem()+" "+qlist.get(i).getChoices()+" "+qlist.get(i).getAnswer());
					}
			}
}
}

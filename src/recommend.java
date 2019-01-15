import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.jizhibackend.bean.Question;
import com.jizhitest.service.QuestionDaoImpl;


public class recommend {
public static void main(String args[]) throws UnsupportedEncodingException, IOException{
	QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
	String concepts[]={"码", "非主属性", "2NF", "3NF"};//1333
   //String concepts2[]={"码", "传递函数依赖", "1NF", "部分函数依赖", "决定因素", "非主属性"};//1335
	//String concepts2[]={"码", "传递函数依赖", "2NF", "BCNF", "非主属性", "3NF", "部分函数依赖", "决定因素"};//1336
	//String concepts[]={"函数依赖", "码", "3NF", "传递函数依赖", "部分函数依赖"};//1337
	//String concepts[]={"1NF", "码", "2NF", "BCNF", "传递函数依赖", "部分函数依赖"};//1338
	//String concepts2[]={"传递函数依赖", "决定因素", "函数依赖", "码"};//1339
      //String concepts[]={"3NF", "函数依赖", "主属性", "码", "2NF"};//1340s
	//String concepts[]={"传递函数依赖", "决定因素", "函数依赖", "码", "码", "1NF"};//1343
	//String concepts2[]={"1NF", "2NF", "BCNF", "传递函数依"2NF", "BCNF", "非主属性"};//1341
	//String concepts2[]={"码", "部分函数依赖", "2NF"};//1342
	//String concepts2[]={"传递函数依赖", "决定因素", "函数依赖", 赖", "部分函数依赖", "函数依赖"};//1344
    // String concepts2[]={"函数依赖", "码", "BCNF", "传递函数依赖", "部分函数依赖", "3NF", "非主属性"};//1345
	List<Integer>wqlist=questionDaoImpl.WrongQuestions(1343,1993917879);
//	List<Integer>fqlist=questionDaoImpl.WrongQuestions(1343, 1993917879);
//	List<Integer>fqlist2=questionDaoImpl.WrongQuestions(1342, 1993917879);
	List<Integer>fqlist=new ArrayList<Integer>();
	List<Integer>fqlist2=new ArrayList<Integer>();
	List<Integer> list=questionDaoImpl.getQuestion(155);
	double count=0;
	double sum=0;
	double sum1=0;
	List<Integer> rlist=new ArrayList<Integer>();
	List<Integer> rlist2=new ArrayList<Integer>();
	for(int j=0;j<concepts.length;j++)
	{
		System.out.println(concepts[j]);
		List<Question> qlist=questionDaoImpl.getQuestionsofconcepts(concepts[j]);
		
			for(int i=0;i<qlist.size();i++)
			{
//				if(wqlist.contains(qlist.get(i).getQuestionid()))
//				{
//					count++;
//				}
				fqlist.add(qlist.get(i).getQuestionid());  //推荐习题集
				System.out.println(qlist.get(i).getStem()+" "+qlist.get(i).getChoices()+" "+qlist.get(i).getAnswer());
			}
			sum=sum+qlist.size();
	}
//	for(int j=0;j<concepts2.length;j++)
//	{
//		//System.out.println(concepts2[j]);
//		List<Question> qlist2=questionDaoImpl.getQuestionsofconcepts(concepts2[j]);
//		
//			for(int i=0;i<qlist2.size();i++)
//			{
//				
//				fqlist2.add(qlist2.get(i).getQuestionid());
//				//System.out.println(qlist2.get(i).getStem()+" "+qlist2.get(i).getChoices()+" "+qlist2.get(i).getAnswer());
//			}
//			sum1=sum1+qlist2.size();
//	}
//	int cc=0;
//	for(int i=0;i<fqlist2.size();i++)
//	{
//		if(fqlist.contains(fqlist2.get(i)))
//		{
//			cc++;
//		}
//	}
//	System.out.println("cc"+cc);
	 //System.out.println(wqlist.toString());
	int rcount=0;
//	System.out.println("22 "+ fqlist.size()+" "+fqlist2.size());
//	for(int j=0;j<sum1;j++)
//	{
//		int r = (int) (Math.random() * (sum-1)); 
//		if(r<list.size())
//		{
//			if(!rlist2.contains(list.get(r)))
//		{
//			//System.out.print(r+" ");
//			rlist2.add(list.get(r));
//		}
//		}
//			
//	}
//	for(int j=0;j<sum;j++)
//	{
//		int r = (int) (Math.random() * (sum-1)); 
//		if(r<list.size())
//		{
//			if(!rlist.contains(list.get(r)))
//		{
//			System.out.print(r+" ");
//			rlist.add(list.get(r));
//			
//		}
//
//		}
//			
//	}
//	int cc2=0;
//	for(int i=0;i<rlist2.size();i++)
//	{
//		if(rlist.contains(rlist2.get(i)))
//		{
//			cc2++;
//		}
//	}
//	System.out.println();
//	System.out.println("cc2 "+cc2);
//	 //System.out.println(wqlist.toString());
//	System.out.println("22 "+ rlist.size()+" "+rlist2.size());
////	System.out.println(rlist.toString());
////	System.out.println(count);
////	System.out.println("rcount "+rcount);
//	//System.out.println(sum);
//	double recall=sum/(sum+wqlist.size()-count);
//	double rrecall=sum/(sum+wqlist.size()-rcount);
//	double precison=(sum-11)/sum;
//	double rprecison=rcount/sum;
//	System.out.println("r "+sum/(sum+wqlist.size()-rcount));
//	System.out.println(recall);
//	System.out.println((sum-11)/sum);
//	String filename="E:/知识图谱推荐/recall.txt";
//	FileOutputStream o= new FileOutputStream(filename,true);
//	String s=""+recall+"\r\n";
//	o.write(s.getBytes("GBK"));
//	String filename2="E:/知识图谱推荐/rrecall.txt";
//	FileOutputStream o2= new FileOutputStream(filename2,true);
//	String s2=""+rrecall+"\r\n";
//	o2.write(s2.getBytes("GBK"));
//	String filename3="E:/知识图谱推荐/precision.txt";
//	FileOutputStream o3= new FileOutputStream(filename3,true);
//	String s3=""+precison+"\r\n";
//	o3.write(s3.getBytes("GBK"));
//	String filename4="E:/知识图谱推荐/rprecision.txt";
//	FileOutputStream o4= new FileOutputStream(filename4,true);
//	String s4=""+rprecison+"\r\n";
//	o4.write(s4.getBytes("GBK"));
//	//double ss=0.8311688311688312+0.8+0.6153846153846154+0.7272727272727273+0.782608695652174+0.8311688311688312+0.7352941176470589+0.7142857142857143+0.7777777777777778+0.6428571428571429+0.6944444444444444+0.8+0.8032786885245902;
//    //System.out.println(ss/13);
}
}
 
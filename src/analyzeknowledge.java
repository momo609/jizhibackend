

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;

import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.jizhibackend.bean.AllTestresult;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.service.MyTestDaoImpl;
import com.jizhitest.service.QuestionDaoImpl;
import com.jizhitest.service.TestResultDaoImpl;
import com.jizhitest.service.UserDaoImpl;


public class analyzeknowledge {
   public static void main(String args[]) throws UnsupportedEncodingException, IOException{
	   String concepts[]={"��������","���ֺ�������","���ݺ�������","1NF","2NF","3NF","BCNF","��������","��","������","��������"};
	   int question[][]=new int[56][11];
	   //String concepts[]={"ʶ��","���"};
	   String writes=null;
	   String filename=null;
	   Map<String,Integer>conceptsorder=new HashMap<String,Integer>();
	   for(int i=0;i<concepts.length;i++)
	   {
		   conceptsorder.put(concepts[i], i);
	   }
	   TestResultDaoImpl dao=new TestResultDaoImpl();
	  List<AllTestresult> testresults=dao.getalltestresult(1993917879);
	   System.out.println(testresults.toString());
	  List <Integer> avgTimeList=dao.getAvgTimeOfEachQuestion(testresults.get(1).getTestid());  //ÿ�����ƽ��ʱ��
	   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testresults.get(1).getTestid()); //ÿ�����ƽ���ؿ�����
	   int order=0;
	   FileOutputStream o2= new FileOutputStream("E:/֪ʶͼ���Ƽ�/ȫ��ʵ����/results.txt");
	   for(int j=0;j<testresults.size();j++)
	   {
		    MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
			UserDaoImpl userdao = new UserDaoImpl();
			MyTest t=myTestDaoImpl.getTestByid(testresults.get(j).getTestid());
			int paperid=t.getUse_paperid();
			QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
			List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
			List<Question>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getStudentid(), 1);
			System.out.println("11   "+testresults.get(j).getStudentid());
			System.out.println(qlist.toString());
			
			String[] answers=testresults.get(j).getAnswers().split("@@");
			String[] timeused=testresults.get(j).getTime_used().split(",");
			String[] lookbacktimes=testresults.get(j).getLook_back_times().split(",");
			String[] answertrace=testresults.get(j).getAnswer_trace().split(",");
			String[] judgetime=new String[timeused.length];
			String[] judgelookback=new String[lookbacktimes.length];
			int[] countanswer=new int[56];
			for(int i=0;i<timeused.length;i++)
			{
				judgetime[i]=judgetime(timeused[i],avgTimeList.get(i));
				judgelookback[i]=judgelookback(lookbacktimes[i],avgLookbacktimes[i]);
			}
			
			String[] finalresults=new String[qlist.size()];
			int[] judgecollect=new int[qlist.size()];
			ArrayList <Question> wrongqlist=new ArrayList<Question>();
			ArrayList <Question> rightqlist=new ArrayList<Question>();
//			for(int i=0;i<qlist.size();i++)
//			{
//				System.out.println(qlist.get(i).toString());
//			}
			
			for(int i=0;i<qlist.size();i++)
			{
				String id=qlist.get(i).getId()+"";
				if(collectlist.toString().indexOf(id)>=0)
				{
					judgecollect[i]=1;
				}
				else
				{
					judgecollect[i]=0;
				}
				String correct = qlist.get(i).getAnswer().trim();
				String stuAnswer = answers[i].trim();
				//�����������и�����Ҫ���������ʣ���׼�������ÿ��������һ���ո������������������ô���һ���ո��������ʱӦ���ȶ�����������д���ȥ������Ŀո�
				stuAnswer=stuAnswer.replaceAll("\\s+"," ");//ֻȥ���м����ո� \\s+��ʾ����ո�
				//countanswer[i]=count(stuAnswer,correct);
				System.out.println(count(stuAnswer,correct));
				if(correct.equalsIgnoreCase(stuAnswer))
				{
					finalresults[i]="1";
					rightqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;
					else
						countanswer[i]=1;
				}else{//��������ӵ������б���ȥ
					finalresults[i]="0";
					wrongqlist.add(qlist.get(i));
					if(count(stuAnswer,correct)!=0)
						countanswer[i]=3;
					else
						countanswer[i]=2;
				}
				
			}
			
			for(int i=0;i<qlist.size();i++)
			{
				String s=qlist.get(i).getTag();
				
				s.replaceAll("\\?", " "); 
				s=s.trim();
				System.out.println(qlist.get(i).getTag());
				
				//int conceptorder=conceptsorder.get(s);
				filename="E:/֪ʶͼ���Ƽ�/ʵ����/"+testresults.get(j).getStudentid()+"/"+s+".txt";
				FileOutputStream o= new FileOutputStream(filename,true);
//				writes=qlist.get(i).getLevel()+","+judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				writes=judgetime[i]+","+judgelookback[i]+","+countanswer[i]+","+judgecollect[i]+","+finalresults[i]+"\r\n";
				//o.write(writes.getBytes("GBK"));
				//o2.write(writes.getBytes("GBK"));
			}
			  
	   }
	   BufferedReader br = new BufferedReader(new FileReader("C:/Users/admin/Desktop/qqq.txt"));//����һ��BufferedReader������ȡ�ļ�
       String s = null;
       int i=0;
       while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
       	System.out.println(s+" "+conceptsorder.get(s));
       	question[i][conceptsorder.get(s)]=40;
       	i++;
       }
       FileOutputStream out = null;         
       
       HSSFWorkbook workbook = new HSSFWorkbook(); // ����excel�� ����Ŀ¼       
                         
       HSSFSheet sheet = workbook.createSheet("sheet1");  
       //��ӱ�ͷ 
       for(int z=0;z<56;z++)
  	  {
    	   Row row = workbook.getSheet("sheet1").createRow(z);
    	   try {              
  	           for(int q = 0;q < 11;q++){  
  	               Cell cell = row.createCell(q);  
  	               cell.setCellValue(question[z][q]);  
  	           } 
  	           out = new FileOutputStream("C:/Users/admin/Desktop/results.xls");  
  	           workbook.write(out);
  	      }catch (Exception e) {  
  	          e.printStackTrace();  
  	      }finally {    
  	          try {    
  	              out.close();    
  	          } catch (IOException e) {    
  	              e.printStackTrace();  
  	          }    
  	      }       
  		
  		// System.out.println();
  	 }
         //������һ��            
            
	
	   
   }
   public static String judgetime(String p_time,int avgtime){
	   if(Integer.parseInt(p_time)>avgtime)
		   return "0";
	   else
		   return "1";
   }
   public static String judgelookback(String p_lookback,float avglookback){
	   if(Integer.parseInt(p_lookback)>(int)avglookback)
		   return "0";
	   else
		   return "1";
   }
   public static int count(String answertrace,String rightanswer)
   {
	   int count=0;  
       //���������ÿ��Ԫ��    
       for(int i=0;i<=answertrace.length()-1;i++) {  
    	   if((i+rightanswer.length())<=answertrace.length())
           {
    		   String getstr=answertrace.substring(i,i+1);
    		   //System.out.println(getstr.equals(rightanswer));
                if(getstr.equals(rightanswer)){  
                    count++;  
               }  
           }
    	   else
    		   break;
       }  
       return count;
   }
}

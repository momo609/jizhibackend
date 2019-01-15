package com.jizhitest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jizhibackend.bean.AllTestresult;
import com.jizhibackend.bean.HandleTestResult;
import com.jizhibackend.bean.KnowledgePoint;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhitest.db.DBConn;


public class RecommendDaoImpl {
	private SqlSessionFactory sessionFactory;
	protected SqlSession session;
	private static final String resource = "conf.xml";
	private  static final String getclassdata = "select time,look_back_times,answer_trace,collect,finalresults,knowledgeid from handle_test_result,r_student_class where class_id=? AND r_student_class.student_id=handle_test_result.studentid ";
	private static final String getstudentdata="select studentid,testid,time,look_back_times,answer_trace,collect,knowledgeid from handle_test_result where studentid=?";
	private static final String addhandledata="insert into handle_test_result(testid,studentid,answer_trace,time,look_back_times,collect,finalresults,knowledgeid) values(?,?,?,?,?,?,?,?)";
	private static final String addkgdegree="insert into student_kg_degree(studentid,knowledge,degree) values(?,?,?)";
	private static final String getkpdegree="select degree,knowledge_point.knowledge FROM student_kg_degree,knowledge_point where studentid=? AND knowledge_point.knowledgeid=student_kg_degree.knowledge";
	private static final String getstudentkg="select degree,knowledge FROM student_kg_degree where studentid=?";
	private static final String updatedegree="UPDATE student_kg_degree SET degree=? WHERE studentid=? AND knowledge=?"; 
	public RecommendDaoImpl() {

        InputStream is = RecommendDaoImpl.class.getClassLoader().getResourceAsStream(resource);
         sessionFactory = new SqlSessionFactoryBuilder().build(is);
         session=sessionFactory.openSession();
	}
    
	static String results = null;
	static int[] correctcount=new int[11];	
//	static double[] correctrate=new double[11];
	static int[][] edge ={{1,1,1,0,0,0,0,0,0,0,0},
		                  {0,1,0,1,0,0,0,0,0,0,0},
		                  {0,0,1,0,1,0,0,0,0,0,0},
		                  {0,0,0,1,1,0,0,0,0,0,0},
		                  {0,0,0,0,1,1,0,0,0,0,0},
		                  {0,0,0,0,0,1,1,0,0,0,0},
		                  {0,0,0,0,0,0,1,1,0,0,0},
		                  {0,0,0,0,0,0,0,1,1,0,0},
		                  {0,0,0,0,0,0,0,0,1,1,1},
		                  {0,0,0,0,0,0,0,0,0,1,0},
		                  {0,0,0,0,0,0,0,0,0,0,1}};
	static Map<String,Integer>recommend=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommend=new HashMap<String,Double>();
	static Map<String,Integer>recommends=new HashMap<String,Integer>();
	static Map<String,Double>finalrecommends=new HashMap<String,Double>();
	static Map<String,Double>conceptsrate=new HashMap<String,Double>();
	static Map<String,Double>c=new HashMap<String,Double>();
	static ArrayList<String>result=new ArrayList<String>();
	static double[] correctrate=new double[11];
	TestResultDaoImpl dao=new  TestResultDaoImpl();
	static String concepts[]={"��������","���ֺ�������","���ݺ�������","1NF","2NF","3NF","BCNF","��������","��","������","��������"};
	//static String concepts[]={"1NF","2NF","3NF","BCNF","������","���ݺ�������","��������","��������","��","���ֺ�������","��������"};
	
	public List<KnowledgePoint> getkpdegree(int studentid){
		List<KnowledgePoint> kp=new ArrayList();
		Connection conn=DBConn.getConnection();
	    try {
			PreparedStatement ps=conn.prepareStatement(getkpdegree);
			ps.setInt(1, studentid);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
			  KnowledgePoint k=new KnowledgePoint();
			  String degree =rs.getString(1);
			  String knowledge=rs.getString(2);
			  k.setDegree(degree);
			  k.setKnowledge(knowledge);
			  kp.add(k);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kp;
	}
	
	public void addhandledata(int testid) throws SQLException{
		 
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
		  List<AllTestresult> testresults=dao.getalltestresult(testid);
		  System.out.println(testresults.size());
		  List <Integer> avgTimeList=dao.getAvgTimeOfEachQuestion(testresults.get(1).getTestid());  //ÿ�����ƽ��ʱ��
		   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testresults.get(1).getTestid()); //ÿ�����ƽ���ؿ�����
		   int order=0;
		   Connection conn=DBConn.getConnection();
	    	PreparedStatement ps=conn.prepareStatement(addhandledata);
			
		   for(int j=0;j<testresults.size();j++)
		   {
			    MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
				UserDaoImpl userdao = new UserDaoImpl();
				MyTest t=myTestDaoImpl.getTestByid(testresults.get(j).getTestid());
				int paperid=t.getUse_paperid();
				QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
				List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
				List<Question>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getStudentid(), 1);
				
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
//				for(int i=0;i<qlist.size();i++)
//				{
//					System.out.println(qlist.get(i).toString());
//				}
				
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
					//System.out.println(count(stuAnswer,correct));
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
					int knowledgeid=qlist.get(i).getKnowledgeid();
					System.out.println(testresults.get(j).getResultid()+" "+testid+" "+testresults.get(j).getStudentid()+" "+judgetime[i]+" "+judgelookback[i]+" "+countanswer[i]+" "+judgecollect[i]+" "+finalresults[i]);
					ps.setInt(1,testid);
					ps.setInt(2,testresults.get(j).getStudentid());
					ps.setString(3, judgetime[i]);
					ps.setString(4, judgelookback[i]);
					ps.setInt(5, countanswer[i]);
					ps.setInt(6, judgecollect[i]);
					ps.setString(7, finalresults[i]);
					ps.setInt(8,knowledgeid);
					i+=ps.executeUpdate();
					
				}
				  
		   }
		   conn.close();
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
	public ArrayList<String> recommendforstudent(int studentid,int classid) throws FileNotFoundException, SQLException{
        String[] attrNames = new String[] {"time", "lookbacktime","answertrace","collect"};
    	// String[] attrNames = new String[] {"outlook","temperature","humidity","play"};
        // ��ȡ������
        Map<Object, List<Sample>> samples = readSamples(attrNames,classid);
       // System.out.println(samples.toString());
  	  	//���㽨ģʱ��
        long startTime1 = System.currentTimeMillis();    //��ȡ��ʼʱ��
        Object decisionTree = generateDecisionTree(samples, attrNames);
        long endTime1 = System.currentTimeMillis();    //��ȡ����ʱ��
        System.out.println(">>>>>������ģ�ͽ������");
        System.out.println("��ģʱ�䣺" + (endTime1 - startTime1) + "ms");
        // ���ɾ�����
        // ���������
       // outputDecisionTree(decisionTree, 0, null);
      
        
        //�����µ����ݼ�
      Testnewdata(attrNames,decisionTree,studentid);
       for(int i=0;i<correctrate.length;i++)
       {
    	   System.out.println(correctrate[i]);
       }
 	 for(int i=0;i<11;i++)
   	 {
    	recommends(i,correctrate);
  	 }
 	 Connection conn2=DBConn.getConnection();
	 PreparedStatement ps2=conn2.prepareStatement(getstudentkg);
	 ResultSet rs=null;
	 ps2.setInt(1,studentid);
	 rs=ps2.executeQuery();
	 if(rs.next())
	 {
		 Connection conn3=DBConn.getConnection();
	 	 PreparedStatement ps3=conn3.prepareStatement(updatedegree);
	 	  int ww=0;
	 	   int id;
	 	   for(int i=0;i<11;i++)
	 	   {
	 		  id=i+1;
	 		  conceptsrate.put(concepts[i], correctrate[i]);
	 		  System.out.println( conceptsrate);
	 		  String rate=""+correctrate[i];
			  ps3.setString(1,rate);
			  ps3.setInt(2, id);
			  ps3.setInt(3, studentid);
			  ww+=ps3.executeUpdate();
	 	    }
	 	    conn3.close();	 }
	 else
	 {
 	   Connection conn=DBConn.getConnection();
 	   PreparedStatement ps=conn.prepareStatement(addkgdegree);
 	   int ww=0;
 	   int id;
 	   for(int i=0;i<11;i++)
 	   {
 		  id=i+1;
 		  conceptsrate.put(concepts[i], correctrate[i]);
 		  System.out.println( conceptsrate);
 		  String rate=""+correctrate[i];
		  ps.setInt(1,studentid);
		  ps.setInt(2, id);
		  ps.setString(3, rate);
		  ww+=ps.executeUpdate();
 	    }
 	    conn.close();
	 }
	 conn2.close();
 	 List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(recommend.entrySet());            
     Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {  
              //��������  
     public int compare(Entry<String, Integer> o1, Entry<String,  Integer> o2) {  
               return o2.getValue().compareTo(o1.getValue());  
        }  
     });  
     String[] cc=new String[recommend.size()];
     int i=0;
     for (Entry<String, Integer> e: list) {  
	        System.out.print(e.getKey()+"="+e.getValue()+" ");
	        cc[i]=e.getKey();
	        i++;
	        recommends.put(e.getKey(),e.getValue());
	        System.out.println("xxx "+conceptsrate.get(e.getKey()));
        finalrecommend.put(e.getKey(),conceptsrate.get(e.getKey()));
    }  
     List<Entry<String, Double>> list2 = new ArrayList<Entry<String,Double>>(finalrecommend.entrySet());            
     Collections.sort(list2,new Comparator<Map.Entry<String, Double>>() {  
              //��������  
     public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
               return o1.getValue().compareTo(o2.getValue());  
        }  
     });  
     System.out.println();
     for (Entry<String, Double> e: list2) {  
    	 finalrecommends.put(e.getKey(),e.getValue());
	        System.out.print(e.getKey()+"="+e.getValue()+"  ");
            
     } 
     System.out.println();
     for(int z=0;z<cc.length-1;z++)
     {
    	 int j=z+1;
    	//System.out.println("55 "+(z==cc.length-2));
    	 if(recommends.get(cc[z])==recommends.get(cc[j]))
    	 {
    		// System.out.println("11 "+ z+" "+j+" "+recommends.get(cc[z])+" "+recommends.get(cc[j]));
    		 c.put(cc[z], finalrecommends.get(cc[z]));
    		 c.put(cc[j], finalrecommends.get(cc[z]));
    		/// System.out.println("22 "+c.toString());
    		 if(z==cc.length-2)
    		 {
    			 c= ss(c);
    		 }
    	 }
    	 else 
    	 {
    		// System.out.println("44 "+z+" "+c.size());
    		 if(c.size()!=0)
    			{
    		//	 System.out.println("33 "+c.toString());
    			 c= ss(c);
    			 c.clear();
    			}
    		 else
    			 result.add(cc[z]);
    	 }
    	
     }
     return result;
    }
    private static Map<String,Double> ss(Map<String,Double>c)
    {
    	  List<Entry<String, Double>> list3 = new ArrayList<Entry<String,Double>>(c.entrySet());
    	    
    	     Collections.sort(list3,new Comparator<Map.Entry<String, Double>>() {  
    	              //��������  
    	     public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
    	               return o1.getValue().compareTo(o2.getValue());  
    	        }  
    	     }); 
    	     for (Entry<String, Double> e: list3) {  
    	    	   result.add(e.getKey());
    	    	  // System.out.println("111 "+result.toString());
    		        //System.out.print("111 "+e.getKey()+"="+e.getValue()+"  ");
    	            
    	     }  
    	     //System.out.println("111 "+result.toString());
    	     return c;
    }
	private static void Testnewdata(String[] attrNames, Object decisionTree,int studentid) throws FileNotFoundException, SQLException {
		//������Լ�
//		 File file = new File("E:/֪ʶͼ���Ƽ�/ʵ����/1345"); 
//		 String[] flist = file.list();
		int correct=0;
		 HashMap<String, Map<String, Integer>> allNormalTF = new HashMap<String, Map<String,Integer>>();
		 Connection conn=DBConn.getConnection();
	     PreparedStatement ps=conn.prepareStatement(getstudentdata);
	     ps.setInt(1, studentid);
		 ResultSet rs=ps.executeQuery();
		 ArrayList<HandleTestResult> handledata=new ArrayList();
		
		 while(rs.next())
		{
			
			 HandleTestResult h=new HandleTestResult();
			 h.setStudentid(rs.getInt(1));
			 h.setTestid(rs.getInt(2));
			 h.setTime(rs.getInt(3));
			 h.setLook_back_times(rs.getInt(4));
			 h.setAnswer_trace(rs.getInt(5));
			 h.setCollect(rs.getInt(6));
			 h.setKnowledgeid(rs.getInt(7));
			 //System.out.println("size "+h.toString());
			 handledata.add(h);
		}
		 conn.close();
		 for(int j=0;j<concepts.length;j++)
		 {
			  correct=0;
//			  String filename="E:/֪ʶͼ���Ƽ�/ʵ����/1345/" + flist[j];
//			  filenames[j]=flist[j];
			  System.out.println(studentid);
		      //Object[][] TestData=gettestdata(studentid);
				int count=1;
//				ArrayList<Object> list=new ArrayList();
				
		        long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
				//�õ�ÿ�����������ķ���ֵ��
				int i=0;
				int z;
				while(i<handledata.size())
				{
					Sample sample = new Sample();
					if(handledata.get(i).getKnowledgeid()==(j+1))
					{
						sample.setAttribute(attrNames[0], handledata.get(i).getTime());
						sample.setAttribute(attrNames[1], handledata.get(i).getLook_back_times());
						sample.setAttribute(attrNames[2], handledata.get(i).getAnswer_trace());
						sample.setAttribute(attrNames[3], handledata.get(i).getCollect());
						String result=getcategoty(sample,decisionTree, 0);
						 if(result.equals("1"))
				             correct++;
				        System.out.println(result);
				        count++;
					}
					i++;
//					for(int h=0;j<4;j++)
//					{
//						z=h+1;
//						sample.setAttribute(attrNames[h], rs.getInt(z)); //�����ݸ�����ֵ�����
//						System.out.print( rs.getInt(z)+" ");
//					}
//					System.out.println();
//					//String result=getcategoty(sample,decisionTree, 0);
//					 if(result.equals("1"))
//			             correct++;
//			        System.out.println(result);
//			        count++;
				}
//			    for (Object[] row : TestData) {
//		            Sample sample = new Sample();       
//		            int i = 0;
//		            for (int n = row.length; i < n; i++)
//		            {
//		            	sample.setAttribute(attrNames[i], row[i]); //�����ݸ�����ֵ�����
//		            }
//		           // System.out.println(sample);
//		            String result=getcategoty(sample,decisionTree, 0);
//		            if(result.equals("1"))
//		            	correct++;
//		            System.out.println(result);
//		            count++;
//		        }
			    correctcount[j]=correct;
			    System.out.println("correctcount[j]"+correctcount[j]);
			    correctrate[j]=(double)correctcount[j]/(double)5;
			    //System.out.println(count);
			    long endTime = System.currentTimeMillis();
			    System.out.println(">>>>>�������");
			    System.out.println("����ʱ�䣺" + (endTime - startTime) + "ms");
			   
		 }
		 
		 recommends(0,correctrate);
	}
	public static double avgcorrect(double correctrate[])
	{
		double sum=0;
		for (int i=0;i<correctrate.length;i++)
		{
			sum=sum+correctrate[i];
		}
		return sum/correctrate.length;
	}
	public static int recommends(int i,double correctrate[])
	{
		double avg=avgcorrect(correctrate);
		System.out.println("avg "+avg);
			if(correctrate[i]<=avg)
		   {
			  System.out.println(concepts[i]);
			  if(recommend.get(concepts[i])==null)
			    {
				  recommend.put(concepts[i],1);
			    }
			  else
			  {
				  int a=recommend.get(concepts[i])+1;
				  recommend.put(concepts[i],a);
			  }
			  for(int j=0;j<11;j++)
			   {
				  if(edge[j][i]==1&&j!=i)
				   {
				    	if(correctrate[j]<=avg)
				    		{
				    		 // System.out.println(concepts[i]);
				    		  if(recommend.get(concepts[j])==null)
						      {
							    recommend.put(concepts[j],1);
						      }
						      else
						      {
							     int a=recommend.get(concepts[j])+1;
							     recommend.put(concepts[j],a);
						      }
				    		    recommends(j,correctrate);
				    		}
				    }
			   }
			 
			  return 1;
		   } 
			else
			  return 0;
		
	}
	public static String getcategoty( Sample sample, Object decisionTree, int level) {	
    		Boolean find=false;
             Tree tree = (Tree) decisionTree;
             String attrName = tree.getAttribute();
            // System.out.println("attName "+attrName+" level "+level);
             Object sampleValue =sample.getAttribute(attrName);
            // System.out.println( sampleValue);
             for (Object attrValue : tree.getAttributeValues()) {
            	 if(sampleValue!=null)
            	 {
            		 if(sampleValue.equals(attrValue)){
            		  //System.out.println(sampleValue+" "+ attrValue);
            		  find=true;
                	 Object child = tree.getChild(attrValue);
                	 if (child instanceof Tree) {
                		 getcategoty(sample,child, level + 1);
                	 }
                	 else{
//                		 System.out.println(sample);
                		 results=child.toString();
                		 break;
                	 }          
                   }    

            	 }
             }
             if(find==false)
             {
            	 //results="No Result";
             
             }
             //System.out.println("11"+results);
             return results;
    }


	/**
     * ��ȡ�ѷ����������������Map������ -> ���ڸ÷�����������б�
     * @throws FileNotFoundException 
	 * @throws SQLException 
     */    
//    ����map���͵���
    static Map<Object, List<Sample>> readSamples(String[] attrNames,int classid) throws FileNotFoundException, SQLException {  
       // Object[][] rawData=getrawdata(classid);
        Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getclassdata);
		ps.setInt(1, classid);
		ResultSet rs=ps.executeQuery();
		int i=0;
		int z;
		//  System.out.println(rawData.length);
        System.out.println("ѵ�����������");
        // ��ȡ�������Լ����������࣬�����ʾ������Sample���󣬲������໮��������
        Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
		while(rs.next())
		{
			Sample sample = new Sample();
			for(int j=0;j<4;j++)
			{
				z=j+1;
				sample.setAttribute(attrNames[j], rs.getInt(z)); //�����ݸ�����ֵ�����
			}
			z=5;
			 sample.setCategory(rs.getInt(z)); //��װ category=row[i]
	            List<Sample> samples = ret.get(rs.getInt(z));
	            if (samples == null) {
	                samples = new LinkedList<Sample>();
	                ret.put(rs.getInt(z), samples);
	            }
	            samples.add(sample); // ���������뵽��Ӧ�����ݽṹ��
		}
      
//        for (Object[] row : rawData) {
//        	//System.out.println(row[1]);
//            Sample sample = new Sample();
//            int i = 0;
//           // System.out.println("row.length-1 "+ (row.length-1));
//            for (int n = row.length-1; i < n; i++)
//            {
//            	//System.out.println(i);
//            	
//            	sample.setAttribute(attrNames[i], row[i]); //�����ݸ�����ֵ�����
//            	//System.out.println(" 111  "+   attrNames[i]+"  "+row[i]);
//            }
//            //System.out.println("111  "+i);
//            sample.setCategory(row[i]); //��װ category=row[i]
//            List<Sample> samples = ret.get(row[i]);
//            if (samples == null) {
//                samples = new LinkedList<Sample>();
//                ret.put(row[i], samples);
//            }
//            samples.add(sample); // ���������뵽��Ӧ�����ݽṹ��
//        }
		conn.close();
        return ret;
    }
     //����ѵ����
    private static Object[][] getrawdata(int classid) throws FileNotFoundException, SQLException  {
    	Object[][] TestData=new Object[5][4];
		Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getclassdata);
		ps.setInt(1, classid);
		ResultSet rs=ps.executeQuery();
		int i=0;
		while(rs.next())
		{
			for(int j=0;j<4;j++)
			{
				//System.out.println("j  "+dataIn[j]);
				int z=j+1;
				System.out.println(z+" "+i+" "+j);
				TestData[i][j]=rs.getInt(z);
			}
				i++;
		}
		return TestData;
	}
    
    //����û�б�ǩ�Ĳ��Լ�
   public static  Object[][] gettestdata(int studentid) throws FileNotFoundException, SQLException {
		Object[][] TestData=new Object[5][4];
		Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getstudentdata);
		ps.setInt(1, studentid);
		ResultSet rs=ps.executeQuery();
		int i=0;
		while(rs.next())
		{
			for(int j=0;j<4;j++)
			{
				//System.out.println("j  "+dataIn[j]);
				TestData[i][j]=rs.getInt(j);
			}
				i++;
		}
		return TestData;
	}
	/**
     * ���������

     */
    static Object generateDecisionTree(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
      
        // ���ֻ��һ��������������������������Ϊ�������ķ���
//        if (categoryToSamples.size() == 1)
//        {	 System.out.println(categoryToSamples.size());
//        	System.out.println("222");
//            return categoryToSamples.keySet().iterator().next();
//        }
 
        // ���û�й����ߵ����ԣ����������о�����������ķ�����Ϊ�������ķ��࣬��ͶƱѡ�ٳ�����
        if (attrNames.length == 0) {
        	//System.out.println("111");
            int max = 0;
            Object maxCategory = null;
            for (Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                int cur = entry.getValue().size();
                if (cur > max) {
                    max = cur;
                    maxCategory = entry.getKey();
                }
            }
            return maxCategory;
        }
 
        // ѡȡ��������
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);
 
        // ����������㣬��֧����Ϊѡȡ�Ĳ�������
        Tree tree = new Tree(attrNames[(Integer) rst[0]]);
        // ���ù��Ĳ������Բ�Ӧ�ٴα�ѡΪ��������
        String[] subA = new String[attrNames.length - 1];
        for (int i = 0, j = 0; i < attrNames.length; i++)
            if (i != (Integer) rst[0])
            {
            	System.out.println(attrNames[i]);
            	subA[j++] = attrNames[i];  //������Ҫ��ȥ��
            }
 
        // ���ݷ�֧�������ɷ�֧
        @SuppressWarnings("unchecked")
        
        Map<Object, Map<Object, List<Sample>>> splits =
        /* NEW LINE */(Map<Object, Map<Object, List<Sample>>>) rst[2];
        
        for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
           // System.out.println(entry);
        	Object attrValue = entry.getKey();//�õ�����Ҫ����������Ӧ��ֵ
            Map<Object, List<Sample>> split = entry.getValue();
            Object child = generateDecisionTree(split, subA);
            tree.setChild(attrValue, child);
        }   
        return tree;
    }
 
    /**
     * ѡȡ���Ų������ԡ�������ָ�������ѡȡ�Ĳ������Է�֧����Ӹ���֧ȷ��������
     * �ķ�����Ҫ����Ϣ��֮����С����ȼ���ȷ���������Ĳ������Ի�õ���Ϣ�������
     * �������飺ѡȡ�������±ꡢ��Ϣ��֮�͡�Map(����ֵ->(����->�����б�))
     */
    static Object[] chooseBestTestAttribute(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {
 
        int minIndex = -1; // ���������±�
        double minValue = Double.MAX_VALUE; // ��С��Ϣ��
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // ���ŷ�֧����
 
        // ��ÿһ�����ԣ����㽫����Ϊ�������Ե�������ڸ���֧ȷ���������ķ�����Ҫ����Ϣ��֮�ͣ�ѡȡ��СΪ����
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {
            int allCount = 0; // ͳ�����������ļ�����
 
            // ����ǰ���Թ���Map������ֵ->(����->�����б�)
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();  //
            for (Entry<Object, List<Sample>> entry : categoryToSamples  //�������ݽṹ��0��1��
                    .entrySet()) {
                Object category = entry.getKey();  //�õ�0ֵ
                List<Sample> samples = entry.getValue();  //�õ�������
                for (Sample sample : samples) {   //��������ÿ����������
                    Object attrValue = sample
                            .getAttribute(attrNames[attrIndex]);   //������ֵ�ó���
                    Map<Object, List<Sample>> split = curSplits.get(attrValue); //split=�������ݽṹ����
                    if (split == null) {
                        split = new HashMap<Object, List<Sample>>();
                        curSplits.put(attrValue, split);
                    }
                    List<Sample> splitSamples = split.get(category);  //�õ�������ΪXXX��
                    if (splitSamples == null) {
                        splitSamples = new LinkedList<Sample>();
                        split.put(category, splitSamples);
                    }
                    splitSamples.add(sample);
                }
                allCount += samples.size();
            }
 
            // ���㽫��ǰ������Ϊ�������Ե�������ڸ���֧ȷ���������ķ�����Ҫ����Ϣ��֮��
            double curValue = 0.0; // ���������ۼӸ���֧
            for (Map<Object, List<Sample>> splits : curSplits.values()) {
                double perSplitCount = 0;
                for (List<Sample> list : splits.values())
                    perSplitCount += list.size(); // �ۼƵ�ǰ��֧������
                double perSplitValue = 0.0; // ����������ǰ��֧
                for (List<Sample> list : splits.values()) {
                    double p = list.size() / perSplitCount;
                    perSplitValue -= p * (Math.log(p) / Math.log(2));
                }
                curValue += (perSplitCount / allCount) * perSplitValue;
            }
 
            // ѡȡ��СΪ����
            if (minValue > curValue) {
                minIndex = attrIndex;
                minValue = curValue;
                minSplits = curSplits;
            }
        }
        return new Object[] { minIndex, minValue, minSplits };
    }
 
    /**
     * ���������������׼���
     */
    static void outputDecisionTree(Object obj, int level, Object from) {
        for (int i = 0; i < level; i++)
            System.out.print("|-----");
        if (from != null)
        	
            System.out.printf("(%s):", from);
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            String attrName = tree.getAttribute();
            System.out.printf("[%s = ?]\n", attrName);
            for (Object attrValue : tree.getAttributeValues()) {
                Object child = tree.getChild(attrValue);
                outputDecisionTree(child, level + 1, attrName + " ==== "
                        + attrValue);
            }
        } else {
            System.out.printf("[CATEGORY = %s]\n", obj);
        }
    }
 
    /**
     * ����������������Ժ�һ��ָ��������������ķ���ֵ
     */
    static class Sample {
 
        private Map<String, Object> attributes = new HashMap<String, Object>();
 
        private Object category;
 
        public Object getAttribute(String name) {
            return attributes.get(name);
        }
 
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }
 
        public Object getCategory() {
            return category;
        }
 
        public void setCategory(Object category) {
            this.category = category;
        }
 
        public String toString() {
            return attributes.toString();
        }
 
    }
 
    /**
     * ����������Ҷ��㣩���������е�ÿ����Ҷ��㶼������һ�þ�����
     * ÿ����Ҷ������һ����֧���ԺͶ����֧����֧���Ե�ÿ��ֵ��Ӧһ����֧���÷�֧������һ���Ӿ�����
     */
    static class Tree {
 
        private String attribute;
 
        private Map<Object, Object> children = new HashMap<Object, Object>();
 
        public Tree(String attribute) {
            this.attribute = attribute;
        }
 
        public String getAttribute() {
            return attribute;
        }
 
        public Object getChild(Object attrValue) {
            return children.get(attrValue);
        }
 
        public void setChild(Object attrValue, Object child) {
            children.put(attrValue, child);
        }
 
        public Set<Object> getAttributeValues() {
            return children.keySet();
        }
 
    }
}

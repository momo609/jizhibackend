package com.jizhitest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jizhibackend.bean.AllTestresult;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.R_Result_Timeuse;
import com.jizhibackend.bean.TestResult;
import com.jizhitest.db.DBConn;
import com.jizhitest.service.MyTestDaoImpl;

public class TestResultDaoImpl extends BaseDaoImpl{
	private  String addTestResultStmt = "com.jizhitest.mapping.testResultMapping.addTestResult";
	private  String addTestResultTimeStmt = "com.jizhitest.mapping.testResultMapping.addTimeuseOfResult";
	private  String findTestResultStmt = "com.jizhitest.mapping.testResultMapping.findTestResult";
//	private  String findTestResultByIdStmt = "com.jizhitest.mapping.testResultMapping.findTestResultById";
	private  String findTestResultByIdStmt = "com.jizhitest.mapping.testResultMapping.findTestAndNameResultById";
	private  String getAvgTimeOfEachQuestionStmt = "com.jizhitest.mapping.testResultMapping.getAvgTimeuseOfEachQuestionByTestid";
	private String getAvgLookbacktimeOfEachQuestionStmt="com.jizhitest.mapping.testResultMapping.getAvgLookbacktimeOfEachQuestionByTestid";
    private String getstatSql="SELECT MAX(score),MIN(score),AVG(score) FROM `test_result` WHERE testid=?";
    private String gettestid="SELECT testid,studentid,answers,score,answer_trace,time_used,total_time_used,look_back_times,proportion,tagproportion FROM `test_result` WHERE studentid=?";
    private String gettestresult="SELECT resultid,testid,studentid,answers,score,answer_trace,time_used,total_time_used,look_back_times,proportion,tagproportion FROM `test_result` WHERE testid=?";
    private String getallconcepts="SELECT question.tag FROM 'question','test','r_testpaper_question' WHERE test.id=1879591991 AND r_testpaper_question.paperid=test.use_paperid AND r_testpaper_question.questionid=question.id";
    
    public Map <String,Integer> getallconceptsbytestids(int testid) throws SQLException{
    	Map <String,Integer> concepts=new HashMap <String,Integer>();
		Connection conn=DBConn.getConnection();
    	PreparedStatement ps=conn.prepareStatement(getallconcepts);
		ps.setInt(1, testid);
		ResultSet rs=ps.executeQuery();
		int i=1;
		while(rs.next())
		{
		      if(concepts.get(rs.getString(1)) != null)
		      {
		    	  concepts.put(rs.getString(1),i);
		    	  //将concepts插入class_concepts表
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  
		    	  i++;
		      }
		}
		return concepts;
    }
    public int inserthandledata(int testid) throws SQLException{
//    	String concepts[]={"函数依赖","部分函数依赖","传递函数依赖","1NF","2NF","3NF","BCNF","决定因素","码","主属性","非主属性"};
    	Map <String,Integer>concepts=getallconceptsbytestids(testid);
		   int question[][]=new int[56][11];
		   String writes=null;
		   String filename=null;
		   TestResultDaoImpl dao=new TestResultDaoImpl();
		  List<AllTestresult> testresults=dao.getalltestresult(testid);
//		   System.out.println(testresults.toString());
		  List <Integer> avgTimeList=dao.getAvgTimeOfEachQuestion(testresults.get(1).getTestid());  //每道题的平均时间
		   float[] avgLookbacktimes=dao.getAvgLookbacktimeOfEachQuestion(testresults.get(1).getTestid()); //每道题的平均回看次数
		   int order=0;
		   int[] testids=new int[testresults.size()];
		   int[] studentids=new int[testresults.size()];
		   int[] results=new int[testresults.size()];
		   int m=0;
		   // FileOutputStream o2= new FileOutputStream("E:/知识图谱推荐/全部实验结果/results.txt");
		   for(int j=0;j<testresults.size();j++)
		   {
			    MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
				UserDaoImpl userdao = new UserDaoImpl();
				MyTest t=myTestDaoImpl.getTestByid(testresults.get(j).getTestid());
				int paperid=t.getUse_paperid();
				QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
				List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
				List<Question>collectlist=questionDaoImpl.getMarkedQuestions(testresults.get(j).getStudentid(), 1);
//				System.out.println("11   "+testresults.get(j).getStudentid());
//				System.out.println(qlist.toString());
				testids[j]=testresults.get(j).getStudentid();
			  studentids[j]=testresults.get(j).getStudentid();
				results[j]=testresults.get(j).getResultid();
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
					//如果填空题中有个空需要填两个单词，标准情况下是每个单词以一个空格隔开，但是如果考生用大于一个空格隔开，此时应该先对这种情况进行处理，去掉多余的空格
					stuAnswer=stuAnswer.replaceAll("\\s+"," ");//只去掉中间多余空格 \\s+表示多个空格
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
					}else{//不对则添加到错题列表中去
						finalresults[i]="0";
						wrongqlist.add(qlist.get(i));
						if(count(stuAnswer,correct)!=0)
							countanswer[i]=3;
						else
							countanswer[i]=2;
					}
					
				}
				
				String sql="insert into handle_test_result(resultid,testid,studentid,answers,answer_trace,time,look_back_time) values(?,?,?,?,?,?,?)";
			    Connection conn=DBConn.getConnection();		   
			    PreparedStatement ps=conn.prepareStatement(sql);
				for(int i=0;i<qlist.size();i++)
				{
					String s=qlist.get(i).getTag();
					
					s.replaceAll("\\?", " "); 
					s=s.trim();
					try {
						ps.setInt(1, results[i]);
						ps.setInt(2, testids[i]);
						ps.setInt(3, studentids[i]);
						ps.setInt(4, Integer.parseInt(judgetime[i]));
						ps.setInt(5, Integer.parseInt(judgelookback[i]));
						ps.setInt(6, countanswer[i]);
				        ps.setInt(7, Integer.parseInt(finalresults[i]));
						i=ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				m=ps.executeUpdate();
				conn.close();
		   }
		return m;
		   
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
	       //遍历数组的每个元素    
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
   
    
    public List<AllTestresult> getalltestid(int userid){
    	ArrayList <AllTestresult> testids=new ArrayList<AllTestresult>();
		Connection conn=DBConn.getConnection();
		try
		{
			PreparedStatement ps=conn.prepareStatement(gettestid);
			ps.setInt(1, userid);
		//	ps.setInt(2, userid);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
			   AllTestresult results=new AllTestresult();
			   //System.out.println(rs.getInt(1));
			   results.setTestid(rs.getInt(1));
			   results.setStudentid(rs.getInt(2));
			   results.setAnswers(rs.getString(3));
			   results.setScore(rs.getInt(4));
			   results.setAnswer_trace(rs.getString(5));
			   results.setTime_used(rs.getString(6));
			   results.setTotal_time_used(rs.getInt(7));
			   results.setLook_back_times(rs.getString(8));
			   results.setProportion(rs.getString(9));
			   results.setTagproportion(rs.getString(10));
			   testids.add(results);
			  // System.out.println(rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println("testid.length "+testids.size());
		return testids;
    }
    public List<AllTestresult> getalltestresult(int testid){
    	ArrayList <AllTestresult> testresults=new ArrayList<AllTestresult>();
		Connection conn=DBConn.getConnection();
		try
		{
			PreparedStatement ps=conn.prepareStatement(gettestresult);
			ps.setInt(1, testid);
		//	ps.setInt(2, userid);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
			   AllTestresult results=new AllTestresult();
			   //System.out.println(rs.getInt(1));
			   results.setTestid(rs.getInt(1));
			   results.setStudentid(rs.getInt(2));
			   results.setAnswers(rs.getString(3));
			   results.setScore(rs.getInt(4));
			   results.setAnswer_trace(rs.getString(5));
			   results.setTime_used(rs.getString(6));
			   results.setTotal_time_used(rs.getInt(7));
			   results.setLook_back_times(rs.getString(8));
			   results.setProportion(rs.getString(9));
			   results.setTagproportion(rs.getString(10));
			   testresults.add(results);
			  // System.out.println(rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println("testid.length "+testids.size());
		return testresults;
    }
	public int addTestResult(TestResult t,List<Integer> tArray)
	{
		
		int i=session.insert(addTestResultStmt,t);
		session.commit();
		R_Result_Timeuse r=new R_Result_Timeuse();
		r.setResultid(t.getResultid());
		addResultUseTime(tArray,r);
		return i;
		
	}
	public List<Integer> getAvgTimeOfEachQuestion(int testid)
	{
		List<Integer> list=session.selectList(getAvgTimeOfEachQuestionStmt, testid);
	
		session.commit();
		
		return list;
		
	}
	public float[] getAvgLookbacktimeOfEachQuestion(int testid)
	{
		List<String> list=session.selectList(getAvgLookbacktimeOfEachQuestionStmt, testid);
	
		session.commit();
		float avglookbacktime[] = new float[56];
		//System.out.println(list.size());
		for (String s:list)
		{
//			System.out.println(s);
			String eachbacktime[]=s.split(",");
			//System.out.println(eachbacktime.length);
			for(int i=0;i<eachbacktime.length;i++)
			{
				avglookbacktime[i]=avglookbacktime[i]+(float)Integer.parseInt(eachbacktime[i]);
			}
		}
		for(int i=0;i<avglookbacktime.length;i++)
		{
			float a=avglookbacktime[i];
			//System.out.println(avglookbacktime[i]);
			avglookbacktime[i]=a/list.size();
		}
		return avglookbacktime;
		
	}
	public boolean addResultUseTime(List<Integer> tArray,R_Result_Timeuse r)
	{
		int i=0;
		for(int t:tArray)
		{
			r.setQ_order(++i);
            r.setTime_used(t);
			session.insert(addTestResultTimeStmt,r);
		}
		
		session.commit();
		return true;
		
	}
	public Map<String,Object> getTestStat(int testid,int userid)
	{
		Map<String,Object> map=new HashMap<String, Object>();
		Connection conn=DBConn.getConnection();
		try
		{
			PreparedStatement ps=conn.prepareStatement(getstatSql);
			ps.setInt(1, testid);
		//	ps.setInt(2, userid);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
			map.put("maxscore",rs.getInt(1));
			map.put("minscore",rs.getInt(2));
			map.put("avgscore",rs.getInt(3));
			}
			List <Integer> avgTimeList=getAvgTimeOfEachQuestion(testid);
	         Integer[] a = null;
	         map.put("timeused",avgTimeList.toArray(new Integer[avgTimeList.size()]));
	         a=avgTimeList.toArray(new Integer[avgTimeList.size()]);
//	         System.out.println("Lingyigeshij:"+Arrays.toString(a));
	         
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	public TestResult findTestResult(int testid,int userid)
	{
		
		TestResult t=new TestResult();
		t.setTestid(testid);
		t.setStudentid(userid);
		 t=session.selectOne(findTestResultStmt,t);
//         List <Integer> avgTimeList=getAvgTimeOfEachQuestion(testid);
//         if(t!=null)
//         if(avgTimeList.size()!=0)
//         {
////         Integer[] a=avgTimeList.toArray(new Integer[avgTimeList.size()]);
////         t.setTime_used(Arrays.toString(a));
////         System.out.println("111:"+Arrays.toString(a));
//         }
//		 System.out.println("用户时间："+t.getTime_used());
		return t;
	}
	public List<TestResult> findTestResultByTestid(int testid) {
		List<TestResult> t=null;
		
		 t=session.selectList(findTestResultByIdStmt,testid);
		 UserDaoImpl userdao = new UserDaoImpl();
		 MyTestDaoImpl dao = new MyTestDaoImpl();
//		 for (int i=0;i<t.size();i++) {
//			 t.get(i).setProportion(MyTestDaoImpl.proportionTest(testid, t.get(i).getAnswers()).toString());
////			 t.get(i).setStudentname(userdao.findUser(t.get(i).getStudentid()).getNickname());
//			 t.get(i).setTagproportion(MyTestDaoImpl.proportionTag(testid, t.get(i).getAnswers()).toString());
//			 dao.setPo(t.get(i).getStudentid(), t.get(i).getProportion(), t.get(i).getTagproportion());
//		}
		 
		return t;
	}
	public void inserttest() throws SQLException
	{
		String sql="insert into insert_test(value) values(2)";
		 Connection conn = DBConn.getConnection();
		 long start=System.currentTimeMillis();
		 
		try {
			conn.setAutoCommit(false);
			java.sql.PreparedStatement s = conn.prepareStatement(sql);
			for(int i=0;i<30000;i++)
			{
			s.addBatch();
			}
			
			s.executeBatch();
			
		} catch (SQLException e) {
			
				conn.rollback();
			
		}
		long end=System.currentTimeMillis();
		System.out.println(end-start);
	
	}
public static void main(String[] args) {
	TestResultDaoImpl impl=new TestResultDaoImpl();
	List<Integer> list=impl.getAvgTimeOfEachQuestion(-1452012222);
	System.out.println();
}

}

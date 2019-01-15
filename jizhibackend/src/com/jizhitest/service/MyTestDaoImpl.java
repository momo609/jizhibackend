package com.jizhitest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jizhibackend.bean.MyClass;
import com.jizhibackend.bean.MyTest;
import com.jizhibackend.bean.Paper;
import com.jizhibackend.bean.Question;
import com.jizhibackend.bean.User;
import com.jizhitest.db.DBConn;

public class MyTestDaoImpl extends BaseDaoImpl{
	
	private  String getTestOfClassStmt = "com.jizhitest.mapping.mytestMapping.getTestOfClass";
//	private  String getDoneTestOfClassStmt = "com.jizhitest.mapping.mytestMapping.getDoneTestOfClass";
//	private  String getBeforeTestOfClassStmt = "com.jizhitest.mapping.mytestMapping.getBeforeTestOfClass";
	private  String deleteTestStmt = "com.jizhitest.mapping.mytestMapping.deleteTestById";
	private  String getTestByIdStmt = "com.jizhitest.mapping.mytestMapping.getTestById";
	private  String getpaperbyid = "com.jizhitest.mapping.paperMapping.gettestpaperbyid";
	public List<MyTest> getTestsOfUsers(User user,int type)
	{
		 	List<MyTest> testList=new ArrayList<MyTest>();
		 	List<MyTest> testList1=new ArrayList<MyTest>();
		 	MyClassDaoImpl daoImpl=new MyClassDaoImpl();
			List<MyClass> classlist=daoImpl.ClassofStudent(user.getUserid());
			String sql = "";
			for(MyClass cls:classlist)
			{
				if(type==1){
					sql = "SELECT testid,title,start_time,end_time,create_time,owner,use_paperid,privilege FROM r_class_test,test WHERE r_class_test.classid=? AND ?-test.end_time<=0 AND r_class_test.testid=test.id ORDER BY start_time DESC";
				}else if(type==2){
					sql = "SELECT testid,title,start_time,end_time,create_time,owner,use_paperid,privilege FROM r_class_test,test WHERE r_class_test.classid=? AND ?-test.end_time>0 AND r_class_test.testid=test.id ORDER BY start_time DESC";
				}else if(type==3){
					sql = "SELECT testid,title,start_time,end_time,create_time,owner,use_paperid,privilege FROM r_class_test,test WHERE r_class_test.classid=? AND ?-test.start_time<=0 AND r_class_test.testid=test.id ORDER BY start_time DESC";	
				}
				testList =getTestsOfClass(cls.getId(),sql);
				for(MyTest test:testList)
				{
					test.setMyclass(cls);
					test.setqNum(getPaperofId(test.getUse_paperid()).getQuestion_num());
				}
				testList1.addAll(testList);
			}
		return testList1;
		
	}
//	//正在进行中的测试列表
//	public List<MyTest> getTestsOfClass(int classid)
//	{
//		List<MyTest> list=session.selectList(getTestOfClassStmt,classid);
//		
//		return list;
//		
//	}
//	
	public List<MyTest> getTestsOfClass(int classid,String sql)
	{
		int i=0;
		ResultSet rs = null;
		List<MyTest> testlist=new ArrayList<MyTest>();
		long time = System.currentTimeMillis();
		try {
			
//			String sql="SELECT testid,title,start_time,end_time,create_time,owner,use_paperid,privilege FROM r_class_test,test WHERE r_class_test.classid=? AND ?-test.end_time<=0 AND r_class_test.testid=test.id ORDER BY start_time DESC";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ps.setInt(1, classid);
		    ps.setLong(2, time);
			rs=ps.executeQuery();
			while(rs.next())
			{
				MyTest test= new MyTest();
				test.setTestid(rs.getInt(1));
				test.setTitle(rs.getString(2));
				test.setStart_time(rs.getLong(3));
				test.setEnd_time(rs.getLong(4));
				test.setCreate_time(rs.getLong(5));
				test.setOwner(rs.getInt(6));
				test.setUse_paperid(rs.getInt(7));
				test.setPrivilege(rs.getInt(8));
				testlist.add(test);
				
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return testlist;
	}

	public Paper getPaperofId(int paperid)
	{
		Paper paper=session.selectOne(getpaperbyid,paperid);
		return paper;
	}
	public static int scoreTest(int testid,String answer,int userid)
	{
		int score=0;
		MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
		UserDaoImpl userdao = new UserDaoImpl();
		User user = userdao.findUser(userid);
		MyTest t=myTestDaoImpl.getTestByid(testid);
		long endTime = t.getEnd_time();
//		long currTime = System.currentTimeMillis();
		String title = t.getTitle();
		int paperid=t.getUse_paperid();
		QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
		List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
		String[] answers=answer.split("@@");
		for(int i=0;i<qlist.size();i++)
		{
			String correct = qlist.get(i).getAnswer().trim();
			String stuAnswer = answers[i].trim();
//			if(correct.indexOf(" ")!=-1){
//				String[] cs = correct.split(" ");
//				String[] stus = stuAnswer.split(" ");
//				int k = 0;
//				for (int j = 0; j < stus.length; j++) {
//					if(!cs[i].trim().equalsIgnoreCase(stus[i].trim())){
//						break;
//					}else{
//						k=k+1;
//					}
//				}
//				if(k==stus.length){
//					score+=qlist.get(i).getPoint();
//				}
//			}
			//如果填空题中有个空需要填两个单词，标准情况下是每个单词以一个空格隔开，但是如果考生用大于一个空格隔开，此时应该先对这种情况进行处理，去掉多余的空格
			stuAnswer=stuAnswer.replaceAll("\\s+"," ");//只去掉中间多余空格 \\s+表示多个空格
			
			if(correct.equalsIgnoreCase(stuAnswer))
			{
				score+=qlist.get(i).getPoint();
			}else{//不对则添加到错题列表中去
//				if(currTime<endTime){
//					try {
//						Thread.sleep(endTime - currTime);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					questionDaoImpl.addWrongQuestions(userid, qlist.get(i).getId()+"", testid, title,endTime);
//				}else{
//					questionDaoImpl.addWrongQuestions(userid, qlist.get(i).getId()+"", testid, title);
//				}
			}
		}
		userdao.updateCoins(user.getCoins()+score, userid);
		userdao.addCoinMessage(userid, 0, score);
		return score;
	}
	//计算每种level的错题率
	public static ArrayList<Integer> proportionTest(int testid,String answer){
		int[] numLevel = {0,0,0};
		int[] numWrong = {0,0,0};
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		ArrayList<Integer> proportion = new ArrayList<Integer>();
		MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
		MyTest t=myTestDaoImpl.getTestByid(testid);
		int paperid=t.getUse_paperid();
		QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
		List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
		String[] answers=answer.split("@@");
		for(int i=0;i<qlist.size();i++){
			if(qlist.get(i).getLevel()==1){
				numLevel[0] +=1;
				list1.add(i);
			}else if(qlist.get(i).getLevel()==2){
				numLevel[1] +=1;
				list2.add(i);
			}else if(qlist.get(i).getLevel()==3){
				numLevel[2] +=1;
				list3.add(i);
			}
		}
		for(int i=0;i<list1.size();i++){
			if(!(qlist.get(list1.get(i)).getAnswer().trim().equals(answers[list1.get(i)].trim().replaceAll("\\s+"," ")))){
				numWrong[0] +=1;
			}
		}
		for(int i=0;i<list2.size();i++){
			if(!(qlist.get(list2.get(i)).getAnswer().trim().equals(answers[list2.get(i)].trim().replaceAll("\\s+"," ")))){
				numWrong[1] +=1;
			}
		}
		for(int i=0;i<list3.size();i++){
			if(!(qlist.get(list3.get(i)).getAnswer().trim().equals(answers[list3.get(i)].trim().replaceAll("\\s+"," ")))){
				numWrong[2] +=1;
			}
		}
		proportion.add(numWrong[0]);
		proportion.add(numWrong[1]);
		proportion.add(numWrong[2]);
		return proportion;
	}
	
	//计算每个知识点的错题率
	public static Map<String,String> proportionTag(int testid,String answer){
		//map的键是知识点名称，值是该知识点拥有题目的数量
		Map<String,Integer> map = new HashMap<String,Integer>();
		//map1的键是知识点名称，值是该知识点错题的数量
		Map<String,Integer> map1 = new HashMap<String,Integer>();
		MyTestDaoImpl myTestDaoImpl=new MyTestDaoImpl();
		MyTest t=myTestDaoImpl.getTestByid(testid);
		int paperid=t.getUse_paperid();
		QuestionDaoImpl questionDaoImpl=new QuestionDaoImpl();
		List<Question> qlist=questionDaoImpl.getQustionsOfTestPaper(paperid);
		String[] answers=answer.split("@@");
		//拿到所有知识点，并且每个知识点下所有问题的题号
		for(int i=0;i<qlist.size();i++){
			String[] tag = qlist.get(i).getTag().split(",");
			for (int j = 0; j < tag.length; j++) {
				String ta = tag[j].trim();
				if(!map.containsKey(ta)){
					map.put(ta, 1);
					if(!((qlist.get(i).getAnswer().trim()).equalsIgnoreCase(answers[i].trim().replaceAll("\\s+"," ")))){
						map1.put(ta,1);
					}else{
						map1.put(ta,0);
					}
				}else{
					map.put(ta, map.get(ta)+1);
					if(!((qlist.get(i).getAnswer().trim()).equalsIgnoreCase(answers[i].trim().replaceAll("\\s+"," ")))){
						map1.put(ta,map1.get(ta)+1);
					}else{
						map1.put(ta,map1.get(ta));
					}
				}
			}

		}
		//map2为每个知识点错题率
		Map<String,String> map2 = new HashMap<String,String>();
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		for (String key : map.keySet()) {
			map2.put(key, numberFormat.format((float)map1.get(key)/(float)map.get(key)));
		}
		return map2;
	}
	
	public MyTest getTestByid(int testid)
	{
		MyTest mytest=session.selectOne(getTestByIdStmt,testid);
		return mytest;
		
	}

	public boolean createTest(MyTest mytest)
	{
	
		int i=0;
		try {
			String sql="insert into test(title,start_time,end_time,create_time,owner,use_paperid,id,privilege) values(?,?,?,?,?,?,?,?)";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, mytest.getTitle());
			ps.setLong(2, mytest.getStart_time());
			ps.setLong(3, mytest.getEnd_time());
			ps.setLong(4, mytest.getCreate_time());
			ps.setInt(5, mytest.getOwner());	
			ps.setInt(6, mytest.getUse_paperid());
			ps.setInt(7, mytest.getTestid());
			ps.setInt(8, mytest.getPrivilege());
			i=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(i==0)
			return false;
		else return true;
	
	}
	
	public boolean setTakePartInClass(MyTest mytest,int classid)
	{
	
		int i=0;
	    
		try {
			String sql="insert into r_class_test(classid,testid) values(?,?)";
		    Connection conn=DBConn.getConnection();		   
		    PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, classid);
			ps.setInt(2, mytest.getTestid());
			i=ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(i==0)
			return false;
		else return true;
	
	}
	
	
//	public boolean setPo(int studentid,String pop,String tagPop)
//	{
//	
//		int i=0;
//	    
//		try {
//			String sql="update test_result set proportion=?,tagproportion=? where studentid=?";
//		    Connection conn=DBConn.getConnection();		   
//		    PreparedStatement ps=conn.prepareStatement(sql);
//			ps.setString(1, pop);
//			ps.setString(2, tagPop);
//			ps.setInt(3, studentid);
//			i=ps.executeUpdate();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		if(i==0)
//			return false;
//		else return true;
//	
//	}
	
	
	public int deleteTestById(int testid)
	{
		int i=session.delete(deleteTestStmt,testid);
		session.commit();
		return i;
		
	}
	
	public List<Integer> findTestByOwnerid(int ownerid){
		List<Integer> testlist = new ArrayList<Integer>();
		Connection conn=DBConn.getConnection();
		String sql="select id from test where owner=?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, ownerid);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				testlist.add(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return testlist;
	}

}

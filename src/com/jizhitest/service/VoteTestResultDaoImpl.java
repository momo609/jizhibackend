package com.jizhitest.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jizhibackend.bean.R_Result_Timeuse;
import com.jizhibackend.bean.TestResult;
import com.jizhibackend.bean.VoteTestResult;
import com.jizhitest.db.DBConn;

public class VoteTestResultDaoImpl extends BaseDaoImpl{
	private  String addTestResultStmt = "com.jizhitest.mapping.voteResultMapping.addTestResult";
	private  String addTestResultTimeStmt = "com.jizhitest.mapping.testResultMapping.addTimeuseOfResult";
	private  String findTestResultStmt = "com.jizhitest.mapping.voteResultMapping.findTestResult";
//	private  String findTestResultByIdStmt = "com.jizhitest.mapping.testResultMapping.findTestResultById";
	private  String findTestResultByIdStmt = "com.jizhitest.mapping.testResultMapping.findTestAndNameResultById";
	private  String getAvgTimeOfEachQuestionStmt = "com.jizhitest.mapping.testResultMapping.getAvgTimeuseOfEachQuestionByTestid";
    private String getstatSql="SELECT AVG(score) FROM `vote_result2` WHERE votetestid=? AND studentid=?";
	public int addTestResult(VoteTestResult t)
	{
		
		int i=session.insert(addTestResultStmt,t);
		session.commit();
		return i;
		
	}
	public List<Integer> getAvgTimeOfEachQuestion(int testid)
	{
		List<Integer> list=session.selectList(getAvgTimeOfEachQuestionStmt, testid);
	
		session.commit();
		
		return list;
		
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
	public VoteTestResult findTestResult(int votetestid,int studentid,int userid)
	{
		
		VoteTestResult t=new VoteTestResult();
		t.setVoteTestid(votetestid);
		t.setStudentid(studentid);
		t.setUserid(userid);
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
	public  int findTestResults(int votetestid,int studentid)
	{
			int score=-1;
			Connection conn=DBConn.getConnection();
			try
			{
				PreparedStatement ps=conn.prepareStatement(getstatSql);
				ps.setInt(1, votetestid);
				ps.setInt(2, studentid);
				ResultSet rs=ps.executeQuery();
				if(rs.next())
				{
					score=rs.getInt(1);
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return score;
	}
	
	public	List<String> findVoteResults(int votetestid,int studentid)
	{
		List<String> studentVoteDetail = new ArrayList<String>();
		Connection conn = DBConn.getConnection();
		String Sql="SELECT userid,score FROM `vote_result2` WHERE votetestid=? AND studentid=? ORDER BY userid ASC";
		try{
			
			PreparedStatement ps=conn.prepareStatement(Sql);
			ps.setInt(1, votetestid);
			ps.setInt(2, studentid);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				studentVoteDetail.add(rs.getInt(1)+"@"+rs.getInt(2));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return studentVoteDetail;
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
